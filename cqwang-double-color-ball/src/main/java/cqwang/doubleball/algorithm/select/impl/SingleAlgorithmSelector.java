package cqwang.doubleball.algorithm.select.impl;

import cqwang.doubleball.algorithm.select.AlgorithmSelector;
import cqwang.doubleball.algorithm.select.ValueCalculator;
import cqwang.doubleball.model.DoubleColorBallItem;

/**
 * 单一算法推测红蓝
 */
public class SingleAlgorithmSelector implements AlgorithmSelector {

    @Override
    public String getFilePath() {
        return "/SingleSelectedAlgorithm.json";
    }

    @Override
    public int getMaxCount() {
        return 8;
    }

    @Override
    public int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        return ValueCalculator.calculate(predictResult, target);
    }

}
