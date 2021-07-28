package com.github.jadepeng.pipeline.web.rest.vm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.jadepeng.pipeline.core.bean.Pipeline;

import java.util.*;

/**
 *
 * 老版本的pipeline json
 * @author jqpeng
 */
public class PipelineJson {

    private List<Pipeline> pipeline = new ArrayList<>();

    private List<Network> networks = new ArrayList(Arrays.asList(new Network()));

    private List<Volume> volumes = new ArrayList(Arrays.asList(new Volume()));

    /**
     * 开始运行节点
     */
    private String startNode;

    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public List<Pipeline> getPipeline() {
        return pipeline;
    }

    public void setPipeline(List<Pipeline> pipeline) {
        this.pipeline = pipeline;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public static class Pipeline {
        String name;
        String alias;
        List<PipelineStep> steps = new ArrayList<>();

        /**
         * 依赖的pipelines
         */
        List<String> dependencies = new ArrayList<>();

        /**
         * 下一个pipeline
         */
        List<String> nextPipelines = new ArrayList<>();

        public List<String> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<String> dependencies) {
            this.dependencies = dependencies;
        }

        public List<String> getNextPipelines() {
            return nextPipelines;
        }

        public void setNextPipelines(List<String> nextPipelines) {
            this.nextPipelines = nextPipelines;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public List<PipelineStep> getSteps() {
            return steps;
        }

        public void setSteps(List<PipelineStep> steps) {
            this.steps = steps;
        }
    }

    public com.github.jadepeng.pipeline.core.bean.Pipeline toPipeline() {
        JSONObject json = (JSONObject) JSON.toJSON(this);
        return json.toJavaObject(com.github.jadepeng.pipeline.core.bean.Pipeline.class);
    }

    public static class Network {
        String name = "pipeline_default";
        String driver = "bridge";


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }
    }

    public static class Volume {
        String name = "pipeline_default";
        String driver = "local";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }
    }

    public static class StepNetwork {
        private String name;
        private List<String> aliases = new ArrayList(Arrays.asList("default"));

        public StepNetwork() {}

        public StepNetwork(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAliases() {
            return aliases;
        }

        public void setAliases(List<String> aliases) {
            this.aliases = aliases;
        }
    }

    public static class PipelineStep {
        private String name;
        private String alias;
        private String image;
        private String working_dir = "/workspace";
        private JSONObject environment = new JSONObject();
        private List<String> entrypoint = new ArrayList(Arrays.asList("/bin/bash", "-c"));
        private List<String> command = new ArrayList(Arrays.asList("echo $CI_SCRIPT | base64 -d | /bin/bash -e"));
        private List<String> volumes = new ArrayList(Arrays.asList("pipeline_default:/aimind"));

        private List<StepNetwork> networks = Arrays.asList(new StepNetwork("pipeline_default"));
        private boolean on_success = true;
        private JSONObject auth_config = new JSONObject();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getWorking_dir() {
            return working_dir;
        }

        public void setWorking_dir(String working_dir) {
            this.working_dir = working_dir;
        }

        public JSONObject getEnvironment() {
            return environment;
        }

        public void setEnvironment(JSONObject environment) {
            this.environment = environment;
        }

        public List<String> getEntrypoint() {
            return entrypoint;
        }

        public void setEntrypoint(List<String> entrypoint) {
            this.entrypoint = entrypoint;
        }

        public List<String> getCommand() {
            return command;
        }

        public void setCommand(List<String> command) {
            this.command = command;
        }

        public List<String> getVolumes() {
            return volumes;
        }

        public void setVolumes(List<String> volumes) {
            this.volumes = volumes;
        }

        public List<StepNetwork> getNetworks() {
            return networks;
        }

        public void setNetworks(List<StepNetwork> networks) {
            this.networks = networks;
        }

        public boolean isOn_success() {
            return on_success;
        }

        public void setOn_success(boolean on_success) {
            this.on_success = on_success;
        }

        public JSONObject getAuth_config() {
            return auth_config;
        }

        public void setAuth_config(JSONObject auth_config) {
            this.auth_config = auth_config;
        }

        public String buildCmd(List<String> commands) {

            String finalCmd = "\n" +
                    "if [ -n \"$CI_NETRC_MACHINE\" ]; then\n" +
                    "cat <<EOF > $HOME/.netrc\n" +
                    "machine $CI_NETRC_MACHINE\n" +
                    "login $CI_NETRC_USERNAME\n" +
                    "password $CI_NETRC_PASSWORD\n" +
                    "EOF\n" +
                    "chmod 0600 $HOME/.netrc\n" +
                    "fi\n" +
                    "unset CI_NETRC_USERNAME\n" +
                    "unset CI_NETRC_PASSWORD\n" +
                    "unset CI_SCRIPT";
            List<String> cmds = new ArrayList<>();
            cmds.add(finalCmd);
            for (String line : commands) {
                // 确保echo可以输出
                cmds.add("echo + " + line.replaceAll("\\'|\\(|\\?|\\{|\\}|\\)|\\<|\\>|\\$", "\\\\$0"));
                cmds.add(line);
                cmds.add("");
            }
            finalCmd = String.join("\n", cmds);
            return Base64.getEncoder().encodeToString(finalCmd.getBytes());
        }
    }
}
