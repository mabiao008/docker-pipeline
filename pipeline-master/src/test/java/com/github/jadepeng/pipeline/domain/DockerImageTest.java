package com.github.jadepeng.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DockerImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DockerImage.class);
        DockerImage dockerImage1 = new DockerImage();
        dockerImage1.setId("id1");
        DockerImage dockerImage2 = new DockerImage();
        dockerImage2.setId(dockerImage1.getId());
        assertThat(dockerImage1).isEqualTo(dockerImage2);
        dockerImage2.setId("id2");
        assertThat(dockerImage1).isNotEqualTo(dockerImage2);
        dockerImage1.setId(null);
        assertThat(dockerImage1).isNotEqualTo(dockerImage2);
    }
}
