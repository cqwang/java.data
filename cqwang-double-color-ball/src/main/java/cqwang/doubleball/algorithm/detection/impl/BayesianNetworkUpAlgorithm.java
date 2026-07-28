package cqwang.doubleball.algorithm.detection.impl;

/**
 * 贝叶斯网络模拟算法 - 基于概率推理的预测
 */
public class BayesianNetworkUpAlgorithm extends BayesianNetworkDownAlgorithm {
    @Override
    int next(double nextSum, int nextCount) {
        return (int) Math.ceil(nextSum / nextCount);
    }
}
