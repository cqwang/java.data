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
        return surgeDistributionWeight(singleBall, range, option);
    }


    /**
     * 冷-》热
     * 热-》冷-》快热
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult surgeDistributionWeight(SingleBall singleBall,
                                     Range<Integer> range,
                                     PredictOption option) {
        var sub100 = singleBall.sub(100);
        var sub50 = singleBall.sub(50);
        var sub12 = singleBall.sub(12);
        var sub4 = singleBall.sub(4);

        double maxScore = 0;
        boolean success = false;
        int result = range.getMinimum();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlocked(i)) {
                continue;
            }

            if (sub100.getFrequency(i) == 0) {
                continue;
            }

            // 最近几天内 不能太热
            if (sub4.getFrequency(i) > 2 || sub4.getMaxContinuousFrequency(i) > 2) {
                continue;
            }


            if(!becomeHot(singleBall,sub100, i, 1.5) || !becomeHot(singleBall, sub50, i ,1.5) || !becomeHot(singleBall, sub12, i ,1.5)){
                continue;
            }



            // 检测频率突跃：最近的频率相比整体频率的提升
            double surgeRatio = getSurgeRatio(singleBall, sub12, i);
            double score = singleBall.getFrequency(i) * (1.0 + surgeRatio);

            if (score > maxScore) {
                maxScore = score;
                result = i;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }

    /**
     * 短期频次按照比例扩展后，和长期批次的比率
     * @param longBall
     * @param recentBall
     * @param data
     * @return
     */
    private static double getSurgeRatio(SingleBall longBall, SingleBall recentBall, int data){
        return scale(longBall,recentBall, data) / longBall.getFrequency(data);
    }

    /**
     *
     * @param longBall
     * @param recentBall
     * @param data
     * @param factor 倍数
     * @return
     */
    private static boolean becomeHot(SingleBall longBall, SingleBall recentBall, int data, double factor){
        return scale(longBall,recentBall, data) > longBall.getFrequency(data) * factor;
    }

    private static boolean becomeCold(SingleBall longBall, SingleBall recentBall, int data){
        return scale(longBall,recentBall, data) < longBall.getFrequency(data);
    }

    private static double calcFactor(SingleBall longBall, SingleBall recentBall){
        return 1.0 * longBall.getDataList().size() / recentBall.getDataList().size();
    }

    /**
     * 把短期频次 按照比例扩展为长期频次
     * @param longBall
     * @param recentBall
     * @param data
     * @return
     */
    private static double scale(SingleBall longBall, SingleBall recentBall, int data){
        return recentBall.getFrequency(data) * calcFactor(longBall, recentBall);
    }
}
