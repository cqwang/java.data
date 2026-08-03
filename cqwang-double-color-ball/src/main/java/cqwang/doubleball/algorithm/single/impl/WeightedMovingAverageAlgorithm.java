package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 加权移动平均算法 - 最近的数据权重更高
 */
public class WeightedMovingAverageAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return calculateWeightedMovingAverage(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return calculateWeightedMovingAverage(blueBallDataDetail, blueRange);
    }

    private int calculateWeightedMovingAverage(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        double weightedSum = 0;
        double weightSum = 0;
        int windowSize = Math.min(20, dataList.size());

        for (int i = 0; i < windowSize; i++) {
            double weight = i + 1;
            weightedSum += dataList.get(dataList.size() - windowSize + i) * weight;
            weightSum += weight;
        }

        int result = (int) Math.round(weightedSum / weightSum);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
