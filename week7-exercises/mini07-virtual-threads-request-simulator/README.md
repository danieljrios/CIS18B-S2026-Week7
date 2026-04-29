# Mini-Assignment 7: Virtual Threads Request Simulator

## Objective
Use virtual threads for many blocking tasks when Java 21 is available.

## Primary Competency
Recognizing when virtual threads improve scalability for blocking workloads.

## Learning Objectives Alignment
Week 7 objective 7.

## Problem Statement
Simulate many slow request handlers and collect their completions. If Java 21 is not available, explain what the simulator would do and why virtual threads help this workload.

## Requirements
Complete the code and tests so that it:

* detects whether virtual threads are available
* runs many blocking tasks through virtual threads when Java 21 is present
* returns a deterministic sorted completion summary
* prints a clear compatibility message when Java 21 is not available
* explains why thread safety still matters even with virtual threads

## Extension Challenge
Compare this simulator with a small fixed thread pool and discuss the throughput and resource tradeoffs.

## Self-Check

* Does your code fail clearly instead of silently pretending to use virtual threads?
* Can you explain why virtual threads help blocking work but do not fix unsafe shared state?

## Expected Output

Java 21 path:

```text
Virtual thread support available.
[request-1 complete, request-2 complete, request-3 complete, request-4 complete, request-5 complete]
```

Java 17 path:

```text
Virtual threads require Java 21 or newer.
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Correct virtual-thread reasoning
* Compatibility handling
* Deterministic reporting