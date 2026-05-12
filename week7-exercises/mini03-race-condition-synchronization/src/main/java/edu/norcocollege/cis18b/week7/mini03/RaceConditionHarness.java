package edu.norcocollege.cis18b.week7.mini03;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public final class RaceConditionHarness {
    private RaceConditionHarness() {
    }

    public static int runUnsafeTrial(int threadCount, int incrementsPerThread) throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();

        // This intentionally uses the unsafe increment method to demonstrate
        // how shared mutable state can produce an incorrect result.
        runWorkers(threadCount, incrementsPerThread, counter::increment);

        return counter.getValue();
    }

    public static int runSynchronizedTrial(int threadCount, int incrementsPerThread) throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();

        // This path protects the shared counter with synchronized methods.
        runWorkers(threadCount, incrementsPerThread, counter::increment);

        return counter.getValue();
    }

    public static int runAtomicTrial(int threadCount, int incrementsPerThread) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();

        // AtomicInteger provides a thread-safe increment operation without
        // manually writing a synchronized counter class.
        runWorkers(threadCount, incrementsPerThread, counter::incrementAndGet);

        return counter.get();
    }

    private static void runWorkers(int threadCount, int incrementsPerThread, IncrementOperation operation)
            throws InterruptedException {
        // ready waits until all workers have been created and are prepared.
        CountDownLatch ready = new CountDownLatch(threadCount);

        // start releases all workers at about the same time.
        // This increases the chance of overlapping work.
        CountDownLatch start = new CountDownLatch(1);

        // done lets the main thread wait until all workers finish.
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
                    // Preserve the interruption status.
                    Thread.currentThread().interrupt();
                } finally {
                    // Always count down so the main thread is not stuck waiting forever.
                    done.countDown();
                }
            }, "counter-worker-" + index);

            worker.start();
        }

        // Wait until every worker is ready, then release them together.
        ready.await();
        start.countDown();

        // Wait until every worker finishes.
        done.await();
    }

    @FunctionalInterface
    private interface IncrementOperation {
        void increment();
    }
}