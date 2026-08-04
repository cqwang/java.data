package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 周期频率算法 - 周期频率增强优化版
 * 优化策略：加权频率（强调周期性）+ 分布分析
 */
public class CyclicFrequencyAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 60, 0.90);
        int result2 = AlgorithmOptimizationUtils.predictByDistribution(
                redBallDataDetail, redRange);
        return result1 == result2 ? result1 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 60, 0.90);
        int result2 = AlgorithmOptimizationUtils.predictByDistribution(
                blueBallDataDetail, blueRange);
        return result1 == result2 ? result1 : result1;
    }
}
