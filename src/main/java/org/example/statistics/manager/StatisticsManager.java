package org.example.statistics.manager;

import org.example.line.CheckLineFormatResult;
import org.example.line.LineType;
import org.example.statistics.StatisticsCollector;
import org.example.statistics.implementation.FloatStatistics;
import org.example.statistics.implementation.IntegerStatistics;
import org.example.statistics.implementation.StringStatistics;

import java.util.EnumMap;
import java.util.Map;

/**
 * Manages all statistics statisticsCollectors and delegates data to the appropriate one.
 */
public class StatisticsManager {
    // A map of collectors for each line type.
    private final Map<LineType, StatisticsCollector> statisticsCollectors = new EnumMap<>(LineType.class);

    /**
     * Initializes the statistics manager and creates statisticsCollectors for each line type.
     * @param isFullStatistics shows to collect full or short statistics.
     */
    public StatisticsManager(boolean isFullStatistics) {
        statisticsCollectors.put(LineType.INTEGER, new IntegerStatistics(isFullStatistics));
        statisticsCollectors.put(LineType.FLOAT, new FloatStatistics(isFullStatistics));
        statisticsCollectors.put(LineType.STRING, new StringStatistics(isFullStatistics));
    }

    /**
     * Collects statistics for a line processing result.
     * Finds the correct collector based on the line type and passes data to it.
     *
     * @param result The result of the line format check.
     */
    public void collectStatistics(CheckLineFormatResult result) {

        StatisticsCollector collector = statisticsCollectors.get(result.lineType());
        if (collector != null) {
            collector.getString(result.string());
        }
    }


    /**
     * Prints the final statistics report by calling the print() method of each collector.
     * A collector will only print its report if it has processed at least one item.
     */
    public void printStatistics() {
        for (StatisticsCollector collector : statisticsCollectors.values()) {
            if (collector.getCountOfCollectedLines() > 0) {
                collector.print();
            }
        }
    }

    public Map<LineType, StatisticsCollector> getStatisticsCollectors() {
        return statisticsCollectors;
    }

}
