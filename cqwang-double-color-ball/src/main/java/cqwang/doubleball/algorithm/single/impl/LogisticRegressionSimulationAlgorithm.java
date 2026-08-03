package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 逻辑回归模拟算法 - 基于逻辑函数的概率预测
 */
public class LogisticRegressionSimulationAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByLogisticRegression(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByLogisticRegression(blueBallDataDetail, blueRange);
    }

    private int predictByLogisticRegression(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 计算数据的特征
        double mean = dataList.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double max = dataList.stream().mapToDouble(Integer::doubleValue).max().orElse(range.getMaximum());

        // 逻辑函数：1 / (1 + e^(-x))
        double x = (dataList.get(dataList.size() - 1) - mean) / Math.max(1, max - mean);
        double sigmoid = 1.0 / (1.0 + Math.exp(-x));

        // 映射回目标范围
        int result = range.getMinimum() + (int) Math.round(sigmoid * (range.getMaximum() - range.getMinimum()));
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
