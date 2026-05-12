package edu.norcocollege.cis18b.week7.mini08;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundedObjectPoolDemo {

    public static void main(String[] args) throws Exception {
        AtomicInteger sequence = new AtomicInteger(1);

        // Create a pool that can make at most 2 MockConnection objects.
        BoundedObjectPool<MockConnection> pool = new BoundedObjectPool<>(2,
            () -> new MockConnection("conn-" + sequence.getAndIncrement()));

        MockConnection first = pool.borrow();
        MockConnection second = pool.borrow();

        System.out.println("Borrowed: " + first.id());
        System.out.println("Borrowed: " + second.id());

        pool.release(first);
        System.out.println("Returned: " + first.id());

        // This should reuse conn-1 instead of creating conn-3.
        MockConnection reused = pool.borrow();
        System.out.println("Borrowed again: " + reused.id());
    }
}