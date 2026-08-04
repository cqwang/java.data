package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于间隔特征的相关性算法 - 预测蓝球
 * 通过分析样本中第5位与第7位的间隔、第6位与第7位的间隔等特征
 * 基于预测值的第5位、第6位与历史样本的间隔分布特征来预测第7位
 */
public class IntervalGapRelevanceAlgorithm implements RelevanceAlgorithm {

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        double gap5WeightedSum = 0;
        double gap6WeightedSum = 0;
        double totalWeight = 0;

        for (VirtualDoubleColorBallItem sample : sampleList) {
            List<Integer> sampleBallValues = sample.getBallValueList();
            if (sampleBallValues == null || sampleBallValues.size() < 7) {
                continue;
            }

            int sampleFifth = sampleBallValues.get(4);
            int sampleSixth = sampleBallValues.get(5);
            int sampleBlue = sampleBallValues.get(6);

            // 计算第5位与蓝球的间隔
            int gap5Sample = Math.abs(sampleBlue - sampleFifth);
            // 计算第6位与蓝球的间隔
            int gap6Sample = Math.abs(sampleBlue - sampleSixth);

            // 计算预测值的第5位、第6位与样本对应位置的相似度权重
            double gap5Similarity = calculateGapSimilarity(predictedFifth, sampleFifth, gap5Sample);
            double gap6Similarity = calculateGapSimilarity(predictedSixth, sampleSixth, gap6Sample);

            // 综合权重
            double combinedWeight = (gap5Similarity + gap6Similarity) / 2.0;

            if (combinedWeight > 0) {
                gap5WeightedSum += gap5Sample * combinedWeight;
                gap6WeightedSum += gap6Sample * combinedWeight;
                totalWeight += combinedWeight;
            }
        }

        if (totalWeight == 0) {
            return INVALID_RESULT;
        }

        // 基于平均间隔来推断蓝球值
        double avgGap5 = gap5WeightedSum / totalWeight;
        double avgGap6 = gap6WeightedSum / totalWeight;

        // 使用第5位、第6位和平均间隔来估计蓝球
        int blueEstimate1 = (int) Math.round(predictedFifth + avgGap5);
        int blueEstimate2 = (int) Math.round(predictedSixth + avgGap6);

        // 取两个估计值的平均
        int predictedBlue = (int) Math.round((blueEstimate1 + blueEstimate2) / 2.0);

        if (predictedBlue < BLUE_VIRTUAL_MIN || predictedBlue > BLUE_VIRTUAL_MAX) {
            return INVALID_RESULT;
        }

        return predictedBlue;
    }

    /**
     * 计算间隔相似度
     * 基于预测位置与样本位置的接近程度来评估间隔特征的相似性
     */
    private double calculateGapSimilarity(int predictedPosition, int samplePosition, int sampleGap) {
        int positionDiff = Math.abs(predictedPosition - samplePosition);

        if (positionDiff == 0) {
            return 1.0;
        }

        if (positionDiff <= 2) {
            return 0.8;
        }

        if (positionDiff <= 5) {
            return 0.5;
        }

        if (positionDiff <= 8) {
            return 0.2;
        }

        return 0;
    }
}
