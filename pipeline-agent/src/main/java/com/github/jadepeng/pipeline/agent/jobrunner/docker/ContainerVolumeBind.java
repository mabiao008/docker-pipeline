package com.github.jadepeng.pipeline.agent.jobrunner.docker;

import lombok.Data;

@Data
public class ContainerVolumeBind {
    String hostPath;
    String containerPath;
    String name = "data";
    boolean readOnly = false;

    public ContainerVolumeBind(String hostPath, String containerPath) {
        this.hostPath = hostPath;
        this.containerPath = containerPath;
    }
}
