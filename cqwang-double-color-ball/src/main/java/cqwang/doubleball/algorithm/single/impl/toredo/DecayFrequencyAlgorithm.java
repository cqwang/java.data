package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球衰减频率算法 - 激进衰减优化版
 * 优化策略：激进衰减（0.88）+ 爆发检测，快速反应最近趋势
 */
public class DecayFrequencyAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 55, 0.88);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 55, 0.88);
    }
}
