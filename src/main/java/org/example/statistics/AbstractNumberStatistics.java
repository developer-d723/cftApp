package org.example.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractNumberStatistics<N extends Number & Comparable<N>> implements StatisticsCollector {
    protected final boolean isFullStatistics;
    /* BigDecimal is required.
    12345678901234567890 is already bigger than Long.MAX_VALUE, which is
    9223372036854775807 */
    protected BigDecimal sum = BigDecimal.ZERO;
    protected N minimum;
    protected N maximum;
    protected long countOfCollectedLines = 0;

    public boolean isFullStatistics() {
        return isFullStatistics;
    }

    public N getMaximum() {
        return maximum;
    }

    public N getMinimum() {
        return minimum;
    }

    public BigDecimal getSum() {
        return sum;
    }

    protected AbstractNumberStatistics(boolean isFullStatistics) {
        this.isFullStatistics = isFullStatistics;
    }

    protected abstract N parseNumber(String value);

    @Override
    public void getString(String line) {
        try {
            N number = parseNumber(line);
            countOfCollectedLines++;

            if (isFullStatistics) {
                sum = sum.add(new BigDecimal(number.toString()));
                if (minimum == null || number.compareTo(minimum) < 0) {
                    minimum = number;
                }
                if (maximum == null || number.compareTo(maximum) > 0) {
                    maximum = number;
                }
            }
        } catch (NumberFormatException n) {
        }
    }


    @Override
    public void print() {
        if (countOfCollectedLines == 0) return;

        System.out.println("Count of collected lines: " + countOfCollectedLines);
        if (isFullStatistics) {
            System.out.println("Minimum value: " + minimum);
            System.out.println("Maximum value: " + maximum);
            System.out.println("Sum: " + sum);
            if (countOfCollectedLines > 0) {
                BigDecimal avg = sum.divide(BigDecimal.valueOf(countOfCollectedLines), 10, RoundingMode.HALF_UP);
                System.out.println("Average value: " + avg);
            }
        }
    }

    @Override
    public long getCountOfCollectedLines() {
        return countOfCollectedLines;
    }
}