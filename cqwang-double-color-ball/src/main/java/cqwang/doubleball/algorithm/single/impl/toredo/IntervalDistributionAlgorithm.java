package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 区间分布算法 - 区间分布优化版
 * 优化策略：分布分析 + 加权平均，兼顾区间中点和频率权重
 */
public class IntervalDistributionAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByDistribution(
                redBallDataDetail, redRange);
        int result2 = (int)AlgorithmOptimizationUtils.calculateWeightedAverage(
                redBallDataDetail.getDataList(), 60);
        return result1 == result2 ? result2 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByDistribution(
                blueBallDataDetail, blueRange);
        int result2 = (int)AlgorithmOptimizationUtils.calculateWeightedAverage(
                blueBallDataDetail.getDataList(), 60);
        return result1 == result2 ? result2 : result1;
    }
}
