package cqwang.doubleball.algorithm.detection.impl.mixed;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

public class RecentModified_SVMSimulation4_Algorithm implements PredictionAlgorithm {
    private static final int RECENT_SIZE = 30;
    private static final double WEIGHT = 0.4;// 更接近max的支持向量

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.recentModified(redBallDataDetail, redRange, RECENT_SIZE);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.svmSimulation(blueBallDataDetail, blueRange, WEIGHT);
    }
}
