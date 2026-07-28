package cqwang.doubleball.algorithm.impl;

import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 支持向量机模拟算法 - 基于边界优化的预测
 */
public class SVMSimulation6Algorithm extends SVMSimulation4Algorithm {
    @Override
    protected double getWeight() {
        return 0.6;
    }
}
