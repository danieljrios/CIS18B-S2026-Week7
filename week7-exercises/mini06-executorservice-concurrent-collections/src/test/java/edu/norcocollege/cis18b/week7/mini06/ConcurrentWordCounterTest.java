package edu.norcocollege.cis18b.week7.mini06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ConcurrentWordCounterTest {

    @Test
    void aggregatesWordCountsSafely() throws Exception {
        ConcurrentWordCounter counter = new ConcurrentWordCounter();

        CountSummary summary = counter.countWords(List.of(
            List.of("alert", "info"),
            List.of("warning", "alert"),
            List.of("info", "info")
        ));

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("alert", 2);
        expected.put("info", 3);
        expected.put("warning", 1);

        assertEquals(6, summary.processedTokens());
        assertEquals(expected, summary.counts());
    }
}