package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import org.apache.commons.lang3.Range;

public class MaxFrequencyImpl implements SingleBallPredictAlgorithm {
    @Override
    public int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 从全量样本中，选取出现频次最高的数值
        return singleBall.getMaxDataFrequency(range, option).getData();
    }
}
