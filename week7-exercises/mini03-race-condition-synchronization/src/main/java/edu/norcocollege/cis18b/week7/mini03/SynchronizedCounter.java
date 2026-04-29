package edu.norcocollege.cis18b.week7.mini03;

public class SynchronizedCounter {
    private int value;

    public synchronized void increment() {
        value++;
    }

    public synchronized int getValue() {
        return value;
    }
}