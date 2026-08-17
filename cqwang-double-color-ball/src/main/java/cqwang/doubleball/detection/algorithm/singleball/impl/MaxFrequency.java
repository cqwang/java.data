package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.StrategyOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

public class MaxFrequency implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 从全量样本中，选取出现频次最高的数值
        return singleBall.getMaxDataFrequency(range, option);
    }
}
