package cqwang.doubleball.algorithm.single.impl.milestone;

import cqwang.doubleball.common.MathUtils;
import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球加权众数算法 - 最近出现的频率最高值权重更高
 */
public class WeightedModeAlgorithm implements PredictionAlgorithm {
    private static final int SAMPLE_SIZE = 80;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.weightedMode(redBallDataDetail, redRange, SAMPLE_SIZE);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.weightedMode(blueBallDataDetail, blueRange, SAMPLE_SIZE);
    }
}
