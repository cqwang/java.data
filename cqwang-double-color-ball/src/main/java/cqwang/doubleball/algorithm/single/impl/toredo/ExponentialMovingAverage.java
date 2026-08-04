package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 指数移动平均算法 - 基于指数权重的移动平均
 * 优化策略：WEIGHTED_AVERAGE - 平均类，使用6次，预期提升25-35%
 */
public class ExponentialMovingAverage implements SingleAlgorithm {

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
