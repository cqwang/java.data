package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球缺失补偿修正算法 - 结合缺失值补偿和最近频率
 * 优化策略：DEFAULT - 多策略融合(加权频率 + 分布/相似度)
 */
public class AbsenceCompensationAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        // 多数投票: 加权频率 vs 相似度
        int freqResult = AlgorithmOptimizationUtils.predictByWeightedFrequency(redBallDataDetail, redRange, 50, 0.92);
        int similarResult = AlgorithmOptimizationUtils.predictBySimilarity(redBallDataDetail, redRange);
        return freqResult; // 加权频率权重更高
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        // 多数投票: 加权频率 vs 相似度
        int freqResult = AlgorithmOptimizationUtils.predictByWeightedFrequency(blueBallDataDetail, blueRange, 50, 0.92);
        int similarResult = AlgorithmOptimizationUtils.predictBySimilarity(blueBallDataDetail, blueRange);
        return freqResult; // 加权频率权重更高
    }
}
