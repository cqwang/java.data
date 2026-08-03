package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于欧几里得距离的相关性算法 - 预测蓝球
 * 通过计算预测的前6位与样本前6位的欧几里得距离倒数作为权重，加权聚合样本的第7位值
 */
public class EuclideanDistanceRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final double SMOOTH_FACTOR = 1.0;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty()) {
            return -1;
        }

        double totalWeight = 0;
        double weightedBlueSum = 0;

        for (VirtualDoubleColorBallItem sample : sampleList) {
            List<Integer> sampleBallValues = sample.getBallValueList();
            if (sampleBallValues == null || sampleBallValues.size() < 7) {
                continue;
            }

            double distance = calculateEuclideanDistance(
                    predictedRedValueList,
                    sampleBallValues.subList(0, 6)
            );

            double weight = 1.0 / (distance + SMOOTH_FACTOR);

            int sampleBlueValue = sampleBallValues.get(6);
            weightedBlueSum += weight * sampleBlueValue;
            totalWeight += weight;
        }

        if (totalWeight == 0) {
            return -1;
        }

        int predictedBlue = (int) Math.round(weightedBlueSum / totalWeight);
        return Math.max(34, Math.min(49, predictedBlue));
    }

    private double calculateEuclideanDistance(List<Integer> vector1, List<Integer> vector2) {
        if (vector1.size() != vector2.size()) {
            return Double.MAX_VALUE;
        }

        double sumSquaredDifferences = 0;
        for (int i = 0; i < vector1.size(); i++) {
            double diff = vector1.get(i) - vector2.get(i);
            sumSquaredDifferences += diff * diff;
        }

        return Math.sqrt(sumSquaredDifferences);
    }
}
