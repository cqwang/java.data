package cqwang.doubleball.detection.utils;

import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmUtils {

    public static int diffWeightedAverage(SingleBall singleBall, SingleBall preBall, Range<Integer> range) {
        var diffList = new ArrayList<Integer>(singleBall.getDataList().size());
        for (int i = 0; i < singleBall.getDataList().size(); i++) {
            var diff = singleBall.getDataList().get(i) - preBall.getDataList().get(i);
            diffList.add(diff);
        }
        return weightedAverage(diffList, range);
    }


    /**
     * 中位数
     * @param range
     * @return
     */
    public static int median(List<Integer> dataList, Range<Integer> range) {
        var median = medianData(dataList);
        return Math.max(range.getMinimum(),
                Math.min(range.getMaximum(), (int) Math.round(median)));
    }

    private static int medianData(List<Integer> dataList){
        int mid = dataList.size() / 2;
        if (dataList.size() % 2 == 0) {
            return (dataList.get(mid - 1) + dataList.get(mid)) / 2;
        } else {
            return dataList.get(mid);
        }
    }

    /**
     * 加权平均值
     * @return
     */
    public static int weightedAverage(List<Integer> dataList, Range<Integer> range){
        var weightedAvg = weightedAverage(dataList);
        return Math.max(range.getMinimum(),
                Math.min(range.getMaximum(), (int) Math.round(weightedAvg)));
    }

    private static double weightedAverage(List<Integer> dataList){
        double sum = 0;
        double totalWeight = 0;

        for (int i = 0; i < dataList.size(); i++) {
            double weight = 1.0 + (double) (i / dataList.size());
            sum += dataList.get(i) * weight;
            totalWeight += weight;
        }

        return sum / totalWeight;
    }



    /**
     * 差分平滑算法 - 基于一阶差分的平滑预测
     */
    public static int predictByDifferentialSmoothing(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {

        // 计算差分序列
        double diffSum = 0;
        for (int i = 1; i < singleBall.getDataList().size(); i++) {
            diffSum += singleBall.getDataList().get(i) - singleBall.getDataList().get(i - 1);
        }

        double avgDiff = diffSum / (singleBall.getDataList().size() - 1);
        int prediction = (int) Math.round(singleBall.getDataList().get(singleBall.getDataList().size() - 1) + avgDiff);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }

    /**
     * 标准差加权算法 - 基于均值和标准差的加权选择
     */
    public static int standardDeviationWeighted(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {
        // 计算均值和标准差
        double mean = singleBall.getDataList().stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double variance = singleBall.getDataList().stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0);
        double stdDev = Math.sqrt(variance);

        // 计算综合评分
        double bestScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = singleBall.getFrequency(i);
            if (freq == 0) continue;

            // 评分 = 频率 * (1 - 偏离度)
            // 偏离度 = |i - mean| / (stdDev + 1)
            double deviation = Math.abs(i - mean) / (stdDev + 1.0);
            double score = freq * (1.0 - Math.min(deviation, 1.0));

            if (score > bestScore) {
                bestScore = score;
                result = i;
            }
        }

        return result;
    }

    /**
     * 中点平衡算法 - 在中点附近寻找最高频数
     */
    public static int midpointBalance(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {
        int midPoint = (range.getMinimum() + range.getMaximum()) / 2;

        // 分别计算两侧的评分
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if(option.isBlocked(i)) {
                continue;
            }

            // 评分 = 频率 * (1 - 偏离中点的距离权重)
            int distance = Math.abs(i - midPoint);
            double distanceWeight = 1.0 - (double) distance / (range.getMaximum() - range.getMinimum());
            double score = singleBall.getFrequency(i) * (0.7 + distanceWeight * 0.3);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    public static SingleResult distribution(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {
        var sub5 = singleBall.sub(5);
        var sub20 = singleBall.sub(20);
        var sub70 = singleBall.sub(70);

        int maxFrequency = 0;
        int result = range.getMinimum();
        boolean success = false;
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlocked(data)) {
                continue;
            }

            // 全局热
            if (singleBall.toAvgFrequencyRatio(data) < 2
                    && singleBall.getTotalFrequency() > 200 && singleBall.getMaxContinuousFrequency(data) < 2) {
                continue;
            }

            // 长期热
            if (sub70.toAvgFrequencyRatio(data) < 2) {
                continue;
            }

            // 中期稳
            if (sub20.toAvgFrequencyRatio(data) < 0.5 || sub20.toAvgFrequencyRatio(data) > 2 || sub20.getMaxContinuousFrequency(data) >= 3) {
                continue;
            }

            // 短期冷
            if (sub5.toAvgFrequencyRatio(data) > 0.8 || sub5.getMaxContinuousFrequency(data) >= 2) {
                continue;
            }

            if (singleBall.get(data).getFrequency() > maxFrequency) {
                maxFrequency = singleBall.getFrequency(data);
                result = data;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }

    /**
     * 邻域聚集算法 - 优先选择与其他高频数相邻的值
     *
     */
    public static SingleResult neighborhoodCluster(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {

        // 计算每个值的邻域强度
        double maxScore = 0;
        int result = range.getMinimum();
        boolean success = false;
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlocked(i)) {
                continue;
            }

            int freq = singleBall.getFrequency(i);
            if (freq == 0) continue;

            // 计算邻域强度：周围NEIGHBOR_RANGE内的频率总和
            int neighborStrength = 0;
            int period =2 ;
            for (int j = Math.max(range.getMinimum(), i - period);
                 j <= Math.min(range.getMaximum(), i + period); j++) {
                neighborStrength += singleBall.getFrequency(j);
            }

            // 评分 = 自身频率 + 邻域强度权重
            double score = freq * 2.0 + (neighborStrength - freq) * 0.8;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return new SingleResult(result, success);
    }



    /**
     * 支持向量机模拟算法 - 基于边界优化的预测
     * 基于相似度的预测 - 多维度相似度计算
     */
    public static SingleResult similarity(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {
        var subBall = singleBall.sub(40);
        double maxScore = 0;
        int result = range.getMinimum();
        boolean success = false;
        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            if (option.isBlocked(candidate)) {
                continue;
            }

            double score = 0;

            // 频率得分
            score += subBall.getFrequency(candidate) * 10;

            // 邻近性得分
            for (var data : subBall.getDataList()) {
                int diff = Math.abs(data - candidate);
                if (diff <= 2) {
                    score += 5;
                } else if (diff <= 5) {
                    score += 2;
                }
            }

            // 全局频率补充
            int globalFreq = singleBall.getFrequency(candidate);
            score += globalFreq * 0.5;

            if (score > maxScore) {
                maxScore = score;
                result = candidate;
            }
        }

        return new SingleResult(result, success);
    }


    /**
     * 连续出现的次数 权重高
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult continueWeight(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {

        double maxScore = 0;
        int result = range.getMinimum();

        boolean success = false;
        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            if (option.isBlocked(candidate)) {
                continue;
            }

            var dataFrequency = singleBall.get(candidate);
            if (dataFrequency == null) {
                continue;
            }
            double score = dataFrequency.getFrequency() * 1.0 + dataFrequency.getMaxContinuousFrequency() * 3.0;

            if (score > maxScore) {
                maxScore = score;
                result = candidate;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }


    /**
     * 频率突跃算法 - 检测并偏好频率的突跃点
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult surge(SingleBall singleBall,
                            Range<Integer> range,
                                     PredictOption option) {
        var period = 30;
        var midBall = singleBall.sub(period);
        var subBall = singleBall.sub(period / 2);

        double maxScore = 0;
        boolean success = false;
        int result = range.getMinimum();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlocked(i)) {
                continue;
            }

            if (midBall.getFrequency(i) == 0) {
                continue;
            }

            // 检测频率突跃：最近一半的频率相比整体频率的提升
            double surgeRatio = subBall.getFrequency(i) * 2.0 / midBall.getFrequency(i);
            double score = midBall.getFrequency(i) * (1.0 + Math.min(surgeRatio, 2.0) * 0.5);

            if (score > maxScore) {
                maxScore = score;
                result = i;
                success = true;
            }
        }

        return new SingleResult(result, success);
    }


    /**
     * 加权众数算法 - 最近出现的频率最高值权重更高
     *
     * @param singleBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult squareWeight(
            SingleBall singleBall,
            Range<Integer> range,
            PredictOption option) {
        // 计算每个值的加权频率
        double maxWeightedFreq = 0;
        int result = range.getMinimum();

        boolean success = false;
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
                success = true;
            }
        }

        return new SingleResult(result, success);
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
        var subList = new SingleBall[3];
        subList[0] = singleBall.sub(12);
        subList[1] = singleBall.sub(20);
        subList[2]=singleBall.sub(40);

        var weightList = isCumulativeWeight ? new int[]{7, 3, 1} : new int[]{10, 4, 1};



        double maxScore = 0;
        int result = range.getMinimum();
        boolean success = false;
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlocked(data)) {
                continue;
            }


            var score = 0;
            for (var index = 0; index < subList.length; index++) {
                score += calculateScore(subList, isCumulativeWeight, weightList, data, index);
            }

            if (score > maxScore) {
                maxScore = score;
                result = data;
                success = true;
            }
        }
        return new SingleResult(result, success);
    }

    public static int calculateScore(SingleBall[] subList, boolean isCumulativeWeight, int[] weightList, int data, int index) {
        if (isCumulativeWeight) {
            return subList[index].getFrequency(data) * weightList[index];
        }

        if (index > 0) {
            return (subList[index].getFrequency(data) - subList[index - 1].getFrequency(data)) * weightList[index];
        } else {
            return subList[index].getFrequency(data) * weightList[index];
        }
    }

}
