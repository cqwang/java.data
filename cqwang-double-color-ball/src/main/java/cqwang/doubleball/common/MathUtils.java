package cqwang.doubleball.common;

import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

public class MathUtils {

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

    public static int svmSimulation(BallDataDetail ballDataDetail, Range<Integer> range, double weight) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 寻找支持向量（边界值）
        int min = dataList.stream().mapToInt(Integer::intValue).min().orElse(range.getMinimum());
        int max = dataList.stream().mapToInt(Integer::intValue).max().orElse(range.getMaximum());

        // 支持向量机的决策边界：在min和max之间的加权中点
        int result = (int) Math.round(min * (1 - weight) + max * weight);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }

    public static int adaptiveThreshold(BallDataDetail ballDataDetail, Range<Integer> range, int analysisWindow) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - analysisWindow);

        // 计算45次窗口内每个数的频率
        double[] freqs = new double[range.getMaximum() - range.getMinimum() + 1];
        int validCount = 0;

        for (int j = startIdx; j < dataList.size(); j++) {
            int val = dataList.get(j);
            if (val >= range.getMinimum() && val <= range.getMaximum()) {
                freqs[val - range.getMinimum()]++;
                validCount++;
            }
        }

        // 计算平均频率和标准差
        double avgFreq = validCount > 0 ? (double) validCount / (range.getMaximum() - range.getMinimum() + 1) : 0;
        double variance = 0;
        for (double freq : freqs) {
            variance += Math.pow(freq - avgFreq, 2);
        }
        double stdDev = Math.sqrt(variance / (range.getMaximum() - range.getMinimum() + 1));

        // 动态阈值 = 平均频率 + 0.5*标准差
        double dynamicThreshold = avgFreq + 0.5 * stdDev;

        // 选择超过阈值且频率最高的
        double maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double freq = freqs[i - range.getMinimum()];
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }

        return result;
    }

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
     * 结合多个时间窗口的加权频率
     * @param ballDataDetail
     * @param range
     * @param periodList
     * @return
     */
    public static int ultimateFrequency(BallDataDetail ballDataDetail, Range<Integer> range, List<Integer> periodList) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int start1 = Math.max(0, dataList.size() - periodList.get(0));
        int start2 = Math.max(0, dataList.size() - periodList.get(1));
        int start3 = Math.max(0, dataList.size() - periodList.get(2));

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq1 = 0, freq2 = 0, freq3 = 0;

            for (int j = start1; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq1++;
            }

            for (int j = start2; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq2++;
            }

            for (int j = start3; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq3++;
            }

            // 权重：近期权重更高 (7:3:1)
            double score = freq1 * 7.0 + (freq2 - freq1) * 3.0 + (freq3 - freq2) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }

    public static int hybridOptimized(BallDataDetail ballDataDetail, Range<Integer> range, List<Integer> windowList) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int shortStart = Math.max(0, dataList.size() - windowList.get(0));
        int mediumStart = Math.max(0, dataList.size() - windowList.get(1));
        int longStart = Math.max(0, dataList.size() - windowList.get(2));

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int shortFreq = 0, mediumFreq = 0, longFreq = 0;

            for (int j = shortStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) shortFreq++;
            }

            for (int j = mediumStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) mediumFreq++;
            }

            for (int j = longStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) longFreq++;
            }

            // 多窗口加权：短期权重最高
            double score = shortFreq * 10.0 + (mediumFreq - shortFreq) * 4.0 + (longFreq - mediumFreq) * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }

    public static int recentBurst(BallDataDetail ballDataDetail, Range<Integer> range, List<Integer> windowList) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int burstStart = Math.max(0, dataList.size() - windowList.get(0));
        int secondaryStart = Math.max(0, dataList.size() - windowList.get(1));

        // 第一步：检查最近10次中是否有出现
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            for (int j = burstStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    return i; // 最近10次出现过，直接返回
                }
            }
        }

        // 第二步：在35次内找最高频数
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = secondaryStart; j < dataList.size(); j++) {
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

    public static int extremeRecent(BallDataDetail ballDataDetail, Range<Integer> range, int extremeWindow) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - extremeWindow);
        int maxFreq = 0;
        int result = range.getMinimum();

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

        // 如果没有找到频数，使用全局最高频
        if (maxFreq == 0) {
            var freqMap = ballDataDetail.getDataFrequencyMap();
            for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
                int freq = freqMap.getOrDefault(i, 0);
                if (freq > maxFreq) {
                    maxFreq = freq;
                    result = i;
                }
            }
        }

        return result;
    }

}
