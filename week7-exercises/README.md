# Week 7 Exercises: Concurrency, Threads, Synchronization, Virtual Threads, and Object Pooling

This package contains 8 mini-assignments on Java concurrency basics, synchronization, coordination, high-level utilities, virtual threads, and bounded resource management.

Each assignment has its own folder, starter code, and local README instructions.

## Suggested package naming

Use the package names included in the starter files.

Pattern:

`edu.norcocollege.cis18b.week7.mini0X`

## General submission expectations

* Complete all TODOs and written reasoning prompts.
* Keep class names and package names unchanged unless your instructor says otherwise.
* Ensure each mini compiles before submission.
* Start with `mvn test` and use failures to guide your implementation.
* For runnable demos, also run `mvn exec:java` after tests pass.
* Do not rely on a particular console interleaving unless the README explicitly says the output is deterministic.

## Async pacing guidance

* Mini 1: 20 to 25 minutes
* Mini 2: 25 to 35 minutes
* Mini 3: 30 to 40 minutes
* Mini 4: 20 to 30 minutes
* Mini 5: 35 to 45 minutes
* Mini 6: 40 to 55 minutes
* Mini 7: 35 to 45 minutes
* Mini 8: 50 to 70 minutes

## Grading use

* Minis 1 through 4 are formative practice.
* Minis 5 and 6 are bridge practice with implementation focus.
* Minis 7 and 8 are summative mini-assessments.
* All minis are individual by default.

## Rubric focus for every mini

* Correctness: the program behaves as required.
* Concurrency reasoning: the student can explain why a coordination choice is safe or unsafe.
* Code quality and readability: names, structure, and control flow are clear.

## Folders

1. `mini01-process-vs-thread-clinic`
   Primary competency: process/thread distinction and concurrency vocabulary
   Learning objectives: 1

2. `mini02-simple-thread-coordination`
   Primary competency: creating and coordinating basic threads with `Runnable`, `sleep`, and `join`
   Learning objectives: 2

3. `mini03-race-condition-synchronization`
   Primary competency: identifying shared-state hazards and applying synchronization safely
   Learning objectives: 2, 3

4. `mini04-liveness-diagnosis-lab`
   Primary competency: diagnosing deadlock, starvation, and livelock symptoms
   Learning objectives: 4

5. `mini05-guarded-block-producer-consumer`
   Primary competency: coordinating threads with guarded blocks and `wait`/`notifyAll`
   Learning objectives: 3, 5

6. `mini06-executorservice-concurrent-collections`
   Primary competency: preferring high-level concurrency tools for safe aggregation
   Learning objectives: 6

7. `mini07-virtual-threads-request-simulator`
   Primary competency: using virtual threads for many blocking tasks when Java 21 is available
   Learning objectives: 7

8. `mini08-bounded-object-pool-capstone`
   Primary competency: building a bounded, thread-safe object pool and judging its tradeoffs
   Learning objectives: 3, 5, 8

## Self-check before submission

1. Did you avoid grading logic that depends on nondeterministic thread ordering?
2. Did you explain where shared mutable state exists and how it is protected?
3. Did you preserve interruption behavior where the starter code expects it?
4. If the mini includes tests, do they pass locally with `mvn test`?
5. For runnable demos, did you also run `mvn exec:java`?

## TDD workflow for this week

1. Run `mvn test` before editing code.
2. Implement one TODO or one synchronization step.
3. Run `mvn test` again.
4. Repeat until the tests pass.
5. Run `mvn exec:java` for demos or output checks.

## Java version note

* Minis 1 through 6 and Mini 8 follow the Java 17 course baseline.
* Mini 7 should be run on Java 21 to execute real virtual threads.
* If Java 21 is not available, complete the reading and reasoning prompts for Mini 7 and review the provided compatibility notes.