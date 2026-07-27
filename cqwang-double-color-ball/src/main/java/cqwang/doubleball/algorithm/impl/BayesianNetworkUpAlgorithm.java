package cqwang.doubleball.algorithm.impl;

import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 贝叶斯网络模拟算法 - 基于概率推理的预测
 */
public class BayesianNetworkUpAlgorithm extends BayesianNetworkDownAlgorithm {
    @Override
    int next(double nextSum, int nextCount) {
        return (int) Math.ceil(nextSum / nextCount);
    }
}
