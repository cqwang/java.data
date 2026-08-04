package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 移动平均算法 - 基于移动平均的预测
 * 优化策略：WEIGHTED_AVERAGE - 加权平均优化
 */
public class MovingAverageAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        double weighted = AlgorithmOptimizationUtils.calculateWeightedAverage(
                redBallDataDetail.getDataList(), 45);
        return Math.max(redRange.getMinimum(),
                Math.min(redRange.getMaximum(), (int) Math.round(weighted)));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        double weighted = AlgorithmOptimizationUtils.calculateWeightedAverage(
                blueBallDataDetail.getDataList(), 45);
        return Math.max(blueRange.getMinimum(),
                Math.min(blueRange.getMaximum(), (int) Math.round(weighted)));
    }
}
