package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 随机森林模拟算法 - 分布式优化版
 * 优化策略：使用分布优化，融合多时间窗口的预测
 */
public class RandomForestSimulationAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return AlgorithmOptimizationUtils.predictByDistribution(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return AlgorithmOptimizationUtils.predictByDistribution(blueBallDataDetail, blueRange);
    }
}
