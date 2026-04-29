# Week 7 Lesson: Concurrency, Threads, Synchronization, Virtual Threads, and Object Pooling

# How to use this README.md

This repository contains the lesson for this week (`README.md`, the file you're reading now), as well as your assignments for this week (`week7-exercises/`). Each exercise has its own `README.md` with setup, requirements, tests, and run instructions.

Work through the lesson in order, run the small examples locally, and pause at each checkpoint before moving on. This week builds from conceptual concurrency into hands-on coordination, synchronization, higher-level utilities, and design tradeoffs.

Java note:

* Most of Week 7 is written to fit the repository's Java 17 baseline.
* Mini 7 uses virtual threads and should be run on Java 21 when available.

## Required External Readings

1. Concurrency overview:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)
2. Processes and Threads:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/procthread.html)
3. Threads overview:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/threads.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/threads.html)
4. SimpleThreads example:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html)
5. Synchronization:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html)
6. Liveness:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/liveness.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/liveness.html)
7. Guarded Blocks:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html)
8. Immutable Objects:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/immutable.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/immutable.html)
9. High-Level Concurrency Objects:
   [https://docs.oracle.com/javase/tutorial/essential/concurrency/highlevel.html](https://docs.oracle.com/javase/tutorial/essential/concurrency/highlevel.html)
10. Virtual Threads:
   [https://dev.java/learn/new-features/virtual-threads/](https://dev.java/learn/new-features/virtual-threads/)
11. Object Pool pattern:
   [https://sourcemaking.com/design_patterns/object_pool](https://sourcemaking.com/design_patterns/object_pool)

## Learning Objectives

By the end of this lesson, you will be able to:

* explain the difference between concurrency, parallelism, processes, and threads
* create and coordinate Java threads using `Runnable`, `sleep`, `join`, and interruption-aware code
* identify race conditions and apply synchronization to protect shared mutable state
* explain deadlock, starvation, and livelock at a practical debugging level
* use guarded blocks and immutability to improve thread-safe program design
* apply high-level concurrency utilities such as `ExecutorService`, `Future`, concurrent collections, and atomic classes
* describe when virtual threads are appropriate and how they differ from platform threads
* evaluate the Object Pool pattern in terms of resource cost, contention, and design tradeoffs

---

# Part 1 - Concurrency Overview

Estimated time: 20 to 30 minutes

## Reading Focus

Read the Oracle concurrency overview and focus on:

* what it means for multiple tasks to make progress during overlapping time
* how concurrency differs from raw processor parallelism
* why modern applications often need multiple flows of work even on one machine

## Concurrency vs Parallelism

Concurrency means your program is structured so multiple tasks can be in progress during the same overall time window.

Parallelism means multiple tasks are literally executing at the same instant on different cores or processors.

You can have concurrency without parallel hardware. A single machine may switch attention between tasks so that each one makes progress.

Plain-language rule:

* concurrency is about coordinating tasks
* parallelism is about running tasks at the same time

### Example

Imagine a campus support app that needs to:

* save attendance updates
* send an email notification

Those tasks are independent. Even if one CPU core handles them by switching back and forth, the program still benefits from a concurrent structure because the tasks do not need to block each other conceptually.

```java
Runnable saveAttendance = () -> System.out.println("Saving attendance...");
Runnable sendEmail = () -> System.out.println("Sending absence alert...");

saveAttendance.run();
sendEmail.run();
```

The snippet above is not actually concurrent yet, but it shows two separate units of work. Concurrency begins when we coordinate those tasks so they can overlap in time or be scheduled independently.

Checkpoint:

* Why can a single-core machine still benefit from a concurrent program structure?
* What is the difference between "tasks that can overlap" and "tasks that definitely run at the same instant"?

---

# Part 2 - Processes vs Threads

Estimated time: 20 to 30 minutes

## Reading Focus

Read Oracle's Processes and Threads page and focus on:

* process isolation
* thread sharing within one process
* communication and cost tradeoffs

The reading should remain your primary reference. Video reinforcement can help, but it does not replace the reading.

## Practical Comparison

Processes are separate running programs with stronger isolation. Threads are smaller units of execution inside one process and usually share the same heap.

That shared memory makes thread communication faster and more convenient, but it also creates risk because multiple threads can touch the same mutable data.

| Behavior | Process | Thread |
| --- | --- | --- |
| Memory model | Separate address space | Shared heap within one process |
| Creation cost | Higher | Lower |
| Failure isolation | Stronger | Weaker |
| Communication | Slower, explicit IPC | Faster, shared memory |
| Coordination risk | Lower shared-state risk | Higher shared-state risk |

### Example reasoning

If a browser opens separate tabs in separate processes, a crash in one process is less likely to corrupt the others.

If a web server uses multiple threads in one process, requests can share caches and objects more easily, but the program must protect that shared state correctly.

Checkpoint:

* Why does shared heap memory make threads cheaper to coordinate and riskier to design?
* When would stronger process isolation be worth the extra cost?

---

# Part 3 - Thread Basics

Estimated time: 30 to 40 minutes

## Reading Focus

Read the Threads overview and the SimpleThreads example. Concentrate on:

* `Thread` and `Runnable`
* starting work with `start()` instead of calling `run()` directly
* `sleep`, `join`, and interruption
* basic lifecycle ideas such as new, runnable, waiting, and terminated

## Core Ideas

`Runnable` describes the work. `Thread` provides a separate path of execution for that work.

Useful beginner rules:

* call `start()` to create concurrent execution
* use `join()` when one thread must wait for another to finish
* treat `InterruptedException` as a signal that your code should stop or clean up
* never assume execution order unless you explicitly coordinate it

### Repository-style SimpleThreads adaptation

```java
public class SimpleThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        Runnable worker = () -> {
            for (int step = 1; step <= 3; step++) {
                System.out.println(Thread.currentThread().getName() + " step " + step);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };

        Thread reportThread = new Thread(worker, "report-worker");
        reportThread.start();
        reportThread.join();
        System.out.println("Main thread observed completion.");
    }
}
```

Important warning:

* execution order is not guaranteed unless you enforce it with coordination tools like `join`, latches, or queues

Checkpoint:

* What changes when you call `run()` directly instead of `start()`?
* Modify the delay or loop count in the example. What behavior changes, and what behavior stays conceptually the same?

---

# Part 4 - Synchronization and Shared State

Estimated time: 30 to 40 minutes

## Reading Focus

Read Oracle's Synchronization material and focus on:

* race conditions
* synchronized methods and blocks
* intrinsic locks
* atomicity and visibility at a beginner-friendly level

## Race Conditions

A race condition happens when correct behavior depends on timing that your program does not control.

Shared mutable state is the usual cause.

```java
class Counter {
    private int value;

    public void incrementUnsafe() {
        value++;
    }

    public synchronized void incrementSafe() {
        value++;
    }

    public int getValue() {
        return value;
    }
}
```

Why `value++` is risky without coordination:

* read current value
* compute next value
* write the result back

If two threads overlap those steps, one update can be lost.

## What synchronization solves

Synchronization makes a critical section exclusive so that one thread finishes protected work before another thread enters the same protected region.

## What synchronization costs

Synchronization is not free.

It can add:

* waiting
* reduced throughput under heavy contention
* higher design complexity if locks are nested carelessly

Checkpoint:

* What bug does synchronization prevent in the counter example?
* Why should you lock only the state that actually needs protection?

---

# Part 5 - Liveness, Guarded Blocks, and Immutability

Estimated time: 35 to 45 minutes

## Reading Focus

Read the Oracle pages on Liveness, Guarded Blocks, and Immutable Objects. Focus on:

* deadlock, starvation, and livelock
* waiting for a condition safely with `wait` and `notifyAll`
* why immutable data simplifies sharing

## Liveness Problems

### Deadlock

Two threads each hold one lock and wait forever for the other lock.

### Starvation

One thread rarely or never gets the resources it needs because other work always wins.

### Livelock

Threads keep reacting to each other, but no useful progress happens.

### Small deadlock example

```java
final Object lockA = new Object();
final Object lockB = new Object();

Thread first = new Thread(() -> {
    synchronized (lockA) {
        synchronized (lockB) {
            System.out.println("first finished");
        }
    }
});

Thread second = new Thread(() -> {
    synchronized (lockB) {
        synchronized (lockA) {
            System.out.println("second finished");
        }
    }
});
```

The safest beginner fix is usually consistent lock ordering.

## Guarded Blocks

Guarded blocks say: wait until a condition becomes true, then proceed.

```java
class MessageBuffer {
    private String message;
    private boolean empty = true;

    public synchronized void put(String next) throws InterruptedException {
        while (!empty) {
            wait();
        }
        message = next;
        empty = false;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (empty) {
            wait();
        }
        String result = message;
        empty = true;
        notifyAll();
        return result;
    }
}
```

Use `while`, not `if`, because the condition must be checked again after waking.

## Why immutability helps

Immutable objects do not change after construction. That means threads can share them without coordinating every read.

Good beginner examples:

* records with final state
* configuration objects built once and then shared
* immutable value types stored in concurrent collections

Checkpoint:

* Which liveness problem is caused by inconsistent lock ordering?
* Why is an immutable object usually easier to share than a mutable object?

---

# Part 6 - High-Level Concurrency Utilities

Estimated time: 30 to 40 minutes

## Reading Focus

Read High-Level Concurrency Objects and focus on:

* `ExecutorService`
* `Callable`
* `Future`
* concurrent collections
* atomic classes

Beginner rule:

* prefer higher-level concurrency tools when they express the workload clearly
* use manual threads directly when the lesson goal is learning thread mechanics

### Fixed thread pool example

```java
ExecutorService pool = Executors.newFixedThreadPool(3);

List<Callable<String>> tasks = List.of(
    () -> "graded quiz set A",
    () -> "graded quiz set B",
    () -> "graded quiz set C"
);

for (Future<String> future : pool.invokeAll(tasks)) {
    System.out.println(future.get());
}

pool.shutdown();
```

### Concurrent map counting example

```java
ConcurrentHashMap<String, AtomicInteger> counts = new ConcurrentHashMap<>();

counts.computeIfAbsent("warning", key -> new AtomicInteger())
      .incrementAndGet();
```

These tools let you focus on task structure and safe shared updates without manually managing every low-level step.

Checkpoint:

* Why is an executor often clearer than creating many raw `Thread` objects yourself?
* When is `AtomicInteger` simpler than wrapping a whole class in `synchronized`?

---

# Part 7 - Virtual Threads

Estimated time: 20 to 30 minutes

## Reading Focus

Read the dev.java virtual threads overview and focus on:

* what problem virtual threads solve
* why blocking code can scale better with them
* why they do not remove the need for thread safety

## Practical framing

Platform threads map more directly to operating-system threads and are relatively expensive.

Virtual threads are much lighter. They are designed for workloads with many mostly-blocking tasks, such as handling many independent requests.

### Example shape

```java
try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
    for (int requestId = 1; requestId <= 1_000; requestId++) {
        final int id = requestId;
        executor.submit(() -> {
            Thread.sleep(100);
            System.out.println("Handled request " + id);
            return null;
        });
    }
}
```

Practical caution:

* virtual threads help scalability for blocking workloads
* they do not make race conditions, deadlocks, or unsafe shared mutation disappear

Checkpoint:

* Why are virtual threads especially useful for many blocking tasks?
* Why would a shared mutable counter still need protection even if the work runs in virtual threads?

---

# Part 8 - Object Pool Design Pattern

Estimated time: 20 to 30 minutes

## Reading Focus

Read the Object Pool pattern reference and focus on:

* borrowing and returning expensive resources
* bounded capacity
* tradeoffs between reuse and added complexity

## Intent

An object pool manages a limited set of reusable resources.

Classic examples:

* database connections
* parser workers tied to native resources
* rate-limited external service clients

## When it helps

Object pools make sense when the resource is:

* expensive to create
* externally constrained
* unsafe or wasteful to create without limits

## When it hurts

Pooling ordinary short-lived Java objects is often a bad tradeoff. Modern JVM allocation is usually cheap, and a pool can add:

* contention
* stale object state bugs
* lifecycle complexity

That is why you should evaluate the resource cost first instead of assuming "reuse is always better."

Checkpoint:

* Why is pooling a database connection more reasonable than pooling a plain `StringBuilder` in most beginner programs?
* What new correctness problems can a poorly designed object pool introduce?

---

# Week 7 Wrap-Up

This week moves from conceptual concurrency into practical correctness.

Sequence to remember:

1. Understand concurrent work conceptually.
2. Observe thread lifecycle and scheduling behavior.
3. Protect shared mutable state.
4. Recognize liveness risks and guarded coordination.
5. Prefer higher-level tools when they better express the workload.
6. Treat virtual threads as a scalability tool, not a correctness shortcut.
7. Evaluate design patterns like Object Pool by tradeoff, not novelty.

Next step:

* complete the mini-assignments in `week7-exercises/`
* start each mini with `mvn test`
* run `mvn exec:java` where the README asks for a demo run