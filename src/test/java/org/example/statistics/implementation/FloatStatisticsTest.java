package org.example.statistics.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FloatStatisticsTest {

    @Test
    @DisplayName("Should collect float statistics in full mode")
    void shouldCollectFloatStatisticsFullMode() {

        FloatStatistics floatStatistics = new FloatStatistics(true);

        floatStatistics.getString("1.5");
        floatStatistics.getString("2.5");
        floatStatistics.getString("3.5");
        floatStatistics.getString("4.5");
        floatStatistics.getString("string");

        assertEquals(4, floatStatistics.getCountOfCollectedLines());
        assertEquals(1.5, floatStatistics.getMinimum());
        assertEquals(4.5, floatStatistics.getMaximum());
        assertEquals(new BigDecimal("12.0"), floatStatistics.getSum());
    }

    @Test
    @DisplayName("Should print float statistics correctly in full mode")
    void shouldPrintFloatStatisticsFullMode() {

        FloatStatistics floatStatistics = new FloatStatistics(true);
        floatStatistics.getString("1.1");
        floatStatistics.getString("2.1");
        floatStatistics.getString("3.1");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        floatStatistics.print();

        String output = out.toString();
        assertTrue(output.contains("##### Float Statistics #####"));
        assertTrue(output.contains("Count of collected lines: 3"));
        assertTrue(output.contains("Minimum value: 1.1"));
        assertTrue(output.contains("Maximum value: 3.1"));
        assertTrue(output.contains("Sum: 6.3"));
        assertTrue(output.contains("Average value: 2.1000000000"));

        System.setOut(System.out);
    }
    @Test
    @DisplayName("Should print integer statistics correctly in short mode")
    void shouldPrintIntegerStatisticsShortMode() {

        FloatStatistics floatStatistics = new FloatStatistics(false);

        floatStatistics.getString("1.0");
        floatStatistics.getString("2.0");
        floatStatistics.getString("3.0");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        floatStatistics.print();

        String output = out.toString();
        assertTrue(output.contains("##### Float Statistics #####"));
        assertTrue(output.contains("Count of collected lines: 3"));

        assertFalse(output.contains("Minimum value"));
        assertFalse(output.contains("Maximum value"));
        assertFalse(output.contains("Sum"));
        assertFalse(output.contains("Average value"));

        System.setOut(System.out);
    }


}