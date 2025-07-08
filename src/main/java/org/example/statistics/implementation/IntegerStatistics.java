package org.example.statistics.implementation;

import org.example.statistics.AbstractNumberStatistics;

public class IntegerStatistics extends AbstractNumberStatistics<Long> {
    public IntegerStatistics(boolean isFullStatistics) {
        super(isFullStatistics);
    }

    @Override
    protected Long parseNumber(String value) {
        return Long.parseLong(value);
    }

    @Override
    public void print() {
        if (getCountOfCollectedLines() == 0) return;
        System.out.println("##### Integer Statistics #####");
        super.print();
    }
}