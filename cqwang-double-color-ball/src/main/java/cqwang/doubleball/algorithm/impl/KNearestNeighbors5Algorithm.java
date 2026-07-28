package cqwang.doubleball.algorithm.impl;

import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * K近邻算法 - 基于历史最相近数据的预测
 */
public class KNearestNeighbors5Algorithm extends KNearestNeighbors3Algorithm {
    private static final int K = 5;

    @Override
    protected int getK() {
        return super.getK();
    }
}
