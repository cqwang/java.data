package cqwang.doubleball.v2.algorithm.singleball;

import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import org.apache.commons.lang3.Range;

/**
 * 单个球的预测
 */
public interface SingleBallPredictAlgorithm {
    /**
     * 预测单个球的值
     * @param singleBall
     * @param range
     * @return
     */
    int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option);
}
