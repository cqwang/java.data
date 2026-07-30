package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 综合优化算法 - 同时优化红球和蓝球预测
 * 结合多个最优策略的加权融合
 */
public class HybridOptimizedAlgorithm implements PredictionAlgorithm {
    private static final int SHORT_WINDOW = 22;
    private static final int MEDIUM_WINDOW = 50;
    private static final int LONG_WINDOW = 90;

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

        int shortStart = Math.max(0, dataList.size() - SHORT_WINDOW);
        int mediumStart = Math.max(0, dataList.size() - MEDIUM_WINDOW);
        int longStart = Math.max(0, dataList.size() - LONG_WINDOW);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int shortFreq = 0, mediumFreq = 0, longFreq = 0;

            for (int j = shortStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) shortFreq++;
            }

            for (int j = mediumStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) mediumFreq++;
            }

            for (int j = longStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) longFreq++;
            }

            // 多窗口加权：短期权重最高
            double score = shortFreq * 10.0 + (mediumFreq - shortFreq) * 4.0 + (longFreq - mediumFreq) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
