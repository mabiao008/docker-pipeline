package com.github.jadepeng.pipeline.agent.jobrunner.docker;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.jadepeng.pipeline.agent.config.ApplicationProperties;
import com.github.jadepeng.pipeline.core.PipelineLogger;


@Component
public class DockerEngine {
    private final Logger log = LoggerFactory.getLogger(DockerEngine.class);
    private final ApplicationProperties properties;
    private final DockerClient client;

    public DockerEngine(
        ApplicationProperties properties){
        this.properties = properties;
        this.client = this.createDockerClient();
    }

    public void removeOldContainer(String name) {
        try {
            this.client.stopContainerCmd(name).exec();
        } catch (Exception ex) {
        }
        try {
            this.client.removeContainerCmd(name).exec();
        } catch (Exception ex) {
        }
    }

    private DockerClient createDockerClient() {
        DefaultDockerClientConfig.Builder builder =
            DefaultDockerClientConfig.createDefaultConfigBuilder()
                                     .withDockerHost(
                                         "tcp://" + this.properties.getDockerServer() + ":2375")
                                     .withApiVersion("1.30");
        if (properties.getDockerTlsVerify()) {

            String finalCertPath = properties.getDockerCertPath();

            File serverCertFile = Paths
                .get(properties.getDockerCertPath(), this.properties.getDockerServer())
                .toFile();

            // 如果服务器有自定义cert,使用服务器自定义
            if (serverCertFile.exists() && serverCertFile.isDirectory()) {
                finalCertPath = serverCertFile.getAbsolutePath();
            }

            builder = builder.withDockerTlsVerify(true)
                             .withDockerCertPath(finalCertPath);
        }

        return DockerClientBuilder.getInstance(builder.build()).build();
    }

    public InspectContainerResponse getContainer(String name) {
        try {
            return this.client.inspectContainerCmd(name).exec();
        } catch (Exception ex) {
            return null;
        }
    }

    public void getContainerInfo(String containerId) {
        InspectContainerResponse inspectContainerResponse =
            this.client.inspectContainerCmd(containerId).exec();
        System.out.println(inspectContainerResponse.getState().getRunning());
    }

    public String waitContainerFinish(String containerId,
                                      DockerClient dockerClient) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            dockerClient.logContainerCmd(containerId)
                        .withStdErr(true)
                        .withStdOut(true)
                        .withFollowStream(true)
                        .withTailAll()
                        .exec(new LogContainerResultCallback() {
                            @Override
                            public void onNext(Frame item) {
                                stringBuilder.append(item.toString());
                                System.out.println(item);
                            }
                        }).awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 启动容器
     *
     * @param image
     * @param name
     * @param portBinds
     * @param volumeBinds
     * @return
     */
    public String startContainer(String image,
                                 String name,
                                 List<ContainerPortBind> portBinds,
                                 List<ContainerVolumeBind> volumeBinds,
                                 List<String> extraHosts,
                                 List<String> envs,
                                 List<String> entrypoints,
                                 String... cmds) {
        List<Volume> volumes = new ArrayList<>();
        List<Bind> volumesBinds = new ArrayList<>();

        if (volumeBinds != null) {
            for (ContainerVolumeBind volumeBind : volumeBinds) {
                Volume volume = new Volume(volumeBind.containerPath);
                volumes.add(volume);
                volumesBinds.add(new Bind(volumeBind.getHostPath(), volume));
            }
        }

        Ports portBindings = new Ports();
        if (portBinds != null) {
            for (ContainerPortBind portBind : portBinds) {
                portBindings.bind(ExposedPort.tcp(portBind.getContainerPort()),
                                  Ports.Binding
                                      .bindPort(portBind.getHostPort()));
            }
        }

        // 删除老的数据
        this.removeOldContainer(name);

        CreateContainerCmd cmd = this.client.createContainerCmd(image)
                                             .withName(name)
                                             .withVolumes(volumes)
                                             .withBinds(volumesBinds);

        if (portBinds != null && portBinds.size() > 0) {
            cmd = cmd.withPortBindings(portBindings);
        }

        if (cmds != null && cmds.length > 0) {
            cmd = cmd.withCmd(cmds);
        }

        if (extraHosts != null && extraHosts.size() > 0) {
            cmd.withExtraHosts(extraHosts);
        }

        if (envs != null) {
            cmd.withEnv(envs);
        }

        if (entrypoints != null) {
            cmd.withEntrypoint(entrypoints);
        }

        CreateContainerResponse container = cmd.exec();
        System.out.print(container);
        this.client.startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public void pullImage(String image) {
        this.client.pushImageCmd(image).exec(new PushImageResultCallback());
    }

    /**
     * 获取overlay网络
     *
     * @param dockerClient
     * @param name
     * @return
     */
    public String getOverlayNetworkId(DockerClient dockerClient, String name) {
        List<Network> networks =
            dockerClient.listNetworksCmd().withNameFilter(name).exec();
        if (networks.size() > 0) {
            Network network =
                networks.stream().filter(n -> n.getDriver().equals("overlay"))
                        .findFirst().orElse(null);
            if (network != null) {
                return network.getId();
            }
        }
        return null;
    }

    /**
     * 创建overlay网络
     *
     * @param dockerClient
     * @param name
     * @return
     */
    public String createNetwork(DockerClient dockerClient, String name) {
        String networkId = dockerClient.createNetworkCmd().withName(name)
                                       .withDriver("overlay")
                                       .withIpam(new Network.Ipam()
                                                     .withDriver("default"))
                                       .exec().getId();

        return networkId;
    }

    public int runContainer(String containerId, String name,
                           final PipelineLogger logger) {
        final Integer[] exitCode = {0};
        try {
            this.client.logContainerCmd(containerId)
                        .withStdErr(true)
                        .withStdOut(true)
                        .withFollowStream(true)
                        .withTailAll()
                        .exec(new LogContainerResultCallback() {
                            @Override
                            public void onNext(Frame item) {
                                logger.appendLog(
                                    name + ":" + item.toString().replace("STDOUT: ", ""));
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                logger.appendLog(throwable.getMessage());
                                exitCode[0] = -1;
                            }

                            @Override
                            public void onComplete() {
                                logger.appendLog(containerId + " finished!");
                                super.onComplete();
                            }
                        }).awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
            exitCode[0] = -1;
        }

        return exitCode[0];
    }
}
