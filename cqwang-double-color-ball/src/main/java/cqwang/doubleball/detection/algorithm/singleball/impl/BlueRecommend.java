package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import org.apache.commons.lang3.Range;

import java.util.List;

public class BlueRecommend implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var range = Range.between(singleBall.getMinData(), singleBall.getMaxData());
        var result = surge(singleBall, range, option);
        return result;
//        if(result.isSuccess()) {
//            return result;
//        }

//        return findCold(singleBall, range, option);
    }


    // 间隔最大，频次最少，加权重
    public SingleResult findCold(SingleBall singleBall, Range<Integer> range, PredictOption option) {
        double maxScore = 0;
        boolean success = false;
        int result = range.getMinimum();
        int period = 50;
        var maxSize = DoubleColorBallPreload.getAllData().size();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlocked(i)) {
                continue;
            }

            var globalBallIndexList = DoubleColorBallPreload.getSplitAllData().getIndexList(singleBall.getBallType(), singleBall.getIndex(), i);
            var score = calculateScore(globalBallIndexList, maxSize, period);
            if (score > maxScore) {
                maxScore = score;
                result = i;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }

    public double calculateScore(List<Integer> indexList, int maxSize, int period) {
        double sumScore = 0;
        int minIndex = maxSize - period;
        int hitCount = 0;
        int lastIndex = maxSize;
        for (var i = indexList.size() - 1; i >= 0; i--) {
            var index = indexList.get(i);
            if (index < minIndex) {
                break;
            }

            hitCount++;

            var diff = lastIndex - index;
            var score = diff * 1.0 / hitCount;
            sumScore += score;
            lastIndex = index;
        }

        if (hitCount < 2) {
            return 0;
        }
        return sumScore;
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
            if (option.isBlocked(i)) {
                continue;
            }

            if (sub30.getFrequency(i) == 0) {
                continue;
            }

            if(!becomeHot(singleBall, sub30, i, 1.5)){
                continue;
            }

            // 检测频率突跃：最近的频率相比整体频率的提升
            double score = sub30.getFrequency(i) + scale(sub30, sub15, i, 1.0);

            if (score > maxScore) {
                maxScore = score;
                result = i;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }

    /**
     *
     * @param longBall
     * @param recentBall
     * @param data
     * @param factor     倍数
     * @return
     */
    private static boolean becomeHot(SingleBall longBall, SingleBall recentBall, int data, double factor) {
        return scale(longBall, recentBall, data,1.0) > longBall.getFrequency(data) * factor;
    }

    private static boolean becomeCold(SingleBall longBall, SingleBall recentBall, int data) {
        return scale(longBall, recentBall, data, 1.0) < longBall.getFrequency(data);
    }

    private static double calcFactor(SingleBall longBall, SingleBall recentBall) {
        return 1.0 * longBall.getDataList().size() / recentBall.getDataList().size();
    }

    /**
     * 把短期频次 按照比例扩展为长期频次
     *
     * @param longBall
     * @param recentBall
     * @param data
     * @return
     */
    private static double scale(SingleBall longBall, SingleBall recentBall, int data, double factor) {
        return recentBall.getFrequency(data) * calcFactor(longBall, recentBall);
    }
}




