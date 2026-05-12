package edu.norcocollege.cis18b.week7.mini02;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WorkerTask implements Runnable {
    private final String workerName;
    private final int steps;
    private final long delayMillis;
    private final CountDownLatch startGate;
    private final List<String> completionLog;

    public WorkerTask(
        String workerName,
        int steps,
        long delayMillis,
        CountDownLatch startGate,
        List<String> completionLog
    ) {
        this.workerName = workerName;
        this.steps = steps;
        this.delayMillis = delayMillis;
        this.startGate = startGate;
        this.completionLog = completionLog;
    }

    @Override
    public void run() {
        try {
            // Wait until the main thread releases the start gate.
            startGate.await();

            // Simulate work by sleeping for a short time during each step.
            for (int step = 1; step <= steps; step++) {
                Thread.sleep(delayMillis);
            }

            // Multiple workers share the same completionLog list.
            // synchronized prevents two threads from modifying it at the same time.
            synchronized (completionLog) {
                completionLog.add(workerName + " finished " + steps + " steps");
            }
        } catch (InterruptedException ex) {
            // Preserve the interruption status so other code can tell
            // this thread was interrupted.
            Thread.currentThread().interrupt();

            // Log that this worker did not finish normally.
            synchronized (completionLog) {
                completionLog.add(workerName + " interrupted");
            }
        }
    }
}