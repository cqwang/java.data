package cqwang.doubleball.algorithm.detection.impl.mixed;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

public class RecentModified_AdaptiveThreshold_Algorithm implements PredictionAlgorithm {
    private static final int RECENT_SIZE = 30;
    private static final int ANALYSIS_WINDOW = 45;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.recentModified(redBallDataDetail, redRange, RECENT_SIZE);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.adaptiveThreshold(blueBallDataDetail, blueRange, ANALYSIS_WINDOW);
    }
}
