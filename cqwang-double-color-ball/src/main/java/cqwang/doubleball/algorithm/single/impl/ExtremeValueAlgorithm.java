package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 极值优先算法 - 优先选择数据范围内的极值
 */
public class ExtremeValueAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 35;

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

        int startIdx = Math.max(0, dataList.size() - WINDOW_SIZE);

        // 统计窗口内的最小值和最大值
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        int midPoint = (range.getMinimum() + range.getMaximum()) / 2;

        for (int j = startIdx; j < dataList.size(); j++) {
            minVal = Math.min(minVal, dataList.get(j));
            maxVal = Math.max(maxVal, dataList.get(j));
        }

        if (minVal == Integer.MAX_VALUE) {
            return midPoint;
        }

        // 计算每个值的得分
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                }
            }

            if (freq == 0) continue;

            // 靠近极值的数据得分更高
            int distToMin = Math.abs(i - minVal);
            int distToMax = Math.abs(i - maxVal);
            int distToMid = Math.abs(i - midPoint);
            double distWeight = 1.0 / (1.0 + Math.min(Math.min(distToMin, distToMax), distToMid) * 0.1);

            double score = freq * (2.0 + distWeight);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
