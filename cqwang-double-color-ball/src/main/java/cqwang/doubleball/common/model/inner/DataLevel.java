package cqwang.doubleball.common.model.inner;

import lombok.Getter;

public enum DataLevel {
    COLD(0.7),
    STABLE(2),
    HOT(100),
    ;

    /**
     * 相对于平均频率的比率
     */
    @Getter
    private double toAvgFrequencyRatio;

    DataLevel(double toAvgFrequencyRatio) {
        this.toAvgFrequencyRatio = toAvgFrequencyRatio;
    }
}
