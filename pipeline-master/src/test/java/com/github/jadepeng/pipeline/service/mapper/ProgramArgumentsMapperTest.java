package com.github.jadepeng.pipeline.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgramArgumentsMapperTest {

    private ProgramArgumentsMapper programArgumentsMapper;

    @BeforeEach
    public void setUp() {
        programArgumentsMapper = new ProgramArgumentsMapperImpl();
    }
}
