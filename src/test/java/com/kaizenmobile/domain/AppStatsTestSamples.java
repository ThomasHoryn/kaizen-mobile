package com.kaizenmobile.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppStatsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppStats getAppStatsSample1() {
        return new AppStats().id(1L).usedTenantId("usedTenantId1");
    }

    public static AppStats getAppStatsSample2() {
        return new AppStats().id(2L).usedTenantId("usedTenantId2");
    }

    public static AppStats getAppStatsRandomSampleGenerator() {
        return new AppStats().id(longCount.incrementAndGet()).usedTenantId(UUID.randomUUID().toString());
    }
}
