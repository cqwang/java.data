package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.StrategyOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

/**
 * 频率突跃算法 - 检测并偏好频率的突跃点
 */
public class SurgeFrequency implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        option.setPeriod(30);
        return AlgorithmUtils.surge(singleBall, range, option);
    }
}
