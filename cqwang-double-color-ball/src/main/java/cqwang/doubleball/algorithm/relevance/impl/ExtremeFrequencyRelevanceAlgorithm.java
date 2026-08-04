package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于极限频率的相关性算法 - 预测蓝球
 * 关注最近50次样本中最高频率的蓝球值
 * 同时考虑该蓝球值时预测值与样本前6位的相似度
 */
public class ExtremeFrequencyRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int EXTREME_WINDOW = 50;

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

        // 遍历所有可能的蓝球值
        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            int frequency = 0;
            double totalSimilarity = 0;
            int matchCount = 0;

            // 在极限窗口内统计频率
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

                    // 计算前6位的相似度
                    int sampleFifth = ballValues.get(4);
                    int sampleSixth = ballValues.get(5);

                    double fifth_similarity = 1.0 / (1.0 + Math.abs(predictedFifth - sampleFifth));
                    double sixth_similarity = 1.0 / (1.0 + Math.abs(predictedSixth - sampleSixth));
                    totalSimilarity += (fifth_similarity + sixth_similarity) / 2.0;
                }
            }

            // 如果极限窗口内没有找到，检查全局频率
            if (frequency == 0) {
                for (VirtualDoubleColorBallItem sample : sampleList) {
                    List<Integer> ballValues = sample.getBallValueList();
                    if (ballValues != null && ballValues.size() >= 7 && ballValues.get(6) == candidateBlue) {
                        frequency++;
                    }
                }
            }

            // 计算评分：频率 + 相似度权重
            double avgSimilarity = matchCount > 0 ? totalSimilarity / matchCount : 0;
            double score = frequency * 2.0 + avgSimilarity * 3.0;

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
