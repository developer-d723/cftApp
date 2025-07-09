package org.example.statistics.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StringStatisticsTest {

    @Test
    @DisplayName("Should collect string statistics in full mode")
    void shouldCollectStringStatisticsFullMode() {

        StringStatistics stringStatistics = new StringStatistics(true);

        stringStatistics.getString("a1");
        stringStatistics.getString("b2");
        stringStatistics.getString("string3");
        stringStatistics.getString("string4");
        stringStatistics.getString("");
        stringStatistics.getString("ABCDEFGHIJ");

        assertEquals(6, stringStatistics.getCountOfCollectedLines());
        assertEquals(0, stringStatistics.getMinimumLength());
        assertEquals(10, stringStatistics.getMaximumLength());
    }

    @Test
    @DisplayName("Should print string statistics correctly in full mode")
    void shouldPrintStringStatisticsFullMode() {

        StringStatistics stringStatistics = new StringStatistics(true);
        stringStatistics.getString("string");
        stringStatistics.getString("string2");
        stringStatistics.getString("string3");
        stringStatistics.getString("string1234");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));


        stringStatistics.print();

        String output = out.toString();
        assertTrue(output.contains("##### String Statistics #####"));
        assertTrue(output.contains("Count of collected lines: 4"));
        assertTrue(output.contains("Shortest line length: 6"));
        assertTrue(output.contains("Longest line length: 10"));

        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should handle unicode characters in string statistics")
    void shouldHandleUnicodeInStringStatistics() {

        StringStatistics stringStatistics = new StringStatistics(true);

        stringStatistics.getString("Абвг");
        stringStatistics.getString("✅ 1234 Абвabc");

        assertEquals(2, stringStatistics.getCountOfCollectedLines());
        assertEquals(4, stringStatistics.getMinimumLength());
        assertEquals(13, stringStatistics.getMaximumLength());
    }

    @Test
    @DisplayName("Should print string statistics correctly in short mode")
    void shouldPrintStringStatisticsShortMode() {
        StringStatistics stringStatistics = new StringStatistics(false);
        stringStatistics.getString("test");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        stringStatistics.print();

        String output = out.toString();
        assertTrue(output.contains("##### String Statistics #####"));
        assertTrue(output.contains("Count of collected lines: 1"));

        assertFalse(output.contains("Shortest line length"));
        assertFalse(output.contains("Longest line length"));

        System.setOut(System.out);
    }


}