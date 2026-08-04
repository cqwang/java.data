package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于极端最近的相关性算法 - 预测蓝球
 * 只关注最近15个样本中最高频率的蓝球值
 * 强烈偏向最近的历史数据
 */
public class ExtremeRecentRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int EXTREME_WINDOW = 15;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        int startIdx = Math.max(0, sampleList.size() - EXTREME_WINDOW);
        double maxScore = 0;
        int bestBlueCandidate = BLUE_VIRTUAL_MIN;

        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            int frequency = 0;
            double totalSimilarity = 0;
            int matchCount = 0;

            for (int i = startIdx; i < sampleList.size(); i++) {
                VirtualDoubleColorBallItem sample = sampleList.get(i);
                List<Integer> ballValues = sample.getBallValueList();

                if (ballValues == null || ballValues.size() < 7) {
                    continue;
                }

                int sampleBlue = ballValues.get(6);
                if (sampleBlue == candidateBlue) {
                    frequency++;
                    matchCount++;

                    int sampleFifth = ballValues.get(4);
                    int sampleSixth = ballValues.get(5);

                    double fifth_similarity = 1.0 / (1.0 + Math.abs(predictedFifth - sampleFifth));
                    double sixth_similarity = 1.0 / (1.0 + Math.abs(predictedSixth - sampleSixth));
                    totalSimilarity += (fifth_similarity + sixth_similarity) / 2.0;
                }
            }

            double avgSimilarity = matchCount > 0 ? totalSimilarity / matchCount : 0;
            // 在极端窗口中，频率权重更高，因为数据本身就很少
            double score = frequency * 3.0 + avgSimilarity * 2.0;

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
