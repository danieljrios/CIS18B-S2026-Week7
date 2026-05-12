package edu.norcocollege.cis18b.week7.mini04;

public class LivenessScenario {
    private final String name;
    private final LivenessIssue issue;
    private final String symptom;
    private final String mitigation;

    public LivenessScenario(String name, LivenessIssue issue, String symptom, String mitigation) {
        this.name = name;
        this.issue = issue;
        this.symptom = symptom;
        this.mitigation = mitigation;
    }

    public String name() {
        return name;
    }

    public LivenessIssue issue() {
        return issue;
    }

    public String symptom() {
        return symptom;
    }

    public String mitigation() {
        return mitigation;
    }
}

/*
Written Analysis:

1. lock-order-conflict
Issue: Deadlock
Reason: Two threads acquire the same locks in opposite order, so each thread can end up waiting for a lock held by the other.
Mitigation: Use consistent lock ordering so all threads acquire locks in the same order.

2. always-last-in-line
Issue: Starvation
Reason: One worker keeps losing access to the shared resource, so the program may keep running while that worker does not make progress.
Mitigation: Use fairer scheduling, a fair lock, and/or limit how long one thread can monopolize the resource.

3. over-polite-retry-loop
Issue: Livelock
Reason: The workers are not frozen, but they keep backing off and retrying in response to each other without completing useful work.
Mitigation: Add a priority rule, random delay, or backoff strategy so one worker eventually proceeds.
*/