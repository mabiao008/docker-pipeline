package com.github.jadepeng.pipeline.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DockerImageMapperTest {

    private DockerImageMapper dockerImageMapper;

    @BeforeEach
    public void setUp() {
        dockerImageMapper = new DockerImageMapperImpl();
    }
}
