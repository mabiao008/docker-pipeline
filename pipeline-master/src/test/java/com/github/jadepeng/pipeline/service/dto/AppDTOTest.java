package com.github.jadepeng.pipeline.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppDTO.class);
        AppDTO appDTO1 = new AppDTO();
        appDTO1.setId("id1");
        AppDTO appDTO2 = new AppDTO();
        assertThat(appDTO1).isNotEqualTo(appDTO2);
        appDTO2.setId(appDTO1.getId());
        assertThat(appDTO1).isEqualTo(appDTO2);
        appDTO2.setId("id2");
        assertThat(appDTO1).isNotEqualTo(appDTO2);
        appDTO1.setId(null);
        assertThat(appDTO1).isNotEqualTo(appDTO2);
    }
}
