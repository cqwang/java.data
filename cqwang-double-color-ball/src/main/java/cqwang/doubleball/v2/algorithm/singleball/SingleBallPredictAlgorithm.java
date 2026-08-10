package cqwang.doubleball.v2.algorithm.singleball;

import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.data.features.FrequencyLevel;
import cqwang.doubleball.v2.model.option.PredictOptionDetail;
import cqwang.doubleball.v2.model.option.StrategyOption;
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
    int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option);

    default int predictRetry(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        var result = predict(singleBall, range, option);

        // 如果太热，就更换
        var dataFrequency = singleBall.sub(3).get(result);
        if (dataFrequency != null && dataFrequency.getFrequencyLevel().greatEqualsThen(FrequencyLevel.STABLE)) {
            option.setPredictOption(new PredictOptionDetail(Set.of(result)));
            return predict(singleBall, range, option);
        }

        dataFrequency = singleBall.sub(2).get(result);
        if(dataFrequency!=null && dataFrequency.getFrequencyLevel().greatEqualsThen(FrequencyLevel.COLD)){
            option.setPredictOption(new PredictOptionDetail(Set.of(result)));
            return predict(singleBall, range, option);
        }

        return result;
    }
}
