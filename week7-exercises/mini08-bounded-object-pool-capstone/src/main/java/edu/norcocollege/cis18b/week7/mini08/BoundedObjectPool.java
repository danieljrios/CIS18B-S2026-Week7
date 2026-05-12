package edu.norcocollege.cis18b.week7.mini08;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class BoundedObjectPool<T> {
    private final int capacity;
    private final Supplier<T> factory;

    // Resources that have been returned and are ready to be reused.
    private final ArrayDeque<T> available = new ArrayDeque<>();

    // Resources that are currently borrowed.
    // This helps prevent invalid returns or double returns.
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
        // If there are no available resources and the pool is already at capacity,
        // the borrower must wait until another thread releases a resource.
        while (available.isEmpty() && createdCount >= capacity) {
            wait();
        }

        return checkoutResource();
    }

    public synchronized T borrow(long timeoutMillis) throws InterruptedException, TimeoutException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        long remaining = timeoutMillis;

        // Wait only up to the requested timeout.
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
        // If the resource is not currently borrowed, then it should not be returned.
        // This prevents double-return and invalid-return mistakes.
        if (!inUse.remove(resource)) {
            throw new IllegalArgumentException("Resource was not borrowed from this pool.");
        }

        // Put the resource back into the available queue so it can be reused.
        available.addLast(resource);

        // Wake up any threads waiting to borrow a resource.
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

        // Reuse an available resource first.
        if (!available.isEmpty()) {
            resource = available.removeFirst();
        } else {
            // Create a new resource only if the pool has not reached capacity.
            resource = factory.get();
            createdCount++;
        }

        // Mark the resource as currently borrowed.
        inUse.add(resource);

        return resource;
    }
}