package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于激进衰减的相关性算法 - 预测蓝球
 * 采用更激进的衰减系数(0.88)，比DecayFrequencyRelevanceAlgorithm(0.95)更强调最近数据
 * 样本大小为55
 */
public class AggressiveDecayRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int SAMPLE_SIZE = 55;
    private static final double AGGRESSIVE_DECAY = 0.88;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        int startIdx = Math.max(0, sampleList.size() - SAMPLE_SIZE);
        double maxScore = 0;
        int bestBlueCandidate = BLUE_VIRTUAL_MIN;

        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            double score = 0;
            int decayCount = 0;

            // 从最后向前遍历，最近的样本权重更高
            for (int i = sampleList.size() - 1; i >= startIdx; i--) {
                VirtualDoubleColorBallItem sample = sampleList.get(i);
                List<Integer> ballValues = sample.getBallValueList();

                if (ballValues == null || ballValues.size() < 7) {
                    decayCount++;
                    continue;
                }

                int sampleBlue = ballValues.get(6);
                if (sampleBlue == candidateBlue) {
                    int sampleFifth = ballValues.get(4);
                    int sampleSixth = ballValues.get(5);

                    double fifth_similarity = 1.0 / (1.0 + Math.abs(predictedFifth - sampleFifth));
                    double sixth_similarity = 1.0 / (1.0 + Math.abs(predictedSixth - sampleSixth));
                    double similarity = (fifth_similarity + sixth_similarity) / 2.0;

                    // 激进衰减：权重快速下降
                    score += similarity * Math.pow(AGGRESSIVE_DECAY, decayCount);
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
