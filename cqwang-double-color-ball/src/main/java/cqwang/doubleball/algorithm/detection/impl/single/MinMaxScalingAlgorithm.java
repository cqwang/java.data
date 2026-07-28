package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 最大最小缩放算法 - 基于最大最小值的缩放预测
 */
public class MinMaxScalingAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByMinMaxScaling(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByMinMaxScaling(blueBallDataDetail, blueRange);
    }

    private int predictByMinMaxScaling(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int maxVal = dataList.stream().mapToInt(Integer::intValue).max().orElse(range.getMaximum());
        int minVal = dataList.stream().mapToInt(Integer::intValue).min().orElse(range.getMinimum());

        if (maxVal == minVal) {
            return Math.max(range.getMinimum(), Math.min(range.getMaximum(), maxVal));
        }

        double normalized = 0.5;
        int result = (int) Math.round(minVal + normalized * (maxVal - minVal));
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
