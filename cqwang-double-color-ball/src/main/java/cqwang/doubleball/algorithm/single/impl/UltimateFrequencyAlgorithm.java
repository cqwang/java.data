package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.common.MathUtils;
import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

/**
 * 蓝球终极频率算法 - 结合多个时间窗口的加权频率
 */
public class UltimateFrequencyAlgorithm implements PredictionAlgorithm {
    private static final int W1 = 12;   // 最近12次
    private static final int W2 = 28;   // 最近28次
    private static final int W3 = 52;   // 最近52次

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.ultimateFrequency(redBallDataDetail, redRange, List.of(W1, W2, W3));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.ultimateFrequency(blueBallDataDetail, blueRange, List.of(W1, W2, W3));
    }


}
