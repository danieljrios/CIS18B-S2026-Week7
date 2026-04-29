package edu.norcocollege.cis18b.week7.mini01;

import java.util.List;

public class ProcessVsThreadClinic {

    public static void main(String[] args) {
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

    record Scenario(String name, String description, Recommendation recommendation, String reasoning) {
    }

    enum Recommendation {
        PROCESS,
        THREAD,
        NOT_MEANINGFULLY_CONCURRENT
    }
}