package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于余弦相似度的相关性算法 - 预测蓝球
 * 通过计算预测的前6位与样本前6位的余弦相似度，加权聚合样本的第7位值
 */
public class CosineSimilarityRelevanceAlgorithm implements RelevanceAlgorithm {

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty()) {
            return INVALID_RESULT;
        }

        double totalWeight = 0;
        double weightedBlueSum = 0;

        for (VirtualDoubleColorBallItem sample : sampleList) {
            List<Integer> sampleBallValues = sample.getBallValueList();
            if (sampleBallValues == null || sampleBallValues.size() < 7) {
                continue;
            }

            double similarity = calculateCosineSimilarity(
                    predictedRedValueList,
                    sampleBallValues.subList(0, 6)
            );

            if (similarity < 0) {
                similarity = 0;
            }

            int sampleBlueValue = sampleBallValues.get(6);
            weightedBlueSum += similarity * sampleBlueValue;
            totalWeight += similarity;
        }

        if (totalWeight == 0) {
            return INVALID_RESULT;
        }

        int predictedBlue = (int) Math.round(weightedBlueSum / totalWeight);

        if (predictedBlue < BLUE_VIRTUAL_MIN || predictedBlue > BLUE_VIRTUAL_MAX) {
            return INVALID_RESULT;
        }

        return predictedBlue;
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
}
