package com.github.jadepeng.pipeline.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramDTO.class);
        ProgramDTO programDTO1 = new ProgramDTO();
        programDTO1.setId("id1");
        ProgramDTO programDTO2 = new ProgramDTO();
        assertThat(programDTO1).isNotEqualTo(programDTO2);
        programDTO2.setId(programDTO1.getId());
        assertThat(programDTO1).isEqualTo(programDTO2);
        programDTO2.setId("id2");
        assertThat(programDTO1).isNotEqualTo(programDTO2);
        programDTO1.setId(null);
        assertThat(programDTO1).isNotEqualTo(programDTO2);
    }
}
