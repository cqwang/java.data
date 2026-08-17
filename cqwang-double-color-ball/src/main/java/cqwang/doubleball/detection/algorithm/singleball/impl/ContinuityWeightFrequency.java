package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

/**
 * 基于连续性的预测 - 连续出现的频次和权重
 */
public class ContinuityWeightFrequency implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var subBall = singleBall.sub(12);
        var range = Range.between(singleBall.getMinData(), singleBall.getMaxData());
        return AlgorithmUtils.continueWeight(subBall, range, option);
    }
}
