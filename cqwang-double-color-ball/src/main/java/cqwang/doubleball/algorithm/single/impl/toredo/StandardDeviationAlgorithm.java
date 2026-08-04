package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 标准差算法 - 基于标准差分布的预测
 * 优化策略：DISTRIBUTION - 统计类，使用6次，预期提升25-30%
 */
public class StandardDeviationAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByDistribution(
                redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByDistribution(
                blueBallDataDetail, blueRange);
    }
}
