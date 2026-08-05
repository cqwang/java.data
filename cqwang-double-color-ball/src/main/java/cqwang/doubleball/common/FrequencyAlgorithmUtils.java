package cqwang.doubleball.common;

import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyAlgorithmUtils {
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
     * 查找最近样本中频次权重最高的数值
     * 考虑时间衰减和异常值处理
     */
    public static int findLatestMaxWeightFrequencyValue(BallDataDetail ballDataDetail, Range<Integer> range,
                                                        int sampleSize, double decayFactor) {
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
     * 基于爆发检测的预测 - 三层级
     * 爆发 > 稳定中期 > 长期
     */
    public static int predictByBurstDetection(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 爆发窗口
        int burstStart = Math.max(0, dataList.size() - sampleSize);
        return findMostFrequent(dataList, range, burstStart, dataList.size());
    }


    /**
     * 基于分布和权重的频次预测
     */
    public static int predictByDistributionFrequency(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 分三层：最近(12)、中期(50)、长期(100)
        int recentStart = Math.max(0, dataList.size() - 12);
        int mediumStart = Math.max(0, dataList.size() - 20);
        int longStart = Math.max(0, dataList.size() - 40);

        int recentBest = findMostFrequent(dataList, range, recentStart, dataList.size());
        int mediumBest = findMostFrequent(dataList, range, mediumStart, dataList.size());
        int longBest = findMostFrequent(dataList, range, longStart, dataList.size());

        // 权重：最近50%、中期30%、长期20%
        int recentFreq = countFrequency(dataList, recentBest, recentStart, dataList.size());
        int mediumFreq = countFrequency(dataList, mediumBest, mediumStart, dataList.size());
        int longFreq = countFrequency(dataList, longBest, longStart, dataList.size());

        double recentScore = recentFreq * 0.5;
        double mediumScore = mediumFreq * 0.3;
        double longScore = longFreq * 0.2;

        if (recentScore >= mediumScore && recentScore >= longScore) {
            return recentBest;
        } else if (mediumScore >= longScore) {
            return mediumBest;
        } else {
            return longBest;
        }
    }


    /**
     * 频率突跃算法 - 检测并偏好频率的突跃点
     *
     * @param ballDataDetail
     * @param range
     * @param sampleSize     55
     * @return
     */
    public static int predictSurge(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
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
     * 结合多个时间窗口的加权频率
     *
     * @param ballDataDetail
     * @param range
     * @param periodList
     * @return
     */
    public static int ultimateFrequency(BallDataDetail ballDataDetail, Range<Integer> range, List<Integer> periodList, List<Integer> weightList) {
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


