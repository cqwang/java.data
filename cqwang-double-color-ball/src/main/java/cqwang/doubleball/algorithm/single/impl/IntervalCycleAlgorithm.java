package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球间隔周期算法 - 基于历史出现的间隔周期预测
 */
public class IntervalCycleAlgorithm implements SingleAlgorithm {

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

        // 计算每个值出现的间隔
        int result = range.getMinimum();
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            // 找出该值最后出现的位置
            int lastIndex = -1;
            for (int j = dataList.size() - 1; j >= 0; j--) {
                if (dataList.get(j) == i) {
                    lastIndex = j;
                    break;
                }
            }

            if (lastIndex == -1) {
                // 未出现过的值，予以较低的评分
                continue;
            }

            // 计算出现频次和间隔相关性
            int frequency = 0;
            long intervalSum = 0;
            int lastPos = lastIndex;

            for (int j = lastIndex - 1; j >= 0; j--) {
                if (dataList.get(j) == i) {
                    frequency++;
                    intervalSum += (lastPos - j);
                    lastPos = j;
                }
            }

            if (frequency > 0) {
                double avgInterval = (double) intervalSum / frequency;
                double currentInterval = dataList.size() - lastIndex;
                // 评分：频次 * 区间稳定性系数
                double score = frequency * (1.0 / (1.0 + Math.abs(currentInterval - avgInterval)));
                if (score > bestScore) {
                    bestScore = score;
                    result = i;
                }
            }
        }

        return result;
    }
}
