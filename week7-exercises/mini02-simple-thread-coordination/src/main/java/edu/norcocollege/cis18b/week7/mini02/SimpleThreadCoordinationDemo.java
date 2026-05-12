package edu.norcocollege.cis18b.week7.mini02;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SimpleThreadCoordinationDemo {

    public static void main(String[] args) throws InterruptedException {
        List<String> completionLog = runDemo();

        // This message is printed only after runDemo finishes joining all workers.
        System.out.println("All workers completed.");
        System.out.println(completionLog);
    }

    static List<String> runDemo() throws InterruptedException {
        // The latch acts like a starting gate.
        // Each worker waits here until the main thread releases them.
        CountDownLatch startGate = new CountDownLatch(1);

        // This list stores the final completion messages from each worker.
        // Since multiple threads write to it, access is protected in WorkerTask.
        List<String> completionLog = new ArrayList<>();

        // Create worker threads with meaningful names.
        // The numbers control how many steps each worker performs and
        // how long each step sleeps.
        List<Thread> workers = List.of(
            new Thread(new WorkerTask("grade-importer", 3, 20L, startGate, completionLog), "grade-importer"),
            new Thread(new WorkerTask("email-notifier", 2, 30L, startGate, completionLog), "email-notifier"),
            new Thread(new WorkerTask("roster-sync", 4, 15L, startGate, completionLog), "roster-sync")
        );

        // start() creates a new path of execution for each worker.
        // Calling run() directly would not create actual worker threads.
        for (Thread worker : workers) {
            worker.start();
        }

        System.out.println("All workers launched.");

        // Release all workers so they can begin their simulated work.
        startGate.countDown();

        // The main thread waits for every worker to finish before continuing.
        for (Thread worker : workers) {
            worker.join();
        }

        // Thread scheduling can make workers finish in different orders.
        // Sorting gives the final summary a stable, deterministic order.
        completionLog.sort(Comparator.naturalOrder());

        return completionLog;
    }
}