package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 智能自适应算法 - 根据数据分布自动调整策略
 */
public class SmartAdaptiveAlgorithm implements PredictionAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictValue(blueBallDataDetail, blueRange);
    }

    private int predictValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        var freqMap = ballDataDetail.getDataFrequencyMap();

        // 计算统计信息
        int maxFreq = 0;
        double avgFreq = 0;
        int appearCount = 0;

        for (int freq : freqMap.values()) {
            maxFreq = Math.max(maxFreq, freq);
            avgFreq += freq;
            appearCount++;
        }

        if (appearCount > 0) {
            avgFreq /= appearCount;
        }

        // 根据分布情况选择策略
        // 如果有明显的热号，使用高频率优先
        double concentration = maxFreq / (avgFreq > 0 ? avgFreq : 1);

        if (concentration > 2.0) {
            // 高度集中，直接选择最高频
            return findMaxFrequency(freqMap, range);
        } else if (concentration > 1.5) {
            // 中等集中，使用近期加权
            return findRecentWeighted(dataList, range);
        } else {
            // 分散分布，使用多周期融合
            return findMultiCycleFused(dataList, range);
        }
    }

    private int findMaxFrequency(java.util.Map<Integer, Integer> freqMap, Range<Integer> range) {
        int maxFreq = 0;
        int result = range.getMinimum();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = freqMap.getOrDefault(i, 0);
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }
        return result;
    }

    private int findRecentWeighted(java.util.List<Integer> dataList, Range<Integer> range) {
        int windowSize = Math.max(20, dataList.size() / 3);
        int startIdx = Math.max(0, dataList.size() - windowSize);
        double maxWeight = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weight = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    weight += 1.0 + (double) (j - startIdx) / (dataList.size() - startIdx);
                }
            }
            if (weight > maxWeight) {
                maxWeight = weight;
                result = i;
            }
        }
        return result;
    }

    private int findMultiCycleFused(java.util.List<Integer> dataList, Range<Integer> range) {
        int cycle1 = Math.max(15, dataList.size() / 8);
        int cycle2 = Math.max(30, dataList.size() / 4);
        int cycle3 = Math.max(60, dataList.size() / 2);

        int start1 = Math.max(0, dataList.size() - cycle1);
        int start2 = Math.max(0, dataList.size() - cycle2);
        int start3 = Math.max(0, dataList.size() - cycle3);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq1 = 0, freq2 = 0, freq3 = 0;

            for (int j = start1; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq1++;
            }

            for (int j = start2; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq2++;
            }

            for (int j = start3; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq3++;
            }

            double score = freq1 * 8.0 + (freq2 - freq1) * 3.0 + (freq3 - freq2) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
