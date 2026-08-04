package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球连续出现算法 - 连续性模式识别优化版
 * 优化策略：连续性分析 + 加权频率，强化连续性识别
 */
public class ContinuousAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByContinuity(
                redBallDataDetail, redRange);
        int result2 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 50, 0.92);
        return result1 == result2 ? result1 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByContinuity(
                blueBallDataDetail, blueRange);
        int result2 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 50, 0.92);
        return result1 == result2 ? result1 : result1;
    }
}
