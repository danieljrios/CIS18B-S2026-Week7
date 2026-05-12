package edu.norcocollege.cis18b.week7.mini05;

public class OneSlotMessageBuffer {
    private String message;
    private boolean empty = true;

    public synchronized void put(String nextMessage) throws InterruptedException {
        // If the slot is already full, the producer must wait.
        // Use while instead of if because the condition should be checked again
        // after the thread wakes up.
        while (!empty) {
            wait();
        }

        // Store the new message and mark the buffer as full.
        message = nextMessage;
        empty = false;

        // Wake any waiting consumers or producers after the state changes.
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        // If the slot is empty, the consumer must wait.
        // Use while instead of if to protect against waking up before the
        // condition is actually true.
        while (empty) {
            wait();
        }

        // Save the message before clearing the slot.
        String result = message;
        message = null;
        empty = true;

        // Wake any waiting producers or consumers after the state changes.
        notifyAll();

        return result;
    }

    public synchronized boolean isEmpty() {
        return empty;
    }
}