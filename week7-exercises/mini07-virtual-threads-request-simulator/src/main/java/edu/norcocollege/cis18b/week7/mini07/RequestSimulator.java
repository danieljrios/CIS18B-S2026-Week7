package edu.norcocollege.cis18b.week7.mini07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RequestSimulator {

    public List<String> simulateRequests(int requestCount, long delayMillis)
            throws InterruptedException, ExecutionException {
        // This executor uses virtual threads when Java 21 is available.
        ExecutorService executor = VirtualThreadSupport.newExecutor();

        try {
            List<Future<String>> futures = new ArrayList<>();

            // Submit one blocking request task per request.
            for (int requestId = 1; requestId <= requestCount; requestId++) {
                final int currentId = requestId;
                futures.add(executor.submit(() -> handleRequest(currentId, delayMillis)));
            }

            List<String> completions = new ArrayList<>();

            // Collect each task result.
            for (Future<String> future : futures) {
                completions.add(future.get());
            }

            // Sort the completions so the final summary is deterministic.
            completions.sort(Comparator.naturalOrder());

            return completions;
        } finally {
            // Shut down the executor after all work is complete.
            executor.shutdown();
        }
    }

    private String handleRequest(int requestId, long delayMillis) throws InterruptedException {
        // Simulate a slow/blocking request, such as waiting on I/O.
        Thread.sleep(delayMillis);
        return "request-" + requestId + " complete";
    }
}