package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球标准差加权算法 - 基于均值和标准差的加权选择
 */
public class StandardDeviationWeightedAlgorithm implements PredictionAlgorithm {

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
}
