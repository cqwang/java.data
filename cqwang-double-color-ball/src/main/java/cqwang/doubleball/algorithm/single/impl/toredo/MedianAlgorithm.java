package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 中位数算法 - 中位数分布优化版
 * 优化策略：分布分析 + 相似度预测，提高鲁棒性
 */
public class MedianAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByDistribution(
                redBallDataDetail, redRange);
        int result2 = AlgorithmOptimizationUtils.calculateMedian(
                redBallDataDetail.getDataList(), 50);
        return result1 == result2 ? result1 : result2;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByDistribution(
                blueBallDataDetail, blueRange);
        int result2 = AlgorithmOptimizationUtils.calculateMedian(
                blueBallDataDetail.getDataList(), 50);
        return result1 == result2 ? result1 : result2;
    }
}
