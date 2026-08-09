package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class MaxDistributionWeightFrequencyImpl implements SingleBallPredictAlgorithm {

    @Override
    public int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 结合多个时间窗口的加权频次
        prepareOption(option);
        return AlgorithmUtils.distributionWeight(singleBall, range, option);
    }

    private void prepareOption(StrategyOption option) {
        if(option.isCumulativeWeight()){
            option.setPeriods(new int[]{12, 20, 40});
            option.setWeights(new int[]{7, 3, 1});
        }
        else {
            option.setPeriods(new int[]{12, 20, 40});
            option.setWeights(new int[]{10, 4, 1});
        }
    }
}
