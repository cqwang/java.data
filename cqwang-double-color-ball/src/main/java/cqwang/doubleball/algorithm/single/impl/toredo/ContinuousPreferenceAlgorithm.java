package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球连续出现偏好算法 - 连续偏好优化版
 * 优化策略：连续性分析 + 相似度预测，重视连续性
 */
public class ContinuousPreferenceAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictByContinuity(
                redBallDataDetail, redRange);
        int result2 = AlgorithmOptimizationUtils.predictBySimilarity(
                redBallDataDetail, redRange);
        return result1 == result2 ? result1 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictByContinuity(
                blueBallDataDetail, blueRange);
        int result2 = AlgorithmOptimizationUtils.predictBySimilarity(
                blueBallDataDetail, blueRange);
        return result1 == result2 ? result1 : result1;
    }
}
