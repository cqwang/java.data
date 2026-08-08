package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import org.apache.commons.lang3.Range;

public class RecentDistributionMaxWeightFrequencyImpl implements SingleBallPredictAlgorithm {
    private static final int[] SAMPLE_SIZE_LIST = {12, 20, 40};
    private static final int[] SAMPLE_WEIGHT_LIST = {7, 3, 1};


    @Override
    public int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 结合多个时间窗口的加权频次
        var subList = new SingleBall[SAMPLE_SIZE_LIST.length];
        for (int index = 0; index < subList.length; index++) {
            subList[index] = singleBall.sub(SAMPLE_SIZE_LIST[index]);
        }

        double maxScore = 0;
        int result = range.getMinimum();
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            var score = 0;
            for (var index = 0; index < subList.length; index++) {
                score += subList[index].getFrequency(data) * SAMPLE_WEIGHT_LIST[index];
            }

            if (score > maxScore) {
                maxScore = score;
                result = data;
            }
        }
        return result;
    }
}
