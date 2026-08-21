package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

public class SvmSimulation implements SingleBallAlgorithm {
    private static final double weight = 0.4;

    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        // 支持向量机模拟算法 - 基于边界优化的预测, 在min和max之间的加权中点, 更接近max的支持向量
        var subBall = singleBall.sub(50);

        var max = subBall.getMaxDataFrequency(singleBall, option);
        var min = subBall.getMinDataFrequency(singleBall, option);
        var range = Range.between(singleBall.getMinData(), singleBall.getMaxData());

        int result = (int) Math.round(min.getResult() * (1 - weight) + max.getResult() * weight);
        result = Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
        return new SingleResult(result, true);
    }
}