package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于爆发频率的相关性算法 - 预测蓝球
 * 先关注最近12个样本的爆发值，如果没有则看最近50个样本的稳定值
 * 同时考虑预测值与样本前6位的相似度
 */
public class BurstFrequencyRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int BURST_WINDOW = 12;
    private static final int STABLE_WINDOW = 50;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        int burstStart = Math.max(0, sampleList.size() - BURST_WINDOW);
        int stableStart = Math.max(0, sampleList.size() - STABLE_WINDOW);

        // 第一步：检查爆发窗口中的最高分值
        double maxBurstScore = 0;
        int burstResult = BLUE_VIRTUAL_MIN;

        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            int frequency = 0;
            double totalSimilarity = 0;
            int matchCount = 0;

            for (int i = burstStart; i < sampleList.size(); i++) {
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

            if (matchCount > 0) {
                double avgSimilarity = totalSimilarity / matchCount;
                double score = frequency * 2.0 + avgSimilarity * 3.0;

                if (score > maxBurstScore) {
                    maxBurstScore = score;
                    burstResult = candidateBlue;
                }
            }
        }

        // 如果爆发窗口有结果，返回爆发结果
        if (maxBurstScore > 0) {
            return burstResult;
        }

        // 第二步：检查稳定窗口中的最高分值
        double maxStableScore = 0;
        int stableResult = BLUE_VIRTUAL_MIN;

        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            int frequency = 0;
            double totalSimilarity = 0;
            int matchCount = 0;

            for (int i = stableStart; i < sampleList.size(); i++) {
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

            if (matchCount > 0) {
                double avgSimilarity = totalSimilarity / matchCount;
                double score = frequency * 2.0 + avgSimilarity * 3.0;

                if (score > maxStableScore) {
                    maxStableScore = score;
                    stableResult = candidateBlue;
                }
            }
        }

        if (maxStableScore == 0) {
            return INVALID_RESULT;
        }

        return stableResult;
    }
}
