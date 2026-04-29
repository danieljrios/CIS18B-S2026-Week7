package edu.norcocollege.cis18b.week7.mini08;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundedObjectPoolDemo {

    public static void main(String[] args) throws Exception {
        AtomicInteger sequence = new AtomicInteger(1);
        BoundedObjectPool<MockConnection> pool = new BoundedObjectPool<>(2,
            () -> new MockConnection("conn-" + sequence.getAndIncrement()));

        MockConnection first = pool.borrow();
        MockConnection second = pool.borrow();

        System.out.println("Borrowed: " + first.id());
        System.out.println("Borrowed: " + second.id());

        pool.release(first);
        System.out.println("Returned: " + first.id());

        MockConnection reused = pool.borrow();
        System.out.println("Borrowed again: " + reused.id());
    }
}