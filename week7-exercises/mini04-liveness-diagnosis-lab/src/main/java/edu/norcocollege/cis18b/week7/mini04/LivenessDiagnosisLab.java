package edu.norcocollege.cis18b.week7.mini04;

import java.util.List;

public class LivenessDiagnosisLab {

    public static void main(String[] args) {
        // Print each scenario name with its diagnosed liveness issue.
        // The List order is fixed, so the output stays deterministic.
        for (LivenessScenario scenario : scenarios()) {
            System.out.println(scenario.name() + " -> " + scenario.issue());
        }
    }

    static List<LivenessScenario> scenarios() {
        return List.of(
            new LivenessScenario(
                "lock-order-conflict",
                LivenessIssue.DEADLOCK,
                "Two threads acquire the same pair of locks in opposite order.",
                "Use consistent lock ordering."
            ),
            new LivenessScenario(
                "always-last-in-line",
                LivenessIssue.STARVATION,
                "A low-priority worker keeps losing access to a shared resource.",
                "Reduce unfair scheduling and bound how long one actor can monopolize the resource."
            ),
            new LivenessScenario(
                "over-polite-retry-loop",
                LivenessIssue.LIVELOCK,
                "Both workers keep backing off and retrying without making progress.",
                "Add backoff rules that eventually allow one side to proceed."
            )
        );
    }
}