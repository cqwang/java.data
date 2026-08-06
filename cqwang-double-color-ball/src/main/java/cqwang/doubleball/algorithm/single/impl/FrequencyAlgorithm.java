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
public class FrequencyAlgorithm implements SingleAlgorithm {
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

        // 策略： 基于连续性的预测 - 识别连续出现的值 3670
        var continuity = FrequencyAlgorithmUtils.continuity(ballDataDetail, range, 12);

        // 策略：爆发检测 3700
        int recentBurstData = FrequencyAlgorithmUtils.burst(ballDataDetail, range, 12);

        // 策略：加权频率（强调最近） 3740
        int recentMaxWeightData = FrequencyAlgorithmUtils.powerWeight(ballDataDetail, range, 50, 0.99);

        // 策略：基于相似度的预测 - 多维度相似度计算 3895
        var recentSimilarityFrequencyData = FrequencyAlgorithmUtils.similarity(ballDataDetail, range, 40);

        // 策略：频率突跃算法 - 检测并偏好频率的突跃点 4105
        var recentSurgeData = FrequencyAlgorithmUtils.surge(ballDataDetail, range, 30);

        // 策略： 高频冷号分段混合算法 4230
        var hotColdMixedData = FrequencyAlgorithmUtils.hotColdMixed(ballDataDetail, range,5,20,50);

        // 结合多个时间窗口的加权频率 权重：近期权重更高 (7:3:1) 4300
        var ultimateFrequencyData = FrequencyAlgorithmUtils.distributionWeight(ballDataDetail, range, List.of(12, 20, 40), List.of(7,3,1));



//        return VoteUtils.randomVote(
//                List.of(globalMaxWeightData, continuity, recentBurstData,recentMaxWeightData,recentSimilarityFrequencyData, recentSurgeData, hotColdMixedData, ultimateFrequencyData),
//                List.of(1,1,3,3,3,20, 41,43)
//        );

        // 先投票
        // 一票否决 or 区间测算




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

