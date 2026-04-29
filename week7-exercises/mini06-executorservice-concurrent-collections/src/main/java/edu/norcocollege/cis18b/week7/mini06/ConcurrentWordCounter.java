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
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(1, Math.min(4, batches.size())));
        ConcurrentHashMap<String, AtomicInteger> counts = new ConcurrentHashMap<>();

        try {
            List<Callable<Integer>> tasks = new ArrayList<>();
            for (List<String> batch : batches) {
                tasks.add(() -> processBatch(batch, counts));
            }

            int processedTokens = 0;
            List<Future<Integer>> futures = executor.invokeAll(tasks);
            for (Future<Integer> future : futures) {
                processedTokens += future.get();
            }

            return new CountSummary(processedTokens, snapshot(counts));
        } finally {
            executor.shutdown();
        }
    }

    private int processBatch(List<String> batch, ConcurrentHashMap<String, AtomicInteger> counts) {
        for (String token : batch) {
            counts.computeIfAbsent(token, key -> new AtomicInteger()).incrementAndGet();
        }
        return batch.size();
    }

    private Map<String, Integer> snapshot(ConcurrentHashMap<String, AtomicInteger> counts) {
        return counts.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
            .collect(LinkedHashMap::new,
                (ordered, entry) -> ordered.put(entry.getKey(), entry.getValue().get()),
                LinkedHashMap::putAll);
    }
}