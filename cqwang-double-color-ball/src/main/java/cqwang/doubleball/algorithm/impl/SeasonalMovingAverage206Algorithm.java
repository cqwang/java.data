package cqwang.doubleball.algorithm.impl;

/**
 * 周期移动平均算法 - 基于相同周期位置的平均值
 */
public class SeasonalMovingAverage206Algorithm extends SeasonalMovingAverage52Algorithm {
    private static final int SEASON_LENGTH = 206;

    @Override
    protected int getSeasonLength() {
        return SEASON_LENGTH;
    }
}
