package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 四段加权算法 - 四个不同时间段的加权融合
 */
public class FourSegmentWeightedAlgorithm implements PredictionAlgorithm {
    private static final int SEG1 = 10;
    private static final int SEG2 = 25;
    private static final int SEG3 = 50;
    private static final int SEG4 = 85;

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

        int seg1Start = Math.max(0, dataList.size() - SEG1);
        int seg2Start = Math.max(0, dataList.size() - SEG2);
        int seg3Start = Math.max(0, dataList.size() - SEG3);
        int seg4Start = Math.max(0, dataList.size() - SEG4);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq1 = 0, freq2 = 0, freq3 = 0, freq4 = 0;

            for (int j = seg1Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq1++;
            }

            for (int j = seg2Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq2++;
            }

            for (int j = seg3Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq3++;
            }

            for (int j = seg4Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq4++;
            }

            // 四段权重：12:6:2:1
            double score = freq1 * 12.0 + (freq2 - freq1) * 6.0 + (freq3 - freq2) * 2.0 + (freq4 - freq3) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
