package edu.norcocollege.cis18b.week7.mini03;

public class SynchronizedCounter {
    private int value;

    public synchronized void increment() {
        // synchronized makes only one thread enter this method at a time.
        // This protects the read-add-write sequence in value++.
        value++;
    }

    public synchronized int getValue() {
        // synchronized also helps make sure the final value is safely visible.
        return value;
    }
}