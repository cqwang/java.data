package cqwang.doubleball.algorithm.single.impl.milestone;

import cqwang.doubleball.common.MathUtils;
import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球极限近期算法 - 只关注最近15次数据
 */
public class ExtremeRecentAlgorithm implements PredictionAlgorithm {
    private static final int EXTREME_WINDOW = 15;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.extremeRecent(redBallDataDetail, redRange, EXTREME_WINDOW);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.extremeRecent(blueBallDataDetail, blueRange, EXTREME_WINDOW);
    }

}
