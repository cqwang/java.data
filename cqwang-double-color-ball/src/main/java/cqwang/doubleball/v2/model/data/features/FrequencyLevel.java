package cqwang.doubleball.v2.model.data.features;

import lombok.Getter;

/**
 * 频次划分
 */
public enum FrequencyLevel {

    NONE(-1),

    /**
     * 太冷门的数据
     */
    SO_COLD(0.2),
    /**
     * 较冷门的数据
     */
    COLD(0.7),

    /**
     * 稳定数据
     */
    STABLE(2),
    /**
     * 热门数据
     */
    HOT(3),
    /**
     * 太热门数据
     */
    SO_HOT(100),
    ;

    /**
     * 相对于平均频率的比率
     */
    @Getter
    private double toAvgFrequencyRatio;

    FrequencyLevel(double toAvgFrequencyRatio) {
        this.toAvgFrequencyRatio = toAvgFrequencyRatio;
    }

    public boolean greatEqualsThen(FrequencyLevel other) {
        return this.getToAvgFrequencyRatio() >= other.getToAvgFrequencyRatio();
    }

    public boolean lessEqualsThen(FrequencyLevel other) {
        return this.getToAvgFrequencyRatio() <= other.getToAvgFrequencyRatio();
    }

    public boolean between(FrequencyLevel min, FrequencyLevel max){
        return greatEqualsThen(min) && lessEqualsThen(max);
    }

    public boolean equals(FrequencyLevel other){
        return this.getToAvgFrequencyRatio() == other.getToAvgFrequencyRatio();
    }
}
