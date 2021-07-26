package com.github.jadepeng.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramVersion.class);
        ProgramVersion programVersion1 = new ProgramVersion();
        programVersion1.setId("id1");
        ProgramVersion programVersion2 = new ProgramVersion();
        programVersion2.setId(programVersion1.getId());
        assertThat(programVersion1).isEqualTo(programVersion2);
        programVersion2.setId("id2");
        assertThat(programVersion1).isNotEqualTo(programVersion2);
        programVersion1.setId(null);
        assertThat(programVersion1).isNotEqualTo(programVersion2);
    }
}
