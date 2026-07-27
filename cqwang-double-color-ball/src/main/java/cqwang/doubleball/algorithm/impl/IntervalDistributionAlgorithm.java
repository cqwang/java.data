package cqwang.doubleball.algorithm.impl;

import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 区间分布算法 - 基于数据分布的中点进行预测
 */
public class IntervalDistributionAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int min = Math.max(redRange.getMinimum(), redBallDataDetail.getMin());
        int max = Math.min(redRange.getMaximum(), redBallDataDetail.getMax());
        return (min + max) / 2;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int min = Math.max(blueRange.getMinimum(), blueBallDataDetail.getMin());
        int max = Math.min(blueRange.getMaximum(), blueBallDataDetail.getMax());
        return (min + max) / 2;
    }
}
