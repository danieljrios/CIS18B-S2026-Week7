package edu.norcocollege.cis18b.week7.mini05;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

class OneSlotMessageBufferTest {

    @Test
    void takeBlocksUntilProducerAddsMessage() throws Exception {
        OneSlotMessageBuffer buffer = new OneSlotMessageBuffer();
        CompletableFuture<String> consumer = CompletableFuture.supplyAsync(() -> takeUnchecked(buffer));

        assertThrows(TimeoutException.class, () -> consumer.get(100, TimeUnit.MILLISECONDS));

        buffer.put("ready");

        assertEquals("ready", consumer.get(1, TimeUnit.SECONDS));
        assertTrue(buffer.isEmpty());
    }

    @Test
    void secondPutWaitsUntilConsumerRemovesExistingMessage() throws Exception {
        OneSlotMessageBuffer buffer = new OneSlotMessageBuffer();
        buffer.put("first");

        CompletableFuture<Void> secondPut = CompletableFuture.runAsync(() -> putUnchecked(buffer, "second"));

        assertThrows(TimeoutException.class, () -> secondPut.get(100, TimeUnit.MILLISECONDS));
        assertEquals("first", buffer.take());
        secondPut.get(1, TimeUnit.SECONDS);
        assertFalse(buffer.isEmpty());
        assertEquals("second", buffer.take());
    }

    private static String takeUnchecked(OneSlotMessageBuffer buffer) {
        try {
            return buffer.take();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }
    }

    private static void putUnchecked(OneSlotMessageBuffer buffer, String message) {
        try {
            buffer.put(message);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
        }
    }
}