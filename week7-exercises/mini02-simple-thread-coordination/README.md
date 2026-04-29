# Mini-Assignment 2: Simple Thread Coordination

## Objective
Create and coordinate several worker threads safely.

## Primary Competency
Using `Runnable`, thread naming, `sleep`, `join`, and interruption-aware code.

## Learning Objectives Alignment
Week 7 objective 2.

## Problem Statement
Complete a small demo where worker threads simulate staggered work and the main thread waits for completion.

## Requirements
Complete the code so that it:

* starts multiple worker threads with meaningful names
* simulates work with `Thread.sleep`
* waits for completion with `join`
* preserves interruption status when interrupted
* prints a deterministic final summary even though internal scheduling may vary

## Extension Challenge
Add clearer per-step logging and explain which lines are nondeterministic and which lines are stable.

## Self-Check

* Did you call `start()` instead of `run()` on each worker thread?
* Did the main thread wait for worker completion before printing the summary?

## Expected Output

```text
All workers launched.
All workers completed.
[grade-importer finished 3 steps, email-notifier finished 2 steps, roster-sync finished 4 steps]
```

## Run

```bash
mvn test
mvn exec:java
```

## Rubric Focus

* Correct thread lifecycle usage
* Interruption handling
* Clear coordination logic