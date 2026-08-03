package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球三阶段加权算法 - 极近期、近期、中期三个不同权重
 */
public class ThreePhaseWeightedAlgorithm implements PredictionAlgorithm {
    private static final int PHASE1_WINDOW = 15;  // 极近期
    private static final int PHASE2_WINDOW = 35;  // 近期
    private static final int PHASE3_WINDOW = 70;  // 中期

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

        int phase1Start = Math.max(0, dataList.size() - PHASE1_WINDOW);
        int phase2Start = Math.max(0, dataList.size() - PHASE2_WINDOW);
        int phase3Start = Math.max(0, dataList.size() - PHASE3_WINDOW);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq1 = 0, freq2 = 0, freq3 = 0;

            for (int j = phase1Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq1++;
            }

            for (int j = phase2Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq2++;
            }

            for (int j = phase3Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq3++;
            }

            // 三阶段权重：8:4:1
            double score = freq1 * 8.0 + freq2 * 4.0 + freq3 * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
