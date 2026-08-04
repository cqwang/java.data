package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 余弦相似度算法 - 相似度增强优化版
 * 优化策略：相似度预测 + 加权频率，多角度分析
 */
public class CosineSimilarityAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        int result1 = AlgorithmOptimizationUtils.predictBySimilarity(
                redBallDataDetail, redRange);
        int result2 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 50, 0.92);
        return result1 == result2 ? result1 : result1;
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        int result1 = AlgorithmOptimizationUtils.predictBySimilarity(
                blueBallDataDetail, blueRange);
        int result2 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 50, 0.92);
        return result1 == result2 ? result1 : result1;
    }
}
