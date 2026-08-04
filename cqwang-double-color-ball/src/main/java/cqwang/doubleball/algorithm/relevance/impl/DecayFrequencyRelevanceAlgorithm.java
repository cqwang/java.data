package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于衰减频率的相关性算法 - 预测蓝球
 * 按照球值范围遍历，计算每个候选蓝球值基于样本的衰减权重
 * 最近出现的样本权重更高
 */
public class DecayFrequencyRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final double DECAY_FACTOR = 0.95;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        double maxScore = 0;
        int bestBlueCandidate = BLUE_VIRTUAL_MIN;

        // 遍历所有可能的蓝球值
        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            double score = 0;
            int decayCount = 0;

            // 从最后向前遍历样本（最近的样本权重更高）
            for (int i = sampleList.size() - 1; i >= 0; i--) {
                VirtualDoubleColorBallItem sample = sampleList.get(i);
                List<Integer> ballValues = sample.getBallValueList();

                if (ballValues == null || ballValues.size() < 7) {
                    continue;
                }

                int sampleFifth = ballValues.get(4);
                int sampleSixth = ballValues.get(5);
                int sampleBlue = ballValues.get(6);

                // 如果样本蓝球值与候选值相同
                if (sampleBlue == candidateBlue) {
                    // 计算第5位和第6位的相似度
                    double fifth_similarity = 1.0 / (1.0 + Math.abs(predictedFifth - sampleFifth));
                    double sixth_similarity = 1.0 / (1.0 + Math.abs(predictedSixth - sampleSixth));
                    double similarity = (fifth_similarity + sixth_similarity) / 2.0;

                    // 衰减权重
                    score += similarity * Math.pow(DECAY_FACTOR, decayCount);
                }

                decayCount++;
            }

            if (score > maxScore) {
                maxScore = score;
                bestBlueCandidate = candidateBlue;
            }
        }

        if (maxScore == 0) {
            return INVALID_RESULT;
        }

        return bestBlueCandidate;
    }
}
