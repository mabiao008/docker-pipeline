package com.github.jadepeng.pipeline.agent.jobrunner.docker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContainerPortBind {
    int hostPort;
    int containerPort;
}
