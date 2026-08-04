package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.List;

/**
 * 基于连续出现模式的相关性算法 - 预测蓝球
 * 在最近样本中寻找连续出现的蓝球值
 * 同时考虑该蓝球值时预测值的前6位与样本的相似度
 */
public class ContinuousPatternRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int RECENT_WINDOW = 50;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        int startIdx = Math.max(0, sampleList.size() - RECENT_WINDOW);
        double maxScore = 0;
        int bestBlueCandidate = BLUE_VIRTUAL_MIN;

        // 遍历所有可能的蓝球值
        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            int totalFrequency = 0;
            int consecutiveCount = 0;
            int maxConsecutive = 0;
            double totalSimilarity = 0;
            int matchCount = 0;

            boolean lastWasTarget = false;

            for (int i = startIdx; i < sampleList.size(); i++) {
                VirtualDoubleColorBallItem sample = sampleList.get(i);
                List<Integer> ballValues = sample.getBallValueList();

                if (ballValues == null || ballValues.size() < 7) {
                    lastWasTarget = false;
                    continue;
                }

                int sampleBlue = ballValues.get(6);

                if (sampleBlue == candidateBlue) {
                    totalFrequency++;
                    matchCount++;

                    // 计算前6位的相似度
                    int sampleFifth = ballValues.get(4);
                    int sampleSixth = ballValues.get(5);

                    double fifth_similarity = 1.0 / (1.0 + Math.abs(predictedFifth - sampleFifth));
                    double sixth_similarity = 1.0 / (1.0 + Math.abs(predictedSixth - sampleSixth));
                    totalSimilarity += (fifth_similarity + sixth_similarity) / 2.0;

                    // 记录连续出现
                    if (lastWasTarget) {
                        consecutiveCount++;
                    } else {
                        maxConsecutive = Math.max(maxConsecutive, consecutiveCount);
                        consecutiveCount = 1;
                    }
                    lastWasTarget = true;
                } else {
                    lastWasTarget = false;
                }
            }

            maxConsecutive = Math.max(maxConsecutive, consecutiveCount);

            // 计算评分：总频率 + 连续出现次数权重 + 相似度权重
            double avgSimilarity = matchCount > 0 ? totalSimilarity / matchCount : 0;
            double score = totalFrequency * 2.0 + maxConsecutive * 3.0 + avgSimilarity * 5.0;

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
