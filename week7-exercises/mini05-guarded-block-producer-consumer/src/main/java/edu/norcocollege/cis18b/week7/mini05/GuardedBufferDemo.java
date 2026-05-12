package edu.norcocollege.cis18b.week7.mini05;

import java.util.List;

public class GuardedBufferDemo {

    public static void main(String[] args) throws InterruptedException {
        OneSlotMessageBuffer buffer = new OneSlotMessageBuffer();
        List<String> messages = List.of("alpha", "beta", "gamma");

        // The producer adds messages to the one-slot buffer.
        Thread producer = new Thread(() -> produce(messages, buffer), "producer");

        // The consumer removes the same number of messages from the buffer.
        Thread consumer = new Thread(() -> consume(messages.size(), buffer), "consumer");

        producer.start();
        consumer.start();

        // Wait for both threads to finish before ending the program.
        producer.join();
        consumer.join();
    }

    private static void produce(List<String> messages, OneSlotMessageBuffer buffer) {
        try {
            for (String message : messages) {
                buffer.put(message);
                System.out.println("Produced: " + message);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static void consume(int count, OneSlotMessageBuffer buffer) {
        try {
            for (int index = 0; index < count; index++) {
                System.out.println("Consumed: " + buffer.take());
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}