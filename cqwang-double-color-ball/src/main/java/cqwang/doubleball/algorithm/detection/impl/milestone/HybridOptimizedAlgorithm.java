package cqwang.doubleball.algorithm.detection.impl.milestone;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

/**
 * 综合优化算法 - 同时优化红球和蓝球预测
 * 结合多个最优策略的加权融合
 */
public class HybridOptimizedAlgorithm implements PredictionAlgorithm {
    private static final int SHORT_WINDOW = 22;
    private static final int MEDIUM_WINDOW = 50;
    private static final int LONG_WINDOW = 90;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.hybridOptimized(redBallDataDetail, redRange, List.of(SHORT_WINDOW, MEDIUM_WINDOW, LONG_WINDOW));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.hybridOptimized(blueBallDataDetail, blueRange, List.of(SHORT_WINDOW, MEDIUM_WINDOW, LONG_WINDOW));
    }


}
