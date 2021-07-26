package com.github.jadepeng.pipeline.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class AuthResult {
    boolean pass;
    String applyUrl;
    List<String> requiredPermissions;
}
