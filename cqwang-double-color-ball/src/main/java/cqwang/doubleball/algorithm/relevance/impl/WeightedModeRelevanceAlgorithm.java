package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于加权众数的相关性算法 - 预测蓝球
 * 在最近80个样本中，使用加权方式计算众数
 * 最近出现的频率最高值权重更高
 */
public class WeightedModeRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int SAMPLE_SIZE = 80;

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
            double weightedFrequency = 0;
            double totalSimilarity = 0;
            int matchCount = 0;

            // 遍历样本，越近的样本权重越高
            for (int i = startIdx; i < sampleList.size(); i++) {
                VirtualDoubleColorBallItem sample = sampleList.get(i);
                List<Integer> ballValues = sample.getBallValueList();

                if (ballValues == null || ballValues.size() < 7) {
                    continue;
                }

                int sampleBlue = ballValues.get(6);
                if (sampleBlue == candidateBlue) {
                    // 位置权重：越近越高
                    double positionWeight = 1.0 + (double) (i - startIdx) / SAMPLE_SIZE;
                    weightedFrequency += positionWeight;
                    matchCount++;

                    int sampleFifth = ballValues.get(4);
                    int sampleSixth = ballValues.get(5);

                    double fifth_similarity = 1.0 / (1.0 + Math.abs(predictedFifth - sampleFifth));
                    double sixth_similarity = 1.0 / (1.0 + Math.abs(predictedSixth - sampleSixth));
                    totalSimilarity += (fifth_similarity + sixth_similarity) / 2.0;
                }
            }

            double avgSimilarity = matchCount > 0 ? totalSimilarity / matchCount : 0;
            double score = weightedFrequency * 2.0 + avgSimilarity * 3.0;

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
