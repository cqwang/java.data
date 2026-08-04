package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 频率分析算法 - 优化版
 * 基于全局和局部频率分析的加权预测
 * 优化策略：
 * 1. 结合全局频率和近期频率
 * 2. 添加衰减权重，强调最近数据
 * 3. 多策略融合取最优结果
 */
public class FrequencyAnalysisAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return findOptimizedFrequencyValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return findOptimizedFrequencyValue(blueBallDataDetail, blueRange);
    }

    private int findOptimizedFrequencyValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        // 策略1：全局频率
        int result1 = findMaxFrequencyValue(ballDataDetail, range);

        // 策略2：加权频率（强调最近）
        int result2 = AlgorithmOptimizationUtils.predictByWeightedFrequency(
                ballDataDetail, range, 50, 0.99);

        // 策略3：爆发检测
        int result3 = AlgorithmOptimizationUtils.predictByBurstDetection(
                ballDataDetail, range);

        // 多数投票或选择最优
        if (result1 == result2 || result1 == result3) {
            return result1;
        } else if (result2 == result3) {
            return result2;
        }

        // 默认使用加权频率结果
        return result3;
    }

    private int findMaxFrequencyValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = ballDataDetail.getDataFrequencyWeightMap().getOrDefault(i, 0);
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }
        return result;
    }
}

