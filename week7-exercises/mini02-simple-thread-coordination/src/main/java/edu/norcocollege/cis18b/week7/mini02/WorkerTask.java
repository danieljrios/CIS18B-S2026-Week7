package edu.norcocollege.cis18b.week7.mini02;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WorkerTask implements Runnable {
    private final String workerName;
    private final int steps;
    private final long delayMillis;
    private final CountDownLatch startGate;
    private final List<String> completionLog;

    public WorkerTask(String workerName, int steps, long delayMillis, CountDownLatch startGate, List<String> completionLog) {
        this.workerName = workerName;
        this.steps = steps;
        this.delayMillis = delayMillis;
        this.startGate = startGate;
        this.completionLog = completionLog;
    }

    @Override
    public void run() {
        try {
            startGate.await();
            for (int step = 1; step <= steps; step++) {
                Thread.sleep(delayMillis);
            }
            synchronized (completionLog) {
                completionLog.add(workerName + " finished " + steps + " steps");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            synchronized (completionLog) {
                completionLog.add(workerName + " interrupted");
            }
        }
    }
}