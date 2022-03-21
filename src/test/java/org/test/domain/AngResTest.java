package org.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.test.web.rest.TestUtil;

class AngResTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AngRes.class);
        AngRes angRes1 = new AngRes();
        angRes1.setId(1L);
        AngRes angRes2 = new AngRes();
        angRes2.setId(angRes1.getId());
        assertThat(angRes1).isEqualTo(angRes2);
        angRes2.setId(2L);
        assertThat(angRes1).isNotEqualTo(angRes2);
        angRes1.setId(null);
        assertThat(angRes1).isNotEqualTo(angRes2);
    }
}
