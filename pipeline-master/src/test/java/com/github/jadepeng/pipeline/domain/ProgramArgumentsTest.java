package com.github.jadepeng.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramArgumentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramArguments.class);
        ProgramArguments programArguments1 = new ProgramArguments();
        programArguments1.setId("id1");
        ProgramArguments programArguments2 = new ProgramArguments();
        programArguments2.setId(programArguments1.getId());
        assertThat(programArguments1).isEqualTo(programArguments2);
        programArguments2.setId("id2");
        assertThat(programArguments1).isNotEqualTo(programArguments2);
        programArguments1.setId(null);
        assertThat(programArguments1).isNotEqualTo(programArguments2);
    }
}
