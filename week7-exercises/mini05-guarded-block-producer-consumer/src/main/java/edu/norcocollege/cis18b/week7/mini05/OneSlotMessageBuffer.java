package edu.norcocollege.cis18b.week7.mini05;

public class OneSlotMessageBuffer {
    private String message;
    private boolean empty = true;

    public synchronized void put(String nextMessage) throws InterruptedException {
        while (!empty) {
            wait();
        }
        message = nextMessage;
        empty = false;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (empty) {
            wait();
        }
        String result = message;
        message = null;
        empty = true;
        notifyAll();
        return result;
    }

    public synchronized boolean isEmpty() {
        return empty;
    }
}