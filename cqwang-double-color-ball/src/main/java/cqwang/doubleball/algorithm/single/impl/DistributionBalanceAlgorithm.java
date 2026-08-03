package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 分布平衡算法 - 在频率和稳定性之间寻找平衡
 */
public class DistributionBalanceAlgorithm implements SingleAlgorithm {

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

        var freqMap = ballDataDetail.getDataFrequencyMap();

        // 计算全局统计信息
        int totalCount = dataList.size();
        int maxFreq = 0;
        double avgFreq = 0;
        int appearCount = 0;

        for (int freq : freqMap.values()) {
            maxFreq = Math.max(maxFreq, freq);
            avgFreq += freq;
            appearCount++;
        }

        avgFreq = appearCount > 0 ? avgFreq / appearCount : 0;

        // 计算标准差
        double variance = 0;
        for (int freq : freqMap.values()) {
            variance += Math.pow(freq - avgFreq, 2);
        }
        double stdDev = Math.sqrt(variance / Math.max(1, appearCount));

        // 寻找最平衡的值
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = freqMap.getOrDefault(i, 0);
            if (freq == 0) continue;

            // 评分 = 频率 + 稳定性
            // 稳定性 = 频率偏离标准差越小越好
            double stability = 1.0 - Math.abs(freq - avgFreq) / (stdDev + 1.0);
            double score = freq + stability * 5.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
