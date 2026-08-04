package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 均值算法 - 加权平均优化版
 * 优化策略：使用加权平均替代简单平均，强调最近数据权重
 */
public class AverageAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        double weighted = AlgorithmOptimizationUtils.calculateWeightedAverage(
                redBallDataDetail.getDataList(), 40);
        return Math.max(redRange.getMinimum(),
                Math.min(redRange.getMaximum(), (int) Math.round(weighted)));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        double weighted = AlgorithmOptimizationUtils.calculateWeightedAverage(
                blueBallDataDetail.getDataList(), 40);
        return Math.max(blueRange.getMinimum(),
                Math.min(blueRange.getMaximum(), (int) Math.round(weighted)));
    }
}
