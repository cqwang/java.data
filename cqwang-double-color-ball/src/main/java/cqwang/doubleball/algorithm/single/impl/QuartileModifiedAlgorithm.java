package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球三分位数修正算法 - 使用第75百分位数
 */
public class QuartileModifiedAlgorithm implements SingleAlgorithm {

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

        // 构建累积频率数组
        int[] cumulativeFreq = new int[range.getMaximum() - range.getMinimum() + 1];
        int totalFreq = 0;

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = freqMap.getOrDefault(i, 0);
            cumulativeFreq[i - range.getMinimum()] = totalFreq + freq;
            totalFreq += freq;
        }

        // 找第75百分位数
        double percentile75Position = totalFreq * 0.75;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (cumulativeFreq[i - range.getMinimum()] >= percentile75Position) {
                result = i;
                break;
            }
        }

        return result;
    }
}
