package org.example.statistics.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IntegerStatisticsTest {

    @Test
    @DisplayName("Should collect integer statistics in full mode")
    void shouldCollectIntegerStatisticsFullMode() {

        IntegerStatistics integerStatistics = new IntegerStatistics(true);

        integerStatistics.getString("-1");
        integerStatistics.getString("2");
        integerStatistics.getString("3");
        integerStatistics.getString("4");
        integerStatistics.getString("string");


        assertEquals(4, integerStatistics.getCountOfCollectedLines());
        assertEquals(-1L, integerStatistics.getMinimum());
        assertEquals(4L, integerStatistics.getMaximum());
        assertEquals(new BigDecimal("8"), integerStatistics.getSum());
    }

    @Test
    @DisplayName("Should print integer statistics correctly in full mode")
    void shouldPrintIntegerStatisticsFullMode() {

        IntegerStatistics integerStatistics = new IntegerStatistics(true);
        integerStatistics.getString("10");
        integerStatistics.getString("20");
        integerStatistics.getString("30");
        integerStatistics.getString("40");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        integerStatistics.print();

        String output = out.toString();
        assertTrue(output.contains("##### Integer Statistics #####"));
        assertTrue(output.contains("Count of collected lines: 4"));
        assertTrue(output.contains("Minimum value: 10"));
        assertTrue(output.contains("Maximum value: 40"));
        assertTrue(output.contains("Sum: 100"));
        assertTrue(output.contains("Average value: 25.0000000000"));

        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should handle integer overflow in full mode")
    void shouldHandleIntegerOverflow() {

        IntegerStatistics stats = new IntegerStatistics(true);

        stats.getString(Long.MAX_VALUE + "");
        stats.getString("1");
        stats.getString(Long.MIN_VALUE + "");

        assertEquals(3, stats.getCountOfCollectedLines());
        assertEquals(Long.MIN_VALUE, stats.getMinimum());
        assertEquals(Long.MAX_VALUE, stats.getMaximum());
        assertEquals(
                new BigDecimal(Long.MAX_VALUE)
                        .add(BigDecimal.ONE)
                        .add(new BigDecimal(Long.MIN_VALUE)),
                stats.getSum()
        );
    }
}