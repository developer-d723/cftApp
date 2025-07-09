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

        IntegerStatistics integerStatistics = new IntegerStatistics(true);

        integerStatistics.getString(Long.MAX_VALUE + "");
        integerStatistics.getString("1");
        integerStatistics.getString(Long.MIN_VALUE + "");

        assertEquals(3, integerStatistics.getCountOfCollectedLines());
        assertEquals(Long.MIN_VALUE, integerStatistics.getMinimum());
        assertEquals(Long.MAX_VALUE, integerStatistics.getMaximum());
        assertEquals(
                new BigDecimal(Long.MAX_VALUE)
                        .add(BigDecimal.ONE)
                        .add(new BigDecimal(Long.MIN_VALUE)),
                integerStatistics.getSum()
        );
    }

    @Test
    @DisplayName("Should print integer statistics correctly in short mode")
    void shouldPrintIntegerStatisticsShortMode() {

        IntegerStatistics integerStatistics = new IntegerStatistics(false);
        integerStatistics.getString("1");
        integerStatistics.getString("2");
        integerStatistics.getString("3");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        integerStatistics.print();

        String output = out.toString();
        assertTrue(output.contains("##### Integer Statistics #####"));
        assertTrue(output.contains("Count of collected lines: 3"));

        assertFalse(output.contains("Minimum value"));
        assertFalse(output.contains("Maximum value"));
        assertFalse(output.contains("Sum"));
        assertFalse(output.contains("Average value"));

        System.setOut(System.out);
    }

}