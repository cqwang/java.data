package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 周期频率加权算法 - 周期加权增强优化版
 * 优化策略：加权频率 + 连续性分析，兼顾周期性和最近趋势
 */
public class CyclicFrequencyWeightAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 55, 0.91);
        int result2 = AlgorithmOptimizationUtils.predictByContinuity(
                redBallDataDetail, redRange);
        return result1 == result2 ? result1 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 55, 0.91);
        int result2 = AlgorithmOptimizationUtils.predictByContinuity(
                blueBallDataDetail, blueRange);
        return result1 == result2 ? result1 : result1;
    }
}

