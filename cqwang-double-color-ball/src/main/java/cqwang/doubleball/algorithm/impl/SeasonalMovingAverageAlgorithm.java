package cqwang.doubleball.algorithm.impl;

import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 周期移动平均算法 - 基于相同周期位置的平均值
 */
public class SeasonalMovingAverageAlgorithm implements PredictionAlgorithm {
    private static final int SEASON_LENGTH = 52; // 52周为一个周期

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictBySeasonalAverage(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictBySeasonalAverage(blueBallDataDetail, blueRange);
    }

    private int predictBySeasonalAverage(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int currentIndex = dataList.size() - 1;
        int seasonalIndex = currentIndex % SEASON_LENGTH;

        double sum = 0;
        int count = 0;

        // 找同一季节位置的所有值
        for (int i = seasonalIndex; i < dataList.size(); i += SEASON_LENGTH) {
            sum += dataList.get(i);
            count++;
        }

        int result = (int) Math.round(sum / Math.max(1, count));
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
