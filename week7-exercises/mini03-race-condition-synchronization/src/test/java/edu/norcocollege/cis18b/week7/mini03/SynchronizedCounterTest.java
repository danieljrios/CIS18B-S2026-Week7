package edu.norcocollege.cis18b.week7.mini03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SynchronizedCounterTest {

    @Test
    void synchronizedCounterReachesExpectedTotal() throws InterruptedException {
        assertEquals(50_000, RaceConditionHarness.runSynchronizedTrial(5, 10_000));
    }

    @Test
    void atomicCounterReachesExpectedTotal() throws InterruptedException {
        assertEquals(50_000, RaceConditionHarness.runAtomicTrial(5, 10_000));
    }
}