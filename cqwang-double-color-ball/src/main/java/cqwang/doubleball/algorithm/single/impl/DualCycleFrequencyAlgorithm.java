package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球双周期频率算法 - 结合短周期和长周期的频率分析
 */
public class DualCycleFrequencyAlgorithm implements PredictionAlgorithm {
    private static final int SHORT_CYCLE = 20;
    private static final int LONG_CYCLE = 60;

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

        int shortStart = Math.max(0, dataList.size() - SHORT_CYCLE);
        int longStart = Math.max(0, dataList.size() - LONG_CYCLE);

        double bestScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            // 计算短周期频率
            int shortFreq = 0;
            for (int j = shortStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    shortFreq++;
                }
            }

            // 计算长周期频率
            int longFreq = 0;
            for (int j = longStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    longFreq++;
                }
            }

            // 综合评分：短周期权重更高
            double score = shortFreq * 2.0 + longFreq * 0.5;

            if (score > bestScore) {
                bestScore = score;
                result = i;
            }
        }

        return result;
    }
}
