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

        // 策略：支持向量机模拟算法 - 基于边界优化的预测
        var svmSimulationData = FrequencyAlgorithmUtils.svmSimulation(ballDataDetail, range, 50, 0.4); // 更接近max的支持向量

        // 策略：加权众数算法 - 最近出现的频率最高值权重更高
        var weightedMode = FrequencyAlgorithmUtils.weightedMode(ballDataDetail, range, 80);

        // 策略：近期加权算法 - 基于最近数据的加权频率 3935
        var recentWeight = FrequencyAlgorithmUtils.recentWeight(ballDataDetail, range, 45);

        // 策略：域聚集算法 - 优先选择与其他高频数相邻的值 3920
        var neighborhoodCluster = FrequencyAlgorithmUtils.neighborhoodCluster(ballDataDetail, range,5, 2);

        // 策略： 基于连续性的预测 - 识别连续出现的值 3670
        var continuity = FrequencyAlgorithmUtils.continuity(ballDataDetail, range, 12);



        // 策略：基于相似度的预测 - 多维度相似度计算 3895
        var recentSimilarityFrequencyData = FrequencyAlgorithmUtils.similarity(ballDataDetail, range, 40);

        // 策略：频率突跃算法 - 检测并偏好频率的突跃点 4105
        var recentSurgeData = FrequencyAlgorithmUtils.surge(ballDataDetail, range, 30);

        // 默认使用加权频率结果
        return weightedMode;
    }


}

