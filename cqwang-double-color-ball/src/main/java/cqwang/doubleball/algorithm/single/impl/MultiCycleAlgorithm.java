package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球多周期融合算法 - 综合多个周期的频率信息
 */
public class MultiCycleAlgorithm implements PredictionAlgorithm {
    private static final int CYCLE1 = 20;
    private static final int CYCLE2 = 45;
    private static final int CYCLE3 = 80;

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

        int start1 = Math.max(0, dataList.size() - CYCLE1);
        int start2 = Math.max(0, dataList.size() - CYCLE2);
        int start3 = Math.max(0, dataList.size() - CYCLE3);

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

            // 权重：近期权重更高 (6:3:1)
            double score = freq1 * 6.0 + (freq2 - freq1) * 3.0 + (freq3 - freq2) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
