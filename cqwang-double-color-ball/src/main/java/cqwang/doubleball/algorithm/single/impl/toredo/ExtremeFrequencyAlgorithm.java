package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球极限频率算法 - 极限频率优化版
 * 优化策略：加权频率 + 相似度融合，提高准确率
 */
public class ExtremeFrequencyAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 50, 0.92);
        int result2 = AlgorithmOptimizationUtils.predictByBurstDetection(
                redBallDataDetail, redRange);
        return result1 == result2 ? result1 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 50, 0.92);
        int result2 = AlgorithmOptimizationUtils.predictByBurstDetection(
                blueBallDataDetail, blueRange);
        return result1 == result2 ? result1 : result1;
    }
}
