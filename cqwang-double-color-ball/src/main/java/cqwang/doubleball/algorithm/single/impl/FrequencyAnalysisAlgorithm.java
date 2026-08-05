package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.FrequencyAlgorithmUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

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

    /**
     *
     * @param ballDataDetail
     * @param range
     * @return
     */
    private int findOptimizedFrequencyValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        // 策略：全局频率 1105
        int globalMaxWeightData = FrequencyAlgorithmUtils.findMaxFrequencyValue(ballDataDetail, range);

        // 策略：基于分布和权重的频次预测 1340
        int recentDistributionMaxWeightData = FrequencyAlgorithmUtils.predictByDistributionFrequency(ballDataDetail, range);

        // 策略：爆发检测 3700
        int recentBurstData = FrequencyAlgorithmUtils.predictByBurstDetection(ballDataDetail, range, 12);

        // 策略：加权频率（强调最近） 3740
        int recentMaxWeightData = FrequencyAlgorithmUtils.findLatestMaxWeightFrequencyValue(ballDataDetail, range, 50, 0.99);

        // 策略：频率突跃算法 - 检测并偏好频率的突跃点 4105
        var recentSurgeData = FrequencyAlgorithmUtils.predictSurge(ballDataDetail, range, 30);

        // 结合多个时间窗口的加权频率 权重：近期权重更高 (7:3:1) 4300
        var ultimateFrequencyData = FrequencyAlgorithmUtils.ultimateFrequency(ballDataDetail, range, List.of(12, 20, 40), List.of(7,3,1));



        // 先投票
        // 一票否决 or




//        // 多数投票或选择最优
//        if (globalMaxWeightData == recentMaxWeightData || globalMaxWeightData == recentSurgeData) {
//            return globalMaxWeightData; // 优先选择最近和历史频率都高的值
//        } else if(recentDistributionMaxWeightData == recentMaxWeightData || recentDistributionMaxWeightData == recentSurgeData){
//            return recentDistributionMaxWeightData;
//        } else if (recentBurstData == recentMaxWeightData || recentBurstData == recentSurgeData) {
//            return recentBurstData;
//        } else if(recentMaxWeightData == recentSurgeData){
//            return recentSurgeData;
//        }

        // 默认使用加权频率结果
        return ultimateFrequencyData;
    }


}

