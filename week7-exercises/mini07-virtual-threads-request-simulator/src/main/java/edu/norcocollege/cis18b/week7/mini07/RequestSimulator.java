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
        ExecutorService executor = VirtualThreadSupport.newExecutor();
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int requestId = 1; requestId <= requestCount; requestId++) {
                final int currentId = requestId;
                futures.add(executor.submit(() -> handleRequest(currentId, delayMillis)));
            }

            List<String> completions = new ArrayList<>();
            for (Future<String> future : futures) {
                completions.add(future.get());
            }

            completions.sort(Comparator.naturalOrder());
            return completions;
        } finally {
            executor.shutdown();
        }
    }

    private String handleRequest(int requestId, long delayMillis) throws InterruptedException {
        Thread.sleep(delayMillis);
        return "request-" + requestId + " complete";
    }
}