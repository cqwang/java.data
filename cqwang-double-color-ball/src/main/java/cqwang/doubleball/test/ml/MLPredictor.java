package cqwang.doubleball.test.ml;

import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;

public interface MLPredictor {
    /**
     * 预测单个球的值
     */
    SingleResult predict(SingleBall singleBall, PredictOption option);

    /**
     * 获取预测器名称
     */
    String getName();

    /**
     * 获取预测器描述
     */
    String getDescription();
}
