package cqwang.doubleball.v2.algorithm.singleball.imp;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.model.result.SingleResult;
import cqwang.doubleball.v2.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class FirstRedPredict implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        return AlgorithmUtils.predictPriority(singleBall, range, option,
                "DistributionFrequency",
                "SurgeFrequency"
        );
    }
}

