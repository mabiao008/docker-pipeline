package com.github.jadepeng.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.jadepeng.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(App.class);
        App app1 = new App();
        app1.setId("id1");
        App app2 = new App();
        app2.setId(app1.getId());
        assertThat(app1).isEqualTo(app2);
        app2.setId("id2");
        assertThat(app1).isNotEqualTo(app2);
        app1.setId(null);
        assertThat(app1).isNotEqualTo(app2);
    }
}
