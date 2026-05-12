package edu.norcocollege.cis18b.week7.mini01;

import java.util.List;

// Added comments to describe the already done implementation

public class ProcessVsThreadClinic {

    public static void main(String[] args) {
        // Loops through each default scenario and print its classification.
        // This keeps the output deterministic because the scenarios are stored
        // in a fixed List order.
        for (Scenario scenario : defaultScenarios()) {
            System.out.println(scenario.name() + " -> " + scenario.recommendation());
        }
    }

    static List<Scenario> defaultScenarios() {
        return List.of(
            new Scenario(
                "student-code-runner",
                "Run untrusted student code with stronger fault isolation.",
                Recommendation.PROCESS,
                "Separate address spaces reduce the blast radius of crashes or unsafe code."
            ),
            new Scenario(
                "gradebook-auto-save",
                "Save updates while the UI remains responsive.",
                Recommendation.THREAD,
                "Shared in-process state makes background saves convenient, but shared data must be protected."
            ),
            new Scenario(
                "sort-single-list-once",
                "Sort one in-memory list and print it immediately.",
                Recommendation.NOT_MEANINGFULLY_CONCURRENT,
                "There is only one task, so concurrency adds complexity without benefit."
            )
        );
    }

    // A Scenario stores the information for one situation:
    // its name, description, recommendation, and explanation.
    record Scenario(
        String name,
        String description,
        Recommendation recommendation,
        String reasoning
    ) {
    }

    // These are the three possible classifications for each scenario.
    enum Recommendation {
        PROCESS,
        THREAD,
        NOT_MEANINGFULLY_CONCURRENT
    }
}

// Written responses:
/*

1. student-code-runner
This scenario fits PROCESS because untrusted student code should not run in
 the same memory space as the main program. If the student code crashes, 
 freezes, or just generally behaves unsafely, process isolation helps keep the rest of the 
 system protected. A separate process has a stronger boundary than a thread.

2. gradebook-auto-save
This scenario fits THREAD because the auto-save task can run in the background
 while the user keeps working in the same application. Shared memory is 
 helpful here because the thread can access the current gradebook data more 
 easily. However, that shared data also creates risk so then updates would 
 need to be protected to avoid race conditions.

3. sort-single-list-once
This scenario is NOT_MEANINGFULLY_CONCURRENT because there is only one main 
task which is: sorting one list and printing it. Using threads or processes would add 
extra complexity without any real benefit. So a good thing to run with here 
would be that just because something could be concurrent does not mean it should be.

*/