package cqwang.doubleball.v2.utils;

import cqwang.doubleball.common.model.BallDataDetail;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.data.features.FrequencyLevel;
import cqwang.doubleball.v2.model.option.StrategyOption;
import org.apache.commons.lang3.Range;

public class AlgorithmUtils {


    /**
     * 连续出现的次数 权重高
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static int continueWeight(
            SingleBall singleBall,
            Range<Integer> range,
            StrategyOption option) {

        double maxScore = 0;
        int result = range.getMinimum();

        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            if(option.isBlocked(candidate)) {
                continue;
            }

            var dataFrequency = singleBall.get(candidate);
            if(dataFrequency == null){
                continue;
            }
            double score = dataFrequency.getFrequency() * 1.0 + dataFrequency.getMaxContinuousFrequency() * 3.0;

            if (score > maxScore) {
                maxScore = score;
                result = candidate;
            }
        }

        return result;
    }



    /**
     * 频率突跃算法 - 检测并偏好频率的突跃点
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static int surge(SingleBall singleBall,
                            Range<Integer> range,
                            StrategyOption option) {
        var midBall = singleBall.sub(option.getPeriod());
        var subBall = singleBall.sub(option.getPeriod()/2);

        double maxScore = 0;
        int result = range.getMinimum();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
//            if (option.isBlocked(i)) {
//                continue;
//            }

            if (midBall.getFrequency(i) == 0) {
                continue;
            }

            // 检测频率突跃：最近一半的频率相比整体频率的提升
            double surgeRatio = subBall.getFrequency(i) * 2.0 / midBall.getFrequency(i);
            double score = midBall.getFrequency(i) * (1.0 + Math.min(surgeRatio, 2.0) * 0.5);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    /**
     * 加权众数算法 - 最近出现的频率最高值权重更高
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static int squareWeight(
            SingleBall singleBall,
            Range<Integer> range,
            StrategyOption option) {
        // 计算每个值的加权频率
        double maxWeightedFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlocked(i)) {
                continue;
            }


            double weightedFreq = 0;
            int occurrenceIndex = 0;

            for (int j = 0; j < singleBall.getDataList().size(); j++) {
                if (singleBall.getDataList().get(j) == i) {
                    // 距离越近权重越高
                    double weight = 1.0 + (double) j / singleBall.getDataList().size();
                    weightedFreq += weight;
                    occurrenceIndex++;
                }
            }

            // 平滑处理：防止单一高权重项过度影响
            if (occurrenceIndex > 0) {
                weightedFreq /= occurrenceIndex;
                weightedFreq *= occurrenceIndex;
            }

            if (weightedFreq > maxWeightedFreq) {
                maxWeightedFreq = weightedFreq;
                result = i;
            }
        }

        return result;
    }


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
            if (option.isBlocked(data)) {
                continue;
            }

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
            if (option.isBlocked(data)) {
                continue;
            }


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
