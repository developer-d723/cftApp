package org.example.statistics;

/**
 * An interface for all statistics collectors.
 *
 */
public interface StatisticsCollector {
    /**
     * Gets a new string for the statistics.
     *
     * @param line The collected string.
     */
    void getString(String line);

    /**
     * Prints the collected statistics into the terminal
     */
    void print();

    /**
     * @return Total count of collected lines
     */
    long getCountOfCollectedLines();
}