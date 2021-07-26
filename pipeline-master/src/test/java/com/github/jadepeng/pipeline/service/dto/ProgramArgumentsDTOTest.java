package com.github.jadepeng.pipeline.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramArgumentsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramArgumentsDTO.class);
        ProgramArgumentsDTO programArgumentsDTO1 = new ProgramArgumentsDTO();
        programArgumentsDTO1.setId("id1");
        ProgramArgumentsDTO programArgumentsDTO2 = new ProgramArgumentsDTO();
        assertThat(programArgumentsDTO1).isNotEqualTo(programArgumentsDTO2);
        programArgumentsDTO2.setId(programArgumentsDTO1.getId());
        assertThat(programArgumentsDTO1).isEqualTo(programArgumentsDTO2);
        programArgumentsDTO2.setId("id2");
        assertThat(programArgumentsDTO1).isNotEqualTo(programArgumentsDTO2);
        programArgumentsDTO1.setId(null);
        assertThat(programArgumentsDTO1).isNotEqualTo(programArgumentsDTO2);
    }
}
