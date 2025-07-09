package org.example.statistics.manager;

import org.example.line.CheckLineFormatResult;
import org.example.line.LineType;
import org.example.statistics.StatisticsCollector;
import org.example.statistics.implementation.FloatStatistics;
import org.example.statistics.implementation.IntegerStatistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsManagerTest {


    @Test
    @DisplayName("Should find correct collectors")
    void shouldFindCorrectCollectors() {

        StatisticsManager statisticsManager = new StatisticsManager(true);

        statisticsManager.collectStatistics(new CheckLineFormatResult("1", LineType.INTEGER));
        statisticsManager.collectStatistics(new CheckLineFormatResult("1.234", LineType.FLOAT));
        statisticsManager.collectStatistics(new CheckLineFormatResult("string", LineType.STRING));
        statisticsManager.collectStatistics(new CheckLineFormatResult(" string", LineType.STRING));
        statisticsManager.collectStatistics(new CheckLineFormatResult("-200", LineType.INTEGER));

        assertEquals(2, getStatisticCollector(statisticsManager, LineType.INTEGER).getCountOfCollectedLines());
        assertEquals(1, getStatisticCollector(statisticsManager, LineType.FLOAT).getCountOfCollectedLines());
        assertEquals(2, getStatisticCollector(statisticsManager, LineType.STRING).getCountOfCollectedLines());
    }

    @Test
    @DisplayName("Should print only non-empty statistics")
    void shouldPrintOnlyNonEmptyStatistics() {

        StatisticsManager statisticsManager = new StatisticsManager(true);
        statisticsManager.collectStatistics(new CheckLineFormatResult("100", LineType.INTEGER));
        statisticsManager.collectStatistics(new CheckLineFormatResult("string", LineType.STRING));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        statisticsManager.printStatistics();
        String output = out.toString();
        assertTrue(output.contains("Integer Statistics"));
        assertFalse(output.contains("Float Statistics"));
        assertTrue(output.contains("String Statistics"));
        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should handle mixed statistics modes")
    void shouldHandleMixedStatisticsModes() {
        StatisticsManager shortModeManager = new StatisticsManager(false);
        StatisticsManager fullModeManager = new StatisticsManager(true);

        shortModeManager.collectStatistics(new CheckLineFormatResult("100", LineType.INTEGER));
        fullModeManager.collectStatistics(new CheckLineFormatResult("-1.2345", LineType.FLOAT));

        assertFalse(((IntegerStatistics) getStatisticCollector(shortModeManager, LineType.INTEGER)).isFullStatistics());
        assertTrue(((FloatStatistics) getStatisticCollector(fullModeManager, LineType.FLOAT)).isFullStatistics());
    }

    private StatisticsCollector getStatisticCollector(StatisticsManager manager, LineType type) {
        return manager.getStatisticsCollectors().get(type);
    }
}