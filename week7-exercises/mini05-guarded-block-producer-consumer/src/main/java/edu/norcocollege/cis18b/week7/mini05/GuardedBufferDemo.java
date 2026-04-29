package edu.norcocollege.cis18b.week7.mini05;

import java.util.List;

public class GuardedBufferDemo {

    public static void main(String[] args) throws InterruptedException {
        OneSlotMessageBuffer buffer = new OneSlotMessageBuffer();
        List<String> messages = List.of("alpha", "beta", "gamma");

        Thread producer = new Thread(() -> produce(messages, buffer), "producer");
        Thread consumer = new Thread(() -> consume(messages.size(), buffer), "consumer");

        producer.start();
        consumer.start();
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