package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.model.result.SingleResult;
import org.apache.commons.lang3.Range;

public class RecentMaxFrequency implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 爆发检测，取最近出现频次最高的数值
        var sub = singleBall.sub(15);
        return sub.getMaxDataFrequency(range, option);
    }
}
