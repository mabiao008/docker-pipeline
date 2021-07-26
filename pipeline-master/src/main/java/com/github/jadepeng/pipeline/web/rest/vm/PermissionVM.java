package com.github.jadepeng.pipeline.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PermissionVM {
    private String operation;

    private String resourceId;

    private String appId;

    private boolean returnPermissionDetail;
}
