package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import org.apache.commons.lang3.Range;

public class MaxFrequencyImpl implements SingleBallPredictAlgorithm {
    @Override
    public int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        return singleBall.getMaxDataFrequency().getData();
    }
}
