package com.kaizenmobile.domain;

import static com.kaizenmobile.domain.AppStatsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kaizenmobile.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppStatsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppStats.class);
        AppStats appStats1 = getAppStatsSample1();
        AppStats appStats2 = new AppStats();
        assertThat(appStats1).isNotEqualTo(appStats2);

        appStats2.setId(appStats1.getId());
        assertThat(appStats1).isEqualTo(appStats2);

        appStats2 = getAppStatsSample2();
        assertThat(appStats1).isNotEqualTo(appStats2);
    }
}
