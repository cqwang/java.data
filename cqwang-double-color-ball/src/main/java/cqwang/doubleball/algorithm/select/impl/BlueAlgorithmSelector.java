package cqwang.doubleball.algorithm.select.impl;

import cqwang.doubleball.algorithm.select.AlgorithmSelector;
import cqwang.doubleball.algorithm.select.ValueCalculator;
import cqwang.doubleball.model.DoubleColorBallItem;

/**
 * 篮球优先，挑选高优算法
 */
public class BlueAlgorithmSelector implements AlgorithmSelector {
    @Override
    public String getFilePath() {
        return "/BlueSelectedAlgorithm.json";
    }

    @Override
    public int getMaxCount() {
        return 8;
    }

    @Override
    public int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        return ValueCalculator.calculateRed(predictResult, target);
    }
}
