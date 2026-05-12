package edu.norcocollege.cis18b.week7.mini03;

public class UnsafeCounter {
    private int value;

    public void increment() {
        // value++ is not atomic.
        // It reads the current value, adds 1, and writes the result back.
        // If multiple threads overlap these steps, updates can be lost.
        value++;
    }

    public int getValue() {
        return value;
    }
}

// Written response:
/*
Explanation:

The unsafe counter should not be graded by expecting one exact incorrect value 
because the result depends on thread scheduling. The operation value++ isn't really atomic.
it is really doing a read, an add, and a write, so When multiple threads perform those steps at 
same time, one thread can overwrite another thread's update. Because the timing can 
change each run, the unsafe count may be different every time.

The synchronized counter and atomic counter are safe to test by exact value because
they protect the increment operation. The synchronized version allows only one thread
at a time to update the value, while AtomicInteger provides a built-in thread-safe
increment operation.
*/