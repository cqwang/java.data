package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 高斯混合模型算法 - 基于概率混合分布的预测
 */
public class GaussianMixtureAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByGaussianMixture(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByGaussianMixture(blueBallDataDetail, blueRange);
    }

    private int predictByGaussianMixture(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 计算均值和标准差
        double mean = dataList.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double variance = dataList.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .average().orElse(1);
        double stdDev = Math.sqrt(variance);

        // 使用高斯分布的期望值
        int result = (int) Math.round(mean);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
