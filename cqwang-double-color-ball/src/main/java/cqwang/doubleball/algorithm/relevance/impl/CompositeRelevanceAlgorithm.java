package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于多特征加权综合的相关性算法 - 预测蓝球
 * 结合余弦相似度、欧几里得距离倒数、曼哈顿距离倒数的多个相似度指标，加权聚合样本的第7位值
 */
public class CompositeRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final double SMOOTH_FACTOR = 0.5;

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

            double cosineSimilarity = calculateCosineSimilarity(
                    predictedRedValueList,
                    sampleBallValues.subList(0, 6)
            );

            double euclideanWeight = calculateEuclideanWeight(
                    predictedRedValueList,
                    sampleBallValues.subList(0, 6)
            );

            double manhattanWeight = calculateManhattanWeight(
                    predictedRedValueList,
                    sampleBallValues.subList(0, 6)
            );

            double combinedWeight = (cosineSimilarity * 0.5 + euclideanWeight * 0.3 + manhattanWeight * 0.2);

            if (combinedWeight < 0) {
                combinedWeight = 0;
            }

            int sampleBlueValue = sampleBallValues.get(6);
            weightedBlueSum += combinedWeight * sampleBlueValue;
            totalWeight += combinedWeight;
        }

        if (totalWeight == 0) {
            return -1;
        }

        int predictedBlue = (int) Math.round(weightedBlueSum / totalWeight);
        return Math.max(34, Math.min(49, predictedBlue));
    }

    private double calculateCosineSimilarity(List<Integer> vector1, List<Integer> vector2) {
        if (vector1.size() != vector2.size()) {
            return 0;
        }

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (int i = 0; i < vector1.size(); i++) {
            double v1 = vector1.get(i);
            double v2 = vector2.get(i);

            dotProduct += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private double calculateEuclideanWeight(List<Integer> vector1, List<Integer> vector2) {
        if (vector1.size() != vector2.size()) {
            return 0;
        }

        double sumSquaredDifferences = 0;
        for (int i = 0; i < vector1.size(); i++) {
            double diff = vector1.get(i) - vector2.get(i);
            sumSquaredDifferences += diff * diff;
        }

        double distance = Math.sqrt(sumSquaredDifferences);
        return 1.0 / (distance + SMOOTH_FACTOR);
    }

    private double calculateManhattanWeight(List<Integer> vector1, List<Integer> vector2) {
        if (vector1.size() != vector2.size()) {
            return 0;
        }

        double sumDifferences = 0;
        for (int i = 0; i < vector1.size(); i++) {
            sumDifferences += Math.abs(vector1.get(i) - vector2.get(i));
        }

        return 1.0 / (sumDifferences + SMOOTH_FACTOR);
    }
}
