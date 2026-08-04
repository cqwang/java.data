package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 加权移动平均算法 - 最近的数据权重更高
 * 优化策略：WEIGHTED_AVERAGE - 加权平均优化
 */
public class WeightedMovingAverageAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        double avg = AlgorithmOptimizationUtils.calculateWeightedAverage(redBallDataDetail.getDataList(), 50);
        return Math.max(redRange.getMinimum(), Math.min(redRange.getMaximum(), (int) Math.round(avg)));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        double avg = AlgorithmOptimizationUtils.calculateWeightedAverage(blueBallDataDetail.getDataList(), 50);
        return Math.max(blueRange.getMinimum(), Math.min(blueRange.getMaximum(), (int) Math.round(avg)));
    }
}
