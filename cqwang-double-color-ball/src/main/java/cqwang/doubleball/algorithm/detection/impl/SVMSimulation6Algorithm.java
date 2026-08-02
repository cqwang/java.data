package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.impl.milestone.SVMSimulation4Algorithm;

/**
 * 支持向量机模拟算法 - 基于边界优化的预测
 */
public class SVMSimulation6Algorithm extends SVMSimulation4Algorithm {
    @Override
    protected double getWeight() {
        return 0.6;
    }
}
