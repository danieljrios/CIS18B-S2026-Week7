# Mini-Assignment 4: Liveness Diagnosis Lab

## Objective
Diagnose deadlock, starvation, and livelock symptoms from small examples.

## Primary Competency
Recognizing liveness hazards and proposing practical fixes.

## Learning Objectives Alignment
Week 7 objective 4.

## Problem Statement
Inspect the provided scenarios, classify each liveness issue, and explain how you would reduce the risk.

## Requirements
Complete the code and written analysis so that it:

* identifies each scenario as deadlock, starvation, or livelock
* proposes one realistic mitigation
* keeps output deterministic
* compiles and runs cleanly

## Extension Challenge
Refactor one deadlock-prone scenario to use a consistent lock ordering rule.

## Self-Check

* Can you explain why a busy but unproductive program may be livelocked instead of deadlocked?
* Did each mitigation actually address the cause, not just the symptom?

## Expected Output

```text
lock-order-conflict -> DEADLOCK
always-last-in-line -> STARVATION
over-polite-retry-loop -> LIVELOCK
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Diagnosis accuracy
* Quality of proposed fix
* Debugging clarity