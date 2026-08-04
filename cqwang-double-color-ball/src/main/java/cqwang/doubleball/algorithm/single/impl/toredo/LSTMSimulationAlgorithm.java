package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * LSTM模拟算法 - 深度学习时序优化版
 * 优化策略：使用加权频率，强调时间衰减的重要性
 */
public class LSTMSimulationAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(
                redBallDataDetail, redRange, 80, 0.85);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByWeightedFrequency(
                blueBallDataDetail, blueRange, 80, 0.85);
    }
}
