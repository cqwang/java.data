package cqwang.doubleball.v2.utils;

import cqwang.doubleball.common.model.inner.DataLevel;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.data.features.FrequencyLevel;
import cqwang.doubleball.v2.model.option.StrategyOption;
import org.apache.commons.lang3.Range;

public class AlgorithmUtils {

    /**
     * 结合长期、中期、短期的冷热特征推荐
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static int distribution(
            SingleBall singleBall,
            Range<Integer> range,
            StrategyOption option) {
        if (option.getPeriods().length != 3) {
            throw new RuntimeException("not valid periods");
        }

        var shortSub = singleBall.sub(option.getPeriods()[0]);
        var midSub = singleBall.sub(option.getPeriods()[1]);
        var longSub = singleBall.sub(option.getPeriods()[2]);


        int maxFrequency = 0;
        int result = range.getMinimum();
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            // 长期热，中期稳，短期冷，推荐
            var longFre = longSub.getFrequencyLevel(data);
            var midFre = midSub.getFrequencyLevel(data);
            var shortFre = shortSub.getFrequencyLevel(data);

            if (longFre.greatEqualsThen(FrequencyLevel.HOT)
                    && midFre == FrequencyLevel.STABLE
                    && shortFre.lessEqualsThen(FrequencyLevel.COLD)) {
                if (longSub.get(data).getFrequency() > maxFrequency) {
                    maxFrequency = longSub.getFrequency(data);
                    result = data;
                }
            }
        }
        return result;
    }

    /**
     * 结合多个时间窗口的加权频次
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static int distributionWeight(
            SingleBall singleBall,
            Range<Integer> range,
            StrategyOption option) {
        var subList = new SingleBall[option.getPeriods().length];
        for (int index = 0; index < subList.length; index++) {
            subList[index] = singleBall.sub(option.getPeriods()[index]);
        }

        double maxScore = 0;
        int result = range.getMinimum();
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            var score = 0;
            for (var index = 0; index < subList.length; index++) {
                score += calculateScore(subList, option, data, index);
            }

            if (score > maxScore) {
                maxScore = score;
                result = data;
            }
        }
        return result;
    }

    private static int calculateScore(SingleBall[] subList, StrategyOption option, int data, int index) {
        if (option.isCumulativeWeight()) {
            return subList[index].getFrequency(data) * option.getWeights()[index];
        }

        if (index > 0) {
            return (subList[index].getFrequency(data) - subList[index - 1].getFrequency(data)) * option.getWeights()[index];
        } else {
            return subList[index].getFrequency(data) * option.getWeights()[index];
        }
    }

}
