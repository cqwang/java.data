package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 概率分布算法 - 基于离散概率分布的预测
 */
public class ProbabilityDistributionAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByProbability(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByProbability(blueBallDataDetail, blueRange);
    }

    private int predictByProbability(BallDataDetail ballDataDetail, Range<Integer> range) {
        int totalCount = ballDataDetail.getDataList().size();
        if (totalCount == 0) {
            return range.getMinimum();
        }

        double maxProbability = -1;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int frequency = ballDataDetail.getDataFrequencyMap().getOrDefault(i, 0);
            double probability = (double) frequency / totalCount;
            if (probability > maxProbability) {
                maxProbability = probability;
                result = i;
            }
        }
        return result;
    }
}
