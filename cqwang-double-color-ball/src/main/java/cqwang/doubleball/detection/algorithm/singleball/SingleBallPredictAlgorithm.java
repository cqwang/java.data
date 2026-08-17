package cqwang.doubleball.detection.algorithm.singleball;

import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOptionDetail;
import cqwang.doubleball.detection.model.option.StrategyOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

import java.util.Set;

/**
 * 单个球的预测
 */
public interface SingleBallPredictAlgorithm {
    /**
     * 预测单个球的值
     *
     * @param singleBall
     * @param range
     * @return
     */
    SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option);
}
