package org.example.statistics.implementation;

import org.example.statistics.AbstractNumberStatistics;

public class FloatStatistics extends AbstractNumberStatistics<Double> {
    public FloatStatistics(boolean isFullStatistics) {
        super(isFullStatistics);
    }

    @Override
    protected Double parseNumber(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public void print() {
        if (getCountOfCollectedLines() == 0) return;
        System.out.println("##### Float Statistics #####");
        super.print();
    }
}