package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * XGBoost模拟算法 - 基于梯度提升的预测
 */
public class XGBoostSimulationAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByGradientBoosting(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByGradientBoosting(blueBallDataDetail, blueRange);
    }

    private int predictByGradientBoosting(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.size() < 3) {
            return range.getMinimum();
        }

        // 多阶段梯度预测
        double learningRate = 0.1;
        double prediction = dataList.stream().mapToDouble(Integer::doubleValue).average().orElse(0);

        for (int i = 0; i < 3; i++) {
            int lastIdx = Math.max(0, dataList.size() - (i + 1));
            double residual = dataList.get(lastIdx) - prediction;
            prediction += learningRate * residual;
        }

        int result = (int) Math.round(prediction);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
