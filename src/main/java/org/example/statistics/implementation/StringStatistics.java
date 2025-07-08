package org.example.statistics.implementation;


import org.example.statistics.StatisticsCollector;

public class StringStatistics implements StatisticsCollector {
    private final boolean isFullMode;
    private long countOfCollectedLines = 0L;
    private Integer minimumLength = null; // Maximum String length is Integer.MAX_VALUE
    private Integer maximumLength = null;

    public StringStatistics(boolean isFullMode) {
        this.isFullMode = isFullMode;
    }

    @Override
    public void getString(String value) {
        countOfCollectedLines++;
        int length = value.length();

        if (minimumLength == null) {
            minimumLength = length;
            maximumLength = length;
        } else {
            if (length < minimumLength) {
                minimumLength = length;
            }
            if (length > maximumLength) {
                maximumLength = length;
            }
        }
    }

    @Override
    public void print() {
        if (countOfCollectedLines == 0) return;

        System.out.println("##### String Statistics #####");
        System.out.println("Count of collected lines: " + countOfCollectedLines);

        if (isFullMode) {
            System.out.println("Shortest line length: " + minimumLength);
            System.out.println("Longest line length: " + maximumLength);
        }
    }

    @Override
    public long getCountOfCollectedLines() {
        return countOfCollectedLines;
    }
}