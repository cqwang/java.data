package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 五层递进算法 - 五个不同时间段的递进加权
 */
public class FiveLayerProgressiveAlgorithm implements PredictionAlgorithm {
    private static final int L1 = 8;
    private static final int L2 = 20;
    private static final int L3 = 40;
    private static final int L4 = 65;
    private static final int L5 = 100;

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

        int l1Start = Math.max(0, dataList.size() - L1);
        int l2Start = Math.max(0, dataList.size() - L2);
        int l3Start = Math.max(0, dataList.size() - L3);
        int l4Start = Math.max(0, dataList.size() - L4);
        int l5Start = Math.max(0, dataList.size() - L5);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int f1 = 0, f2 = 0, f3 = 0, f4 = 0, f5 = 0;

            for (int j = l1Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f1++;
            }

            for (int j = l2Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f2++;
            }

            for (int j = l3Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f3++;
            }

            for (int j = l4Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f4++;
            }

            for (int j = l5Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f5++;
            }

            // 五层权重：16:8:4:2:1
            double score = f1 * 16.0 + (f2 - f1) * 8.0 + (f3 - f2) * 4.0
                    + (f4 - f3) * 2.0 + (f5 - f4) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
