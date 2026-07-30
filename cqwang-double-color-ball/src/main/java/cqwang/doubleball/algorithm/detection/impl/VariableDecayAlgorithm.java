package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 递增衰减算法 - 从最近往前衰减，步长可变
 */
public class VariableDecayAlgorithm implements PredictionAlgorithm {
    private static final int SAMPLE_SIZE = 70;
    private static final double BASE_DECAY = 0.87;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictValue(blueBallDataDetail, blueRange);
    }

    private int predictValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - SAMPLE_SIZE);
        double maxWeight = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weight = 0;
            int decayCount = 0;

            for (int j = dataList.size() - 1; j >= startIdx; j--) {
                if (dataList.get(j) == i) {
                    // 动态衰减：近期衰减慢，远期衰减快
                    double decayFactor = decayCount < 10 ? Math.pow(BASE_DECAY, decayCount * 0.8)
                            : Math.pow(BASE_DECAY, 8 + (decayCount - 10) * 1.2);
                    weight += decayFactor;
                }
                decayCount++;
            }

            if (weight > maxWeight) {
                maxWeight = weight;
                result = i;
            }
        }

        return result;
    }
}
