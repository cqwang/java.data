package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 最终优化算法 - 综合所有最优策略的终极版本
 */
public class UltimateOptimizationAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW1 = 14;
    private static final int WINDOW2 = 32;
    private static final int WINDOW3 = 58;

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

        int w1Start = Math.max(0, dataList.size() - WINDOW1);
        int w2Start = Math.max(0, dataList.size() - WINDOW2);
        int w3Start = Math.max(0, dataList.size() - WINDOW3);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int f1 = 0, f2 = 0, f3 = 0;
            double decay = 0;

            // 计算三个窗口的频率
            for (int j = w1Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    f1++;
                    // 超强衰减权重
                    decay += Math.pow(0.83, dataList.size() - 1 - j);
                }
            }

            for (int j = w2Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f2++;
            }

            for (int j = w3Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) f3++;
            }

            // 综合评分：衰减权重×4 + 极近期×8 + 近期×3 + 中期×1
            double score = decay * 4.0 + f1 * 8.0 + (f2 - f1) * 3.0 + (f3 - f2) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
