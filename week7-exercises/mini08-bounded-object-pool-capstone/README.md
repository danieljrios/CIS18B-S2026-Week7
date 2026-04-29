# Mini-Assignment 8: Bounded Object Pool Capstone

## Objective
Implement a bounded, thread-safe object pool and evaluate when the pattern is worth its cost.

## Primary Competency
Coordinating bounded resource borrowing and return safely under contention.

## Learning Objectives Alignment
Week 7 objectives 3, 5, and 8.

## Problem Statement
Build a small thread-safe pool for expensive reusable resources. The pool should enforce a maximum capacity, allow reuse of returned resources, and block or time out when the pool is exhausted.

## Requirements
Complete the code and tests so that it:

* enforces a fixed maximum capacity
* reuses returned resources safely
* blocks borrowers when the pool is exhausted
* supports a timeout-based borrow path
* passes the provided JUnit tests

## Extension Challenge
Validate returned resources before reuse or add a replacement policy for damaged resources.

## Self-Check

* Can you explain why this pattern fits external resources better than ordinary short-lived objects?
* Did you prevent double-return or invalid-return mistakes?

## Expected Output

```text
Borrowed: conn-1
Borrowed: conn-2
Returned: conn-1
Borrowed again: conn-1
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Capacity enforcement
* Safe reuse and blocking behavior
* Design tradeoff reasoning