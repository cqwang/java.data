package cqwang.doubleball.detection.algorithm.singleball;

import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;

public interface SingleBallAlgorithm {
    /**
     * 预测单个球的值
     *
     * @param singleBall
     * @return
     */
    SingleResult predict(SingleBall singleBall, PredictOption option);
}
