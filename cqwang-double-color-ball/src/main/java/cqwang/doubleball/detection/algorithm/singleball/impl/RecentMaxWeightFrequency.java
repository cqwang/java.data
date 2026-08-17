package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.StrategyOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class RecentMaxWeightFrequency implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {

        // 取最近出现权重最高的数值，时间越近权重越高
        var sub = singleBall.sub(45);
        return AlgorithmUtils.squareWeight(sub, range, option);
    }
}
