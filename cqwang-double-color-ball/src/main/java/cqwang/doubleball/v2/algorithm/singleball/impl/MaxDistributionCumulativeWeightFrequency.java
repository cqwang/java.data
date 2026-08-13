package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.model.result.SingleResult;
import cqwang.doubleball.v2.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class MaxDistributionCumulativeWeightFrequency implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 结合多个时间窗口的加权频次，权重加在数值区间上累计
        prepareOption(option);
        return AlgorithmUtils.distributionWeight(singleBall, range, option);
    }


    private void prepareOption(StrategyOption option) {
        option.setCumulativeWeight(true);
        option.setPeriods(new int[]{12, 20, 40});
        option.setWeights(new int[]{7, 3, 1});
    }
}
