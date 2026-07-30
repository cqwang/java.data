package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球终极频率算法 - 结合多个时间窗口的加权频率
 */
public class UltimateFrequencyAlgorithm implements PredictionAlgorithm {
    private static final int W1 = 12;   // 最近12次
    private static final int W2 = 28;   // 最近28次
    private static final int W3 = 52;   // 最近52次

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

        int start1 = Math.max(0, dataList.size() - W1);
        int start2 = Math.max(0, dataList.size() - W2);
        int start3 = Math.max(0, dataList.size() - W3);

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

            // 权重：近期权重更高 (7:3:1)
            double score = freq1 * 7.0 + (freq2 - freq1) * 3.0 + (freq3 - freq2) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
