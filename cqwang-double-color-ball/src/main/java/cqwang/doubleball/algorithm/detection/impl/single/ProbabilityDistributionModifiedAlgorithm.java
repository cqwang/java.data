package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球概率分布修正算法 - 基于频率分布计算期望值
 */
public class ProbabilityDistributionModifiedAlgorithm implements PredictionAlgorithm {

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
        int totalCount = dataList.size();

        // 计算概率加权期望值
        double expectedValue = 0;
        double totalWeight = 0;

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = freqMap.getOrDefault(i, 0);
            if (freq > 0) {
                double probability = (double) freq / totalCount;
                // 使用平方权重增加频繁出现值的影响力
                double weight = probability * probability;
                expectedValue += i * weight;
                totalWeight += weight;
            }
        }

        if (totalWeight == 0) {
            return range.getMinimum();
        }

        int result = Math.round((float) (expectedValue / totalWeight));
        result = Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));

        return result;
    }
}
