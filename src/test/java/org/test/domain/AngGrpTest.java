package org.test.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.test.web.rest.TestUtil;

class AngGrpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AngGrp.class);
        AngGrp angGrp1 = new AngGrp();
        angGrp1.setId(1L);
        AngGrp angGrp2 = new AngGrp();
        angGrp2.setId(angGrp1.getId());
        assertThat(angGrp1).isEqualTo(angGrp2);
        angGrp2.setId(2L);
        assertThat(angGrp1).isNotEqualTo(angGrp2);
        angGrp1.setId(null);
        assertThat(angGrp1).isNotEqualTo(angGrp2);
    }
}
