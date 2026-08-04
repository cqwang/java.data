package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 最大最小缩放算法 - 基于最大最小值的缩放预测
 * 优化策略：DISTRIBUTION - 分层分布预测
 */
public class MinMaxScalingAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByDistribution(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByDistribution(blueBallDataDetail, blueRange);
    }
}
