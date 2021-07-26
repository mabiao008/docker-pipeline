package com.github.jadepeng.pipeline.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DockerImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DockerImageDTO.class);
        DockerImageDTO dockerImageDTO1 = new DockerImageDTO();
        dockerImageDTO1.setId("id1");
        DockerImageDTO dockerImageDTO2 = new DockerImageDTO();
        assertThat(dockerImageDTO1).isNotEqualTo(dockerImageDTO2);
        dockerImageDTO2.setId(dockerImageDTO1.getId());
        assertThat(dockerImageDTO1).isEqualTo(dockerImageDTO2);
        dockerImageDTO2.setId("id2");
        assertThat(dockerImageDTO1).isNotEqualTo(dockerImageDTO2);
        dockerImageDTO1.setId(null);
        assertThat(dockerImageDTO1).isNotEqualTo(dockerImageDTO2);
    }
}
