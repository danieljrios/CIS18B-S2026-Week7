# Mini-Assignment 1: Process vs Thread Clinic

## Objective
Classify common scenarios using the right concurrency vocabulary.

## Primary Competency
Choosing the correct abstraction conceptually: process, thread, or not meaningfully concurrent.

## Learning Objectives Alignment
Week 7 objective 1.

## Problem Statement
Review the starter scenarios and classify whether each one calls for process-based isolation, thread-based concurrency, or no meaningful concurrency at all.

## Requirements
Complete the code and written responses so that it:

* keeps each scenario mapped to one classification
* explains why shared memory is a benefit or a risk where relevant
* preserves deterministic console output
* compiles with `mvn test` and runs with `mvn exec:java`

## Extension Challenge
Add one scenario from a real application you use and justify its classification.

## Self-Check

* Can you explain why process isolation is stronger than thread isolation?
* Did you distinguish "can overlap" from "must run in parallel"?

## Expected Output

```text
student-code-runner -> PROCESS
gradebook-auto-save -> THREAD
sort-single-list-once -> NOT_MEANINGFULLY_CONCURRENT
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Vocabulary accuracy
* Quality of reasoning
* Clarity of explanation