package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;


public class BlueRecommend implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var range = Range.between(singleBall.getMinData(), singleBall.getMaxData());
        return surge(singleBall, range, option);
    }


    /**
     * 冷-》热
     * 热-》冷-》快热
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult surge(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {
        var sub30 = singleBall.sub(30);
        var sub15 = singleBall.sub(15);

        double maxScore = 0;
        boolean success = false;
        int result = range.getMinimum();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlock(singleBall.getBallType(), singleBall.getIndex(), i)) {
                continue;
            }
            if(option.isAllow(singleBall.getBallType(), singleBall.getIndex(), i)) {
                return new SingleResult(i, true);
            }

            if (sub30.getFrequency(i) == 0) {
                continue;
            }

            if(!AlgorithmUtils.becomeHot(singleBall, sub30, i, 1.5)){
                continue;
            }

            // 检测频率突跃：最近的频率相比整体频率的提升
            double score = sub30.getFrequency(i) + AlgorithmUtils.scale(sub30, sub15, i, 1.0);

            if (score > maxScore) {
                maxScore = score;
                result = i;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }

}




