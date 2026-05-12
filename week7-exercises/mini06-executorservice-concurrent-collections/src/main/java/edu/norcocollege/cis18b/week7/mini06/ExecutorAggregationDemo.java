package edu.norcocollege.cis18b.week7.mini06;

import java.util.List;

public class ExecutorAggregationDemo {

    public static void main(String[] args) throws Exception {
        ConcurrentWordCounter counter = new ConcurrentWordCounter();

        // Each inner list represents one batch of tokens.
        // The batches can be processed concurrently.
        CountSummary summary = counter.countWords(List.of(
            List.of("alert", "info", "warning"),
            List.of("info", "alert"),
            List.of("warning", "alert", "info")
        ));

        System.out.println("Processed tokens: " + summary.processedTokens());

        // The snapshot is sorted, so this output stays deterministic.
        summary.counts().forEach((token, count) -> System.out.println(token + " -> " + count));
    }
}