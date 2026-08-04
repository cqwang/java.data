package cqwang.doubleball.algorithm.relevance.impl;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于间隔频率统计的相关性算法 - 预测蓝球
 * 通过分析样本中各个位置与第7位的间隔频率分布
 * 找出最常见的间隔模式，基于预测值来推断蓝球
 */
public class IntervalFrequencyRelevanceAlgorithm implements RelevanceAlgorithm {

    @Override
    public int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList) {
        if (sampleList == null || sampleList.isEmpty() || predictedRedValueList == null || predictedRedValueList.size() < 6) {
            return INVALID_RESULT;
        }

        int predictedFifth = predictedRedValueList.get(4);
        int predictedSixth = predictedRedValueList.get(5);

        // 统计样本中各位置与蓝球的间隔频率
        Map<Integer, Integer> gap5Frequency = new HashMap<>();
        Map<Integer, Integer> gap6Frequency = new HashMap<>();

        for (VirtualDoubleColorBallItem sample : sampleList) {
            List<Integer> sampleBallValues = sample.getBallValueList();
            if (sampleBallValues == null || sampleBallValues.size() < 7) {
                continue;
            }

            int sampleFifth = sampleBallValues.get(4);
            int sampleSixth = sampleBallValues.get(5);
            int sampleBlue = sampleBallValues.get(6);

            // 计算间隔，统计频率
            int gap5 = sampleBlue - sampleFifth;
            int gap6 = sampleBlue - sampleSixth;

            gap5Frequency.put(gap5, gap5Frequency.getOrDefault(gap5, 0) + 1);
            gap6Frequency.put(gap6, gap6Frequency.getOrDefault(gap6, 0) + 1);
        }

        if (gap5Frequency.isEmpty() || gap6Frequency.isEmpty()) {
            return INVALID_RESULT;
        }

        // 找出最高频率的间隔
        int mostFrequentGap5 = findMostFrequentGap(gap5Frequency);
        int mostFrequentGap6 = findMostFrequentGap(gap6Frequency);

        // 基于最常见的间隔来推断蓝球
        int blueEstimate1 = predictedFifth + mostFrequentGap5;
        int blueEstimate2 = predictedSixth + mostFrequentGap6;

        // 取两个估计值的平均
        int predictedBlue = (int) Math.round((blueEstimate1 + blueEstimate2) / 2.0);

        if (predictedBlue < BLUE_VIRTUAL_MIN || predictedBlue > BLUE_VIRTUAL_MAX) {
            return INVALID_RESULT;
        }

        return predictedBlue;
    }

    private int findMostFrequentGap(Map<Integer, Integer> gapFrequency) {
        int mostFrequentGap = 0;
        int maxFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : gapFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentGap = entry.getKey();
            }
        }

        return mostFrequentGap;
    }
}
