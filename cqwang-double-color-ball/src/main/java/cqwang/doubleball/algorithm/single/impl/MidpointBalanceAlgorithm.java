package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.common.MathUtils;
import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球中点平衡算法 - 在中点附近寻找最高频数
 */
public class MidpointBalanceAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 45;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.midpointBalance(redBallDataDetail, redRange, WINDOW_SIZE);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.midpointBalance(blueBallDataDetail, blueRange, WINDOW_SIZE);
    }


}
