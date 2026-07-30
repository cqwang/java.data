package cqwang.doubleball.algorithm.detection.impl.mixed;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

public class UltimateFrequency_MidpointBalance_Algorithm implements PredictionAlgorithm {
    private static final int W1 = 12;   // 最近12次
    private static final int W2 = 28;   // 最近28次
    private static final int W3 = 52;   // 最近52次
    private static final int WINDOW_SIZE = 45;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.ultimateFrequency(redBallDataDetail, redRange, List.of(W1, W2, W3));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.midpointBalance(blueBallDataDetail, blueRange, WINDOW_SIZE);
    }
}
