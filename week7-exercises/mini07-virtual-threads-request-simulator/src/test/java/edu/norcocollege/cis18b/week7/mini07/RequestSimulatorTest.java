package edu.norcocollege.cis18b.week7.mini07;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class RequestSimulatorTest {

    @Test
    void usesCompatibilityPathWhenVirtualThreadsUnavailable() throws Exception {
        RequestSimulator simulator = new RequestSimulator();

        if (!VirtualThreadSupport.isAvailable()) {
            IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> simulator.simulateRequests(3, 1L));
            assertTrue(error.getMessage().contains("Java 21"));
            return;
        }

        assertEquals(
            List.of("request-1 complete", "request-2 complete", "request-3 complete"),
            simulator.simulateRequests(3, 1L)
        );
    }
}