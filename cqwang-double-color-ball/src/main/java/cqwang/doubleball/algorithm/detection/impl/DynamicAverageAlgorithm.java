package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球动态平均算法 - 基于动态加权平均值
 */
public class DynamicAverageAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 40;

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

        double weightedSum = 0;
        double totalWeight = 0;

        // 越靠后的数据权重越高
        for (int j = startIdx; j < dataList.size(); j++) {
            double weight = 1.0 + (double) (j - startIdx) / (dataList.size() - startIdx) * 2.0;
            weightedSum += dataList.get(j) * weight;
            totalWeight += weight;
        }

        int result = Math.round((float) (weightedSum / totalWeight));
        result = Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));

        return result;
    }
}
