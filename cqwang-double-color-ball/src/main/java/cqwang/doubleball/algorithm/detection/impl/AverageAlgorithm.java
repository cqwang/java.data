package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 均值算法 - 计算历史数据的平均值
 */
public class AverageAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return calculateAverage(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return calculateAverage(blueBallDataDetail, blueRange);
    }

    private int calculateAverage(BallDataDetail ballDataDetail, Range<Integer> range) {
        if (ballDataDetail.getDataList().isEmpty()) {
            return range.getMinimum();
        }
        long sum = ballDataDetail.getDataList().stream().mapToLong(Integer::longValue).sum();
        int avg = (int) Math.round((double) sum / ballDataDetail.getDataList().size());
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), avg));
    }
}
