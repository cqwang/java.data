package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 标准差加权算法 - 基于方差分布进行预测
 */
public class VarianceWeightedAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByVariance(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByVariance(blueBallDataDetail, blueRange);
    }

    private int predictByVariance(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        double mean = dataList.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double variance = dataList.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .average().orElse(0);

        double stdDev = Math.sqrt(variance);
        int result = (int) Math.round(mean + stdDev / 2);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
