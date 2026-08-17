package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;

public class MaxFrequency implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        return singleBall.getMaxDataFrequency(option);
    }
}
