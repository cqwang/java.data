package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 周期移动平均算法 - 基于相同周期位置的平均值
 * 优化策略：WEIGHTED_FREQUENCY - 加权频率优化(window=50, decay=0.92)
 */
public class SeasonalMovingAverage206Algorithm extends SeasonalMovingAverage52Algorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(redBallDataDetail, redRange, 50, 0.92);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(blueBallDataDetail, blueRange, 50, 0.92);
    }
}
