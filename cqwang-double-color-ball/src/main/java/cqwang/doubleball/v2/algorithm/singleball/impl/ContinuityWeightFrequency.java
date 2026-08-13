package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

/**
 * 基于连续性的预测 - 连续出现的频次和权重
 */
public class ContinuityWeightFrequency implements SingleBallPredictAlgorithm {
    @Override
    public int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        var subBall = singleBall.sub(12);
        return AlgorithmUtils.continueWeight(subBall, range, option);
    }
}
