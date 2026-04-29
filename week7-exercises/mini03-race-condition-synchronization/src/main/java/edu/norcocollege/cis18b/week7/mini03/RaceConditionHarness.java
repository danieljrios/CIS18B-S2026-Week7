package edu.norcocollege.cis18b.week7.mini03;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public final class RaceConditionHarness {
    private RaceConditionHarness() {
    }

    public static int runUnsafeTrial(int threadCount, int incrementsPerThread) throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();
        runWorkers(threadCount, incrementsPerThread, counter::increment);
        return counter.getValue();
    }

    public static int runSynchronizedTrial(int threadCount, int incrementsPerThread) throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        runWorkers(threadCount, incrementsPerThread, counter::increment);
        return counter.getValue();
    }

    public static int runAtomicTrial(int threadCount, int incrementsPerThread) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        runWorkers(threadCount, incrementsPerThread, counter::incrementAndGet);
        return counter.get();
    }

    private static void runWorkers(int threadCount, int incrementsPerThread, IncrementOperation operation)
            throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);

        for (int index = 0; index < threadCount; index++) {
            Thread worker = new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    for (int step = 0; step < incrementsPerThread; step++) {
                        operation.increment();
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            }, "counter-worker-" + index);
            worker.start();
        }

        ready.await();
        start.countDown();
        done.await();
    }

    @FunctionalInterface
    private interface IncrementOperation {
        void increment();
    }
}