package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于Pearson相关系数的相关性算法 - 预测蓝球
 * 通过计算预测的前6位与样本前6位的Pearson相关系数，加权聚合样本的第7位值
 */
public class PearsonCorrelationRelevanceAlgorithm implements RelevanceAlgorithm {

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

            double correlation = calculatePearsonCorrelation(
                    predictedRedValueList,
                    sampleBallValues.subList(0, 6)
            );

            if (correlation < 0) {
                correlation = 0;
            }

            int sampleBlueValue = sampleBallValues.get(6);
            weightedBlueSum += correlation * sampleBlueValue;
            totalWeight += correlation;
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

    private double calculatePearsonCorrelation(List<Integer> vector1, List<Integer> vector2) {
        if (vector1.size() != vector2.size() || vector1.size() == 0) {
            return 0;
        }

        double mean1 = calculateMean(vector1);
        double mean2 = calculateMean(vector2);

        double covariance = 0;
        double stdDev1 = 0;
        double stdDev2 = 0;

        for (int i = 0; i < vector1.size(); i++) {
            double diff1 = vector1.get(i) - mean1;
            double diff2 = vector2.get(i) - mean2;

            covariance += diff1 * diff2;
            stdDev1 += diff1 * diff1;
            stdDev2 += diff2 * diff2;
        }

        if (stdDev1 == 0 || stdDev2 == 0) {
            return 0;
        }

        return covariance / (Math.sqrt(stdDev1) * Math.sqrt(stdDev2));
    }

    private double calculateMean(List<Integer> vector) {
        double sum = 0;
        for (int value : vector) {
            sum += value;
        }
        return sum / vector.size();
    }
}
