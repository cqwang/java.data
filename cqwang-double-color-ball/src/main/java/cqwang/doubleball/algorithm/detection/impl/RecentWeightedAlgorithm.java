package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球近期加权算法 - 基于最近50次数据的加权频率
 */
public class RecentWeightedAlgorithm implements PredictionAlgorithm {
    private static final int RECENT_WINDOW = 50;

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

        int startIdx = Math.max(0, dataList.size() - RECENT_WINDOW);
        double maxWeight = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weight = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    // 距离越近权重越高
                    weight += 1.0 + (double) (j - startIdx) / (dataList.size() - startIdx);
                }
            }

            if (weight > maxWeight) {
                maxWeight = weight;
                result = i;
            }
        }

        return result;
    }
}
