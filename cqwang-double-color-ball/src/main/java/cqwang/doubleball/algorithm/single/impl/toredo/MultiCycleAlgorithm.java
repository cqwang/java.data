package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球多周期融合算法 - 综合多个周期的频率信息
 * 优化策略：WEIGHTED_FREQUENCY - 加权频率优化(window=50, decay=0.92)
 */
public class MultiCycleAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(redBallDataDetail, redRange, 50, 0.92);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(blueBallDataDetail, blueRange, 50, 0.92);
    }
}
