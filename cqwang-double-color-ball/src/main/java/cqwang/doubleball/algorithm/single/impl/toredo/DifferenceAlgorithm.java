package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 差分值算法 - 基于差分序列的频率分析
 * 优化策略：WEIGHTED_FREQUENCY - 频率类，使用10次加权，预期提升30-40%
 */
public class DifferenceAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 50, 0.92);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 50, 0.92);
    }
}
