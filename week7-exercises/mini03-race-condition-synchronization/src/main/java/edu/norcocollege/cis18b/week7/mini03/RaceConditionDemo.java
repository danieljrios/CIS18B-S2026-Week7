package edu.norcocollege.cis18b.week7.mini03;

public class RaceConditionDemo {

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 8;
        int incrementsPerThread = 25_000;
        int expected = threadCount * incrementsPerThread;

        int unsafe = RaceConditionHarness.runUnsafeTrial(threadCount, incrementsPerThread);
        int safe = RaceConditionHarness.runSynchronizedTrial(threadCount, incrementsPerThread);
        int atomic = RaceConditionHarness.runAtomicTrial(threadCount, incrementsPerThread);

        System.out.println("Expected count: " + expected);
        System.out.println("Unsafe count: " + unsafe);
        System.out.println("Synchronized count: " + safe);
        System.out.println("Atomic count: " + atomic);
    }
}