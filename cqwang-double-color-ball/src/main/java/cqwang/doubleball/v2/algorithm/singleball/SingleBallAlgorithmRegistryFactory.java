package cqwang.doubleball.v2.algorithm.singleball;

import cqwang.doubleball.v2.algorithm.singleball.impl.*;

import java.util.Arrays;
import java.util.List;

/**
 * 算法池工厂 - 注册所有可用的预测算法
 */
public class SingleBallAlgorithmRegistryFactory {

    private static final SingleBallPredictAlgorithmRegistry[] ALGORITHMS = {
            new SingleBallPredictAlgorithmRegistry("MaxFrequency", MaxFrequencyImpl.class),
            new SingleBallPredictAlgorithmRegistry("RecentDistributionMaxWeightFrequency", RecentDistributionMaxWeightFrequencyImpl.class)
    };

    public static List<SingleBallPredictAlgorithmRegistry> getAlgorithmPool() {
        return Arrays.asList(ALGORITHMS);
    }

    public static SingleBallPredictAlgorithmRegistry getAlgorithm(String name) {
        for (var registry : ALGORITHMS) {
            if (registry.getAlgorithmName().equals(name)) {
                return registry;
            }
        }
        return null;
    }
}
