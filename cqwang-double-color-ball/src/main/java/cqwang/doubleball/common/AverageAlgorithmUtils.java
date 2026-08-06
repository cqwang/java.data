package cqwang.doubleball.common;

import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * 均值算法
 */
public class AverageAlgorithmUtils {

    /**
     * 标准差加权算法 - 基于均值和标准差的加权选择
     * @param ballDataDetail
     * @param range
     * @return
     */
    public static int standardDeviationWeighted(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 计算均值和标准差
        double mean = dataList.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double variance = dataList.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0);
        double stdDev = Math.sqrt(variance);

        var freqMap = ballDataDetail.getDataFrequencyMap();

        // 计算综合评分
        double bestScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = freqMap.getOrDefault(i, 0);
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
     * 使用加权平均替代简单平均，强调最近数据权重
     * @param
     * @param windowSize
     * @return
     */
    public static int weightAverage(BallDataDetail ballDataDetail, Range<Integer> redRange, int windowSize){
        double weighted = calculateWeightedAverage(
                ballDataDetail.getDataList(), windowSize);
        return Math.max(redRange.getMinimum(),
                Math.min(redRange.getMaximum(), (int) Math.round(weighted)));
    }

    /**
     * 中位数
     * @param ballDataDetail
     * @param redRange
     * @param windowSize
     * @return
     */
    public static int Median(BallDataDetail ballDataDetail, Range<Integer> redRange, int windowSize){
        double weighted = calculateMedian(
                ballDataDetail.getDataList(), windowSize);
        return Math.max(redRange.getMinimum(),
                Math.min(redRange.getMaximum(), (int) Math.round(weighted)));
    }

    /**
     * 中点平衡算法 - 在中点附近寻找最高频数
     * @param ballDataDetail
     * @param range
     * @param windowSize
     * @return
     */
    public static int midpointBalance(BallDataDetail ballDataDetail, Range<Integer> range, int windowSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - windowSize);
        int midPoint = (range.getMinimum() + range.getMaximum()) / 2;

        // 分别计算两侧的评分
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                }
            }

            // 评分 = 频率 * (1 - 偏离中点的距离权重)
            int distance = Math.abs(i - midPoint);
            double distanceWeight = 1.0 - (double) distance / (range.getMaximum() - range.getMinimum());
            double score = freq * (0.7 + distanceWeight * 0.3);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }


    /**
     * 计算加权平均
     */
    private static double calculateWeightedAverage(List<Integer> dataList, int windowSize) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int startIdx = Math.max(0, dataList.size() - windowSize);
        double sum = 0;
        double totalWeight = 0;

        for (int i = startIdx; i < dataList.size(); i++) {
            double weight = 1.0 + (double) (i - startIdx) / windowSize;
            sum += dataList.get(i) * weight;
            totalWeight += weight;
        }

        return sum / totalWeight;
    }

    /**
     * 计算中位数
     */
    private static int calculateMedian(List<Integer> dataList, int windowSize) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int startIdx = Math.max(0, dataList.size() - windowSize);
        List<Integer> subList = new ArrayList<>(dataList.subList(startIdx, dataList.size()));

        int mid = subList.size() / 2;
        if (subList.size() % 2 == 0) {
            return (subList.get(mid - 1) + subList.get(mid)) / 2;
        } else {
            return subList.get(mid);
        }
    }
}
