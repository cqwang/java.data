package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 指数平滑算法 - 基于指数加权的平滑预测
 */
public class ExponentialSmoothing5Algorithm implements PredictionAlgorithm {
    private static final double ALPHA = 0.5;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByExponentialSmoothing(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByExponentialSmoothing(blueBallDataDetail, blueRange);
    }

    private int predictByExponentialSmoothing(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        double result = dataList.get(0);
        for (int i = 1; i < dataList.size(); i++) {
            result = getAlpha() * dataList.get(i) + (1 - getAlpha()) * result;
        }

        int finalResult = (int) Math.round(result);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), finalResult));
    }

    protected double getAlpha(){
        return ALPHA;
    }
}
