package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class RedRecommend implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var range = Range.between(singleBall.getMinData(), singleBall.getMaxData());
        return distributionWeight(singleBall, range, true, option);
    }

    /**
     * 结合多个时间窗口的加权频次
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult distributionWeight(
            SingleBall singleBall,
            Range<Integer> range,
            boolean isCumulativeWeight,
            PredictOption option) {
        var subList = new SingleBall[]{
                singleBall.sub(5),
                singleBall.sub(12),
                singleBall.sub(20),
                singleBall.sub(40)
        };
        var weightList = new double[]{-2, 10, 3, 1};


        double maxScore = 0;
        int result = range.getMinimum();
        boolean success = false;
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlocked(data)) {
                continue;
            }


            var score = 0.0;
            for (var index = 0; index < subList.length; index++) {
                score += AlgorithmUtils.calculateScore(subList, isCumulativeWeight, weightList, data, index);
            }

            if (score > maxScore) {
                maxScore = score;
                result = data;
                success = true;
            }
        }
        return new SingleResult(result, success);
    }
}
