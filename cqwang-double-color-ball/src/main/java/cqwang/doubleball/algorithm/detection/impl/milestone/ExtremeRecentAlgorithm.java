package cqwang.doubleball.algorithm.detection.impl.milestone;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
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
