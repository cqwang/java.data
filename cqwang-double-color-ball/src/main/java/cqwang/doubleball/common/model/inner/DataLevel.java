package cqwang.doubleball.common.model.inner;

import lombok.Getter;

public enum DataLevel {
    COLD(0.7),
    STABLE(2),
    HOT(3),
    SO_HOT(100);

    /**
     * 相对于平均频率的比率
     */
    @Getter
    private double toAvgFrequencyRatio;

    DataLevel(double toAvgFrequencyRatio) {
        this.toAvgFrequencyRatio = toAvgFrequencyRatio;
    }

    public boolean greatEqualsThen(DataLevel other) {
        return this.getToAvgFrequencyRatio() >= other.getToAvgFrequencyRatio();
    }

    public boolean lessEqualsThen(DataLevel other) {
        return this.getToAvgFrequencyRatio() <= other.getToAvgFrequencyRatio();
    }
}
