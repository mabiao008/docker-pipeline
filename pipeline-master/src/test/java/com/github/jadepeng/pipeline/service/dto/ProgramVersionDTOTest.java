package com.github.jadepeng.pipeline.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramVersionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramVersionDTO.class);
        ProgramVersionDTO programVersionDTO1 = new ProgramVersionDTO();
        programVersionDTO1.setId("id1");
        ProgramVersionDTO programVersionDTO2 = new ProgramVersionDTO();
        assertThat(programVersionDTO1).isNotEqualTo(programVersionDTO2);
        programVersionDTO2.setId(programVersionDTO1.getId());
        assertThat(programVersionDTO1).isEqualTo(programVersionDTO2);
        programVersionDTO2.setId("id2");
        assertThat(programVersionDTO1).isNotEqualTo(programVersionDTO2);
        programVersionDTO1.setId(null);
        assertThat(programVersionDTO1).isNotEqualTo(programVersionDTO2);
    }
}
