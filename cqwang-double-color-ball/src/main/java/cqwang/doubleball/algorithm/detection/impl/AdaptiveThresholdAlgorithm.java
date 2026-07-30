package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球动态阈值算法 - 根据近期数据动态调整选择阈值
 */
public class AdaptiveThresholdAlgorithm implements PredictionAlgorithm {
    private static final int ANALYSIS_WINDOW = 45;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictValue(blueBallDataDetail, blueRange);
    }

    private int predictValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - ANALYSIS_WINDOW);

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
}
