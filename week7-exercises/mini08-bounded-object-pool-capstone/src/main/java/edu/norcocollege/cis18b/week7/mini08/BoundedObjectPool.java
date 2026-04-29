package edu.norcocollege.cis18b.week7.mini08;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class BoundedObjectPool<T> {
    private final int capacity;
    private final Supplier<T> factory;
    private final ArrayDeque<T> available = new ArrayDeque<>();
    private final Set<T> inUse = new HashSet<>();
    private int createdCount;

    public BoundedObjectPool(int capacity, Supplier<T> factory) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.factory = factory;
    }

    public synchronized T borrow() throws InterruptedException {
        while (available.isEmpty() && createdCount >= capacity) {
            wait();
        }
        return checkoutResource();
    }

    public synchronized T borrow(long timeoutMillis) throws InterruptedException, TimeoutException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        long remaining = timeoutMillis;

        while (available.isEmpty() && createdCount >= capacity) {
            if (remaining <= 0) {
                throw new TimeoutException("Timed out waiting for a pooled resource.");
            }
            wait(remaining);
            remaining = deadline - System.currentTimeMillis();
        }

        return checkoutResource();
    }

    public synchronized void release(T resource) {
        if (!inUse.remove(resource)) {
            throw new IllegalArgumentException("Resource was not borrowed from this pool.");
        }
        available.addLast(resource);
        notifyAll();
    }

    public synchronized int totalCreated() {
        return createdCount;
    }

    public synchronized int availableCount() {
        return available.size();
    }

    public synchronized int inUseCount() {
        return inUse.size();
    }

    private T checkoutResource() {
        T resource;
        if (!available.isEmpty()) {
            resource = available.removeFirst();
        } else {
            resource = factory.get();
            createdCount++;
        }
        inUse.add(resource);
        return resource;
    }
}