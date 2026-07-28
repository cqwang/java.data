package cqwang.doubleball.algorithm.detection.impl.single;

/**
 * 周期移动平均算法 - 基于相同周期位置的平均值
 */
public class SeasonalMovingAverage101Algorithm extends SeasonalMovingAverage52Algorithm {
    private static final int SEASON_LENGTH = 101;

    @Override
    protected int getSeasonLength() {
        return SEASON_LENGTH;
    }
}
