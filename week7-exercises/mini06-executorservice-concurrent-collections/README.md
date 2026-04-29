# Mini-Assignment 6: ExecutorService and Concurrent Collections

## Objective
Use higher-level concurrency tools to aggregate work safely.

## Primary Competency
Submitting tasks to an executor and updating shared results through concurrent collections and atomic values.

## Learning Objectives Alignment
Week 7 objective 6.

## Problem Statement
Process several batches of words concurrently and build a shared count summary without manual locking around the whole program.

## Requirements
Complete the code and tests so that it:

* uses `ExecutorService` with a fixed-size thread pool
* uses `Callable` and `Future`
* stores shared counts in a concurrent collection
* uses atomic values for safe increments
* returns a deterministic sorted summary for reporting

## Extension Challenge
Replace one part of the implementation with a different high-level primitive and compare the tradeoffs.

## Self-Check

* Did you shut down the executor after use?
* Did you avoid manual coarse-grained locking around the whole counting process?

## Expected Output

```text
Processed tokens: 8
alert -> 3
info -> 3
warning -> 2
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Appropriate high-level tool choice
* Correct aggregation
* Deterministic reporting