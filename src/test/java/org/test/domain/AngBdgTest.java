package org.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.test.web.rest.TestUtil;

class AngBdgTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AngBdg.class);
        AngBdg angBdg1 = new AngBdg();
        angBdg1.setId(1L);
        AngBdg angBdg2 = new AngBdg();
        angBdg2.setId(angBdg1.getId());
        assertThat(angBdg1).isEqualTo(angBdg2);
        angBdg2.setId(2L);
        assertThat(angBdg1).isNotEqualTo(angBdg2);
        angBdg1.setId(null);
        assertThat(angBdg1).isNotEqualTo(angBdg2);
    }
}
