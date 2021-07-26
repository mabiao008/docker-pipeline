package com.github.jadepeng.pipeline.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgramMapperTest {

    private ProgramMapper programMapper;

    @BeforeEach
    public void setUp() {
        programMapper = new ProgramMapperImpl();
    }
}
