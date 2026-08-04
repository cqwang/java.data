package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球双周期频率算法 - 结合短周期和长周期的频率分析
 * 优化策略：WEIGHTED_FREQUENCY - 加权频率优化(window=50, decay=0.92)
 */
public class DualCycleFrequencyAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(redBallDataDetail, redRange, 50, 0.92);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(blueBallDataDetail, blueRange, 50, 0.92);
    }
}
