package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 标准差加权算法 - 基于方差分布进行预测
 * 优化策略：WEIGHTED_AVERAGE - 加权平均预测
 */
public class VarianceWeightedAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        double avg = AlgorithmOptimizationUtils.calculateWeightedAverage(redBallDataDetail.getDataList(), 45);
        return Math.max(redRange.getMinimum(), Math.min(redRange.getMaximum(), (int) Math.round(avg)));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        double avg = AlgorithmOptimizationUtils.calculateWeightedAverage(blueBallDataDetail.getDataList(), 45);
        return Math.max(blueRange.getMinimum(), Math.min(blueRange.getMaximum(), (int) Math.round(avg)));
    }
}
