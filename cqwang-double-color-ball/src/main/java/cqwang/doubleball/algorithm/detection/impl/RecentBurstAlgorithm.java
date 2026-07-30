package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

/**
 * 蓝球近期爆发算法 - 优先选择最近10次中出现过的值
 */
public class RecentBurstAlgorithm implements PredictionAlgorithm {
    private static final int BURST_WINDOW = 10;
    private static final int SECONDARY_WINDOW = 35;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.recentBurst(redBallDataDetail, redRange, List.of(BURST_WINDOW, SECONDARY_WINDOW));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.recentBurst(blueBallDataDetail, blueRange, List.of(BURST_WINDOW, SECONDARY_WINDOW));
    }


}
