package cqwang.doubleball.common;

import cqwang.doubleball.common.model.BallDataDetail;
import cqwang.doubleball.common.model.inner.DataLevel;
import cqwang.doubleball.common.model.inner.FrequencyDataListModel;
import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 频次算法
 */
public class FrequencyAlgorithmUtils {

    /**
     * 加权众数算法 - 最近出现的频率最高值权重更高
     * @param ballDataDetail
     * @param range
     * @param sampleSize
     * @return
     */
    public static int weightedMode(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - sampleSize);

        // 计算每个值的加权频率
        double maxWeightedFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weightedFreq = 0;
            int occurrenceIndex = 0;

            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    // 距离越近权重越高
                    double weight = 1.0 + (double) (j - startIdx) / (dataList.size() - startIdx);
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
     * 支持向量机模拟算法 - 基于边界优化的预测
     * @param ballDataDetail
     * @param range
     * @param sampleSize
     * @param weight
     * @return
     */
    public static int svmSimulation(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize, double weight) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 寻找支持向量（边界值）
        var subList = dataList.subList(dataList.size() - sampleSize, dataList.size());
        int min = subList.stream().mapToInt(Integer::intValue).min().orElse(range.getMinimum());
        int max = subList.stream().mapToInt(Integer::intValue).max().orElse(range.getMaximum());

        // 支持向量机的决策边界：在min和max之间的加权中点
        int result = (int) Math.round(min * (1 - weight) + max * weight);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }



    public static int recentModified(BallDataDetail ballDataDetail, Range<Integer> range, int recentSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - recentSize);
        int maxFreq = 0;
        int result = (range.getMinimum() + range.getMaximum()) / 2;

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                }
            }
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }
        return result;
    }


    public static int predictByBayesian(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 计算后验概率：P(next|last)
        int lastValue = dataList.get(dataList.size() - 1);
        int nextCount = 0;
        int nextSum = 0;

        // 计算在lastValue之后出现的值的分布
        for (int i = 0; i < dataList.size() - 1; i++) {
            if (dataList.get(i) == lastValue) {
                nextSum += dataList.get(i + 1);
                nextCount++;
            }
        }

        int prediction = nextCount > 0 ? (int) Math.floor(nextSum / nextCount) : lastValue;
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }

    /**
     * 查找出现频次最高的数值
     *
     * @param ballDataDetail
     * @param range
     * @return
     */
    public static int findMaxFrequencyValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        return findMaxFrequencyValue(ballDataDetail.getDataFrequencyMap(), range);
    }

//    /**
//     * 查找出现频次的权重最高的数值
//     * @param ballDataDetail
//     * @param range
//     * @return
//     */
//    public static int findMaxFrequencyWeightValue(BallDataDetail ballDataDetail, Range<Integer> range) {
//        var dataFrequencyWeightMap = calculateDataFrequencyWeightMap(ballDataDetail.getDataFrequencyMap());
//        return findMaxFrequencyValue(dataFrequencyWeightMap, range);
//    }

    /**
     * 基于相似度的预测 - 多维度相似度计算
     */
    public static int similarity(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 最近20个数据作为目标模式
        int windowSize = Math.min(sampleSize, dataList.size());
        int startIdx = dataList.size() - windowSize;

        double maxScore = 0;
        int result = range.getMinimum();

        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            double score = 0;

            // 频率得分
            int frequency = 0;
            for (int i = startIdx; i < dataList.size(); i++) {
                if (dataList.get(i) == candidate) {
                    frequency++;
                }
            }
            score += frequency * 10;

            // 邻近性得分
            for (int i = startIdx; i < dataList.size(); i++) {
                int diff = Math.abs(dataList.get(i) - candidate);
                if (diff <= 2) {
                    score += 5;
                } else if (diff <= 5) {
                    score += 2;
                }
            }

            // 全局频率补充
            int globalFreq = ballDataDetail.getDataFrequencyMap().getOrDefault(candidate, 0);
            score += globalFreq * 0.5;

            if (score > maxScore) {
                maxScore = score;
                result = candidate;
            }
        }

        return result;
    }


    /**
     * 基于爆发检测的预测
     */
    public static int burst(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 爆发窗口
        int burstStart = Math.max(0, dataList.size() - sampleSize);
        return findMostFrequent(dataList, range, burstStart, dataList.size());
    }

    /**
     * 频率突跃算法 - 检测并偏好频率的突跃点
     *
     * @param ballDataDetail
     * @param range
     * @param sampleSize     55
     * @return
     */
    public static int surge(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - sampleSize);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            int recentFreq = 0;
            int halfPoint = (dataList.size() + startIdx) / 2;

            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                    if (j >= halfPoint) {
                        recentFreq++;
                    }
                }
            }

            if (freq == 0) continue;

            // 检测频率突跃：最近一半的频率相比整体频率的提升
            double surgeRatio = recentFreq * 2.0 / freq;
            double score = freq * (1.0 + Math.min(surgeRatio, 2.0) * 0.5);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    /**
     * 基于连续性的预测 - 识别连续出现的值
     */
    public static int continuity(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int windowSize = Math.min(sampleSize, dataList.size());
        int startIdx = dataList.size() - windowSize;

        double maxScore = 0;
        int result = range.getMinimum();

        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            int totalFreq = 0;
            int maxConsecutive = 0;
            int currentConsecutive = 0;

            for (int i = startIdx; i < dataList.size(); i++) {
                if (dataList.get(i) == candidate) {
                    totalFreq++;
                    currentConsecutive++;
                    maxConsecutive = Math.max(maxConsecutive, currentConsecutive);
                } else {
                    currentConsecutive = 0;
                }
            }

            double score = totalFreq * 1.0 + maxConsecutive * 3.0;

            if (score > maxScore) {
                maxScore = score;
                result = candidate;
            }
        }

        return result;
    }


    /**
     * 邻域聚集算法 - 优先选择与其他高频数相邻的值
     *
     * @param ballDataDetail
     * @param range
     * @param sampleSize
     * @param neighborRange
     * @return
     */
    public static int neighborhoodCluster(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize, int neighborRange) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - sampleSize);

        // 首先计算每个值的频率
        java.util.Map<Integer, Integer> windowFreq = new java.util.HashMap<>();
        for (int j = startIdx; j < dataList.size(); j++) {
            int val = dataList.get(j);
            windowFreq.put(val, windowFreq.getOrDefault(val, 0) + 1);
        }

        // 计算每个值的邻域强度
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = windowFreq.getOrDefault(i, 0);
            if (freq == 0) continue;

            // 计算邻域强度：周围NEIGHBOR_RANGE内的频率总和
            int neighborStrength = 0;
            for (int j = Math.max(range.getMinimum(), i - neighborRange);
                 j <= Math.min(range.getMaximum(), i + neighborRange); j++) {
                neighborStrength += windowFreq.getOrDefault(j, 0);
            }

            // 评分 = 自身频率 + 邻域强度权重
            double score = freq * 2.0 + (neighborStrength - freq) * 0.8;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    /**
     * 查找最近样本中频次权重最高的数值
     * 考虑时间衰减和异常值处理
     */
    public static int powerWeight(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize, double decayFactor) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - sampleSize);
        double maxWeight = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weight = 0;
            int count = 0;
            for (int j = dataList.size() - 1; j >= startIdx; j--) {
                if (dataList.get(j) == i) {
                    weight += Math.pow(decayFactor, count);
                }
                count++;
            }
            if (weight > maxWeight) {
                maxWeight = weight;
                result = i;
            }
        }

        return result;
    }


    /**
     * 近期加权算法 - 基于最近数据的加权频率
     *
     * @param ballDataDetail
     * @param range
     * @param sampleSize
     * @return
     */
    public static int recentWeight(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - sampleSize);
        double maxWeight = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weight = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    // 距离越近权重越高
                    weight += 1.0 + (double) (j - startIdx) / (dataList.size() - startIdx);
                }
            }

            if (weight > maxWeight) {
                maxWeight = weight;
                result = i;
            }
        }

        return result;
    }

    /**
     * 高频冷号分段混合算法
     * 近期：低频
     * 中期：稳定
     * 长期：高频
     *
     * @param ballDataDetail
     * @param range
     * @return
     */
    public static int hotColdMixed(BallDataDetail ballDataDetail, Range<Integer> range, int shortPeriodSize, int midPeriodSize, int longPeriodSize, double maxFactor) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        var shortModel = new FrequencyDataListModel(ballDataDetail, shortPeriodSize);
        var midModel = new FrequencyDataListModel(ballDataDetail, midPeriodSize);
        var longModel = new FrequencyDataListModel(ballDataDetail, longPeriodSize);

        int maxFrequency = 0;
        int result = range.getMinimum();
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            // 长期热，中期稳，短期冷，推荐
            if (longModel.getLevel(data).greatEqualsThen(DataLevel.HOT) && midModel.getLevel(data) == DataLevel.STABLE && shortModel.getLevel(data) == DataLevel.COLD) {
                if (longModel.getFrequency(data) > longModel.getMaxFrequency() * maxFactor) {
                    continue;
                }
                if (longModel.getFrequency(data) > maxFrequency) {
                    maxFrequency = longModel.getFrequency(data);
                    result = data;
                }
            }
        }
        return result;
    }


    /**
     * 结合多个时间窗口的加权频率
     *
     * @param ballDataDetail
     * @param range
     * @param periodList
     * @return
     */
    public static int distributionWeight(BallDataDetail ballDataDetail, Range<Integer> range, List<Integer> periodList, List<Integer> weightList) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        if (periodList.size() != weightList.size()) {
            return range.getMinimum();
        }

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            var freqArray = new int[periodList.size()];
            for (int index = 0; index < periodList.size(); index++) {
                var startIndex = Math.max(0, dataList.size() - periodList.get(index));
                for (int j = startIndex; j < dataList.size(); j++) {
                    if (dataList.get(j) == i) freqArray[index]++;
                }
            }

            double score = 0.0;
            for (int index = 0; index < freqArray.length; index++) {
                score += freqArray[index] * weightList.get(index);
            }

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    /**
     * 综合优化算法 - 同时优化红球和蓝球预测
     * * 结合多个最优策略的加权融合
     *
     * @param ballDataDetail
     * @param range
     * @param periodList
     * @param weightList
     * @return
     */
    public static int hybridOptimized(BallDataDetail ballDataDetail, Range<Integer> range, List<Integer> periodList, List<Integer> weightList) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            var freqArray = new int[periodList.size()];
            for (int index = 0; index < periodList.size(); index++) {
                var startIndex = Math.max(0, dataList.size() - periodList.get(index));
                for (int j = startIndex; j < dataList.size(); j++) {
                    if (dataList.get(j) == i) freqArray[index]++;
                }
            }

            // 多窗口加权：短期权重最高
            double score = 0.0;
            for (int index = 0; index < freqArray.length; index++) {
                if (index > 0) {
                    score += (freqArray[index] - freqArray[index - 1]) * weightList.get(index);
                } else {
                    score += freqArray[index] * weightList.get(index);
                }
            }

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    private static int findMaxFrequencyValue(Map<Integer, Integer> dataFrequencyMap, Range<Integer> range) {
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = dataFrequencyMap.getOrDefault(i, 0);
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }
        return result;
    }


    private static Map<Integer, Integer> calculateDataFrequencyWeightMap(Map<Integer, Integer> dataFrequencyMap) {
        Map<Integer, Integer> dataFrequencyWeightMap = new HashMap<>(dataFrequencyMap.size());
        int maxCount = dataFrequencyMap.values().stream().mapToInt(t -> t).max().getAsInt();
        int targetCount = (int) (maxCount * 0.85);
        for (var entry : dataFrequencyMap.entrySet()) {
            var diff = Math.abs(entry.getValue() - targetCount);
            var weight = (diff == 0) ? maxCount : maxCount / diff;
            dataFrequencyWeightMap.put(entry.getKey(), weight);
        }
        return dataFrequencyWeightMap;
    }

    private static int findMostFrequent(List<Integer> dataList, Range<Integer> range,
                                        int startIdx, int endIdx) {
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = countFrequency(dataList, i, startIdx, endIdx);
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }

        return result;
    }

    private static int countFrequency(List<Integer> dataList, int target,
                                      int startIdx, int endIdx) {
        int count = 0;
        for (int i = startIdx; i < endIdx; i++) {
            if (dataList.get(i) == target) {
                count++;
            }
        }
        return count;
    }

}


