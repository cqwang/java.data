package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球高频冷号混合算法 - 优先选择高频数字，次选冷号
 */
public class HotColdMixedAlgorithm implements PredictionAlgorithm {
    private static final double HOT_RATIO = 0.6; // 60% 选择热号

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

        // 统计频率
        int maxFreq = 0;
        int totalFreq = 0;
        for (int freq : freqMap.values()) {
            maxFreq = Math.max(maxFreq, freq);
            totalFreq += freq;
        }

        // 定义热号阈值（频率高于平均值1.5倍）
        double avgFreq = (double) totalFreq / (range.getMaximum() - range.getMinimum() + 1);
        double hotThreshold = avgFreq * 1.5;

        // 分离热号和冷号
        int bestHot = range.getMinimum();
        int bestCold = range.getMinimum();
        int hotMaxFreq = 0;
        int coldMinFreq = Integer.MAX_VALUE;

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = freqMap.getOrDefault(i, 0);
            if (freq >= hotThreshold) {
                // 热号
                if (freq > hotMaxFreq) {
                    hotMaxFreq = freq;
                    bestHot = i;
                }
            } else {
                // 冷号，选择最接近热号频率的
                if (freq < coldMinFreq && freq > 0) {
                    coldMinFreq = freq;
                    bestCold = i;
                }
            }
        }

        // 根据概率选择
        if (hotMaxFreq > 0) {
            double probability = (double) hotMaxFreq / totalFreq;
            if (probability > HOT_RATIO) {
                return bestHot;
            }
        }

        // 如果没有找到合适的冷号，返回热号
        if (coldMinFreq == Integer.MAX_VALUE) {
            return bestHot;
        }

        return bestCold;
    }
}
