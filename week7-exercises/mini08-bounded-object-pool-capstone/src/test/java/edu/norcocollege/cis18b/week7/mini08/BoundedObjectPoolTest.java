package edu.norcocollege.cis18b.week7.mini08;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class BoundedObjectPoolTest {

    @Test
    void reusesReturnedResources() throws Exception {
        AtomicInteger sequence = new AtomicInteger(1);
        BoundedObjectPool<MockConnection> pool = new BoundedObjectPool<>(1,
            () -> new MockConnection("conn-" + sequence.getAndIncrement()));

        MockConnection first = pool.borrow();
        pool.release(first);
        MockConnection second = pool.borrow();

        assertSame(first, second);
        assertEquals(1, pool.totalCreated());
    }

    @Test
    void blocksBorrowUntilResourceReturns() throws Exception {
        AtomicInteger sequence = new AtomicInteger(1);
        BoundedObjectPool<MockConnection> pool = new BoundedObjectPool<>(1,
            () -> new MockConnection("conn-" + sequence.getAndIncrement()));

        MockConnection first = pool.borrow();
        CompletableFuture<MockConnection> waitingBorrow = CompletableFuture.supplyAsync(() -> borrowUnchecked(pool));

        assertThrows(TimeoutException.class, () -> waitingBorrow.get(100, TimeUnit.MILLISECONDS));

        pool.release(first);
        MockConnection reused = waitingBorrow.get(1, TimeUnit.SECONDS);
        assertSame(first, reused);
    }

    @Test
    void timeoutBorrowFailsWhenPoolStaysExhausted() throws Exception {
        AtomicInteger sequence = new AtomicInteger(1);
        BoundedObjectPool<MockConnection> pool = new BoundedObjectPool<>(1,
            () -> new MockConnection("conn-" + sequence.getAndIncrement()));

        pool.borrow();

        assertThrows(TimeoutException.class, () -> pool.borrow(100));
    }

    private static MockConnection borrowUnchecked(BoundedObjectPool<MockConnection> pool) {
        try {
            return pool.borrow();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }
    }
}