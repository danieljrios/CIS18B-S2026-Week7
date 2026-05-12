package edu.norcocollege.cis18b.week7.mini06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentWordCounter {

    public CountSummary countWords(List<List<String>> batches) throws InterruptedException, ExecutionException {
        // Create a fixed thread pool.
        // The pool uses at least 1 thread and at most 4 threads, depending on the number of batches.
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(1, Math.min(4, batches.size())));

        // ConcurrentHashMap allows multiple threads to safely update the map.
        // AtomicInteger allows each word count to be incremented safely.
        ConcurrentHashMap<String, AtomicInteger> counts = new ConcurrentHashMap<>();

        try {
            List<Callable<Integer>> tasks = new ArrayList<>();

            // Create one Callable task for each batch of words.
            // Each task processes its batch and returns the number of tokens it handled.
            for (List<String> batch : batches) {
                tasks.add(() -> processBatch(batch, counts));
            }

            int processedTokens = 0;

            // invokeAll starts the tasks and waits for all of them to complete.
            List<Future<Integer>> futures = executor.invokeAll(tasks);

            // Get the token count result from each completed task.
            for (Future<Integer> future : futures) {
                processedTokens += future.get();
            }

            // Return the total number of processed tokens and a stable copy of the counts.
            return new CountSummary(processedTokens, snapshot(counts));
        } finally {
            // Always shut down the executor so the program can exit cleanly.
            executor.shutdown();
        }
    }

    private int processBatch(List<String> batch, ConcurrentHashMap<String, AtomicInteger> counts) {
        for (String token : batch) {
            // computeIfAbsent safely creates the AtomicInteger only if the token is not already in the map.
            // incrementAndGet safely increments that token's count.
            counts.computeIfAbsent(token, key -> new AtomicInteger()).incrementAndGet();
        }

        return batch.size();
    }

    private Map<String, Integer> snapshot(ConcurrentHashMap<String, AtomicInteger> counts) {
        // Sort by key so the final output and tests are deterministic.
        return counts.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
            .collect(
                LinkedHashMap::new,
                (ordered, entry) -> ordered.put(entry.getKey(), entry.getValue().get()),
                LinkedHashMap::putAll
            );
    }
}