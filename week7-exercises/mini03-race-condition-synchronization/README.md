# Mini-Assignment 3: Race Condition and Synchronization

## Objective
Recognize unsafe shared state and fix it with synchronization.

## Primary Competency
Applying `synchronized` and comparing it with an atomic alternative.

## Learning Objectives Alignment
Week 7 objectives 2 and 3.

## Problem Statement
Run a shared counter across multiple threads, observe why the unsafe version is unreliable, and confirm that the synchronized and atomic versions produce correct results.

## Requirements
Complete the code and tests so that it:

* demonstrates an unsafe shared counter path
* provides a synchronized fix
* provides an atomic alternative
* keeps tests deterministic by asserting only the safe implementations
* explains why exact unsafe output should not be graded by raw value alone

## Extension Challenge
Measure the synchronized and atomic approaches over several runs and compare readability with throughput.

## Self-Check

* Can you explain why `value++` is not atomic?
* Did your synchronized path always return the expected total?

## Expected Output

```text
Expected count: 200000
Unsafe count: 183742
Synchronized count: 200000
Atomic count: 200000
```

The unsafe number may vary between runs.

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Race-condition reasoning
* Correct synchronization
* Deterministic testing choices