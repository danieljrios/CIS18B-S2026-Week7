# Mini-Assignment 5: Guarded Producer-Consumer Buffer

## Objective
Coordinate producer and consumer threads with a guarded block.

## Primary Competency
Using `wait` and `notifyAll` correctly around a shared condition.

## Learning Objectives Alignment
Week 7 objectives 3 and 5.

## Problem Statement
Complete a one-slot message buffer so producers block when the slot is full and consumers block when the slot is empty.

## Requirements
Complete the code and tests so that it:

* uses `synchronized`, `wait`, and `notifyAll`
* checks conditions with `while`, not `if`
* preserves message order for the one-slot workflow
* wakes blocked threads correctly after state changes
* passes the provided JUnit tests

## Extension Challenge
Generalize the one-slot buffer into a small bounded queue that preserves FIFO ordering.

## Self-Check

* Did you re-check the guard condition after waking?
* Can you explain why `notifyAll` is safer than `notify` in beginner coordination code?

## Expected Output

```text
Produced: alpha
Consumed: alpha
Produced: beta
Consumed: beta
Produced: gamma
Consumed: gamma
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Guarded-block correctness
* Blocking and wake-up behavior
* Test reliability