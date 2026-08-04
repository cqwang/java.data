package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于混合优化的相关性算法 - 预测蓝球
 * 结合短期(22)、中期(50)、长期(90)三个窗口的数据
 * 对每个窗口计算分值，取加权平均
 */
public class HybridOptimizedRelevanceAlgorithm implements RelevanceAlgorithm {

    private static final int SHORT_WINDOW = 22;
    private static final int MEDIUM_WINDOW = 50;
    private static final int LONG_WINDOW = 90;

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        // 计算三个窗口的分数
        Map<Integer, Double> shortWindowScores = calculateWindowScores(predictedFifth, predictedSixth, sampleList, SHORT_WINDOW);
        Map<Integer, Double> mediumWindowScores = calculateWindowScores(predictedFifth, predictedSixth, sampleList, MEDIUM_WINDOW);
        Map<Integer, Double> longWindowScores = calculateWindowScores(predictedFifth, predictedSixth, sampleList, LONG_WINDOW);

        double maxHybridScore = 0;
        int bestBlueCandidate = BLUE_VIRTUAL_MIN;

        // 三个窗口的权重：短期50%，中期30%，长期20%
        for (int candidateBlue = BLUE_VIRTUAL_MIN; candidateBlue <= BLUE_VIRTUAL_MAX; candidateBlue++) {
            double shortScore = shortWindowScores.getOrDefault(candidateBlue, 0.0);
            double mediumScore = mediumWindowScores.getOrDefault(candidateBlue, 0.0);
            double longScore = longWindowScores.getOrDefault(candidateBlue, 0.0);

            double hybridScore = shortScore * 0.5 + mediumScore * 0.3 + longScore * 0.2;

            if (hybridScore > maxHybridScore) {
                maxHybridScore = hybridScore;
                bestBlueCandidate = candidateBlue;
            }
        }

        if (maxHybridScore == 0) {
            return INVALID_RESULT;
        }

        return bestBlueCandidate;
    }

    private Map<Integer, Double> calculateWindowScores(int predictedFifth, int predictedSixth,
                                                       List<VirtualDoubleColorBallItem> sampleList, int windowSize) {
        Map<Integer, Double> scores = new HashMap<>();
        int startIdx = Math.max(0, sampleList.size() - windowSize);

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

            if (matchCount > 0) {
                double avgSimilarity = totalSimilarity / matchCount;
                double score = frequency * 2.0 + avgSimilarity * 3.0;
                scores.put(candidateBlue, score);
            }
        }

        return scores;
    }
}
