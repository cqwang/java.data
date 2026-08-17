package cqwang.doubleball.detection.algorithm.singleball;

import cqwang.doubleball.detection.algorithm.singleball.imp.*;
import cqwang.doubleball.detection.algorithm.singleball.impl.*;

import java.util.Arrays;
import java.util.List;

/**
 * 算法池工厂 - 注册所有可用的预测算法
 */
public class SingleBallAlgorithmRegistryFactory {

    private static final SingleBallPredictAlgorithmRegistry[] ALGORITHMS = {
            new SingleBallPredictAlgorithmRegistry("DistributionFrequency", DistributionFrequency.class),
            new SingleBallPredictAlgorithmRegistry("MaxDistributionCumulativeWeightFrequency", MaxDistributionCumulativeWeightFrequency.class),
            new SingleBallPredictAlgorithmRegistry("MaxDistributionSplitWeightFrequency", MaxDistributionSplitWeightFrequency.class),
            new SingleBallPredictAlgorithmRegistry("MaxFrequency", MaxFrequency.class),
            new SingleBallPredictAlgorithmRegistry("RecentMaxFrequency", RecentMaxFrequency.class),
            new SingleBallPredictAlgorithmRegistry("RecentMaxWeightFrequency", RecentMaxWeightFrequency.class),
            new SingleBallPredictAlgorithmRegistry("SurgeFrequency", SurgeFrequency.class),
            new SingleBallPredictAlgorithmRegistry("ContinuityWeightFrequency", ContinuityWeightFrequency.class),
            new SingleBallPredictAlgorithmRegistry("BluePredict", BluePredict.class),
            new SingleBallPredictAlgorithmRegistry("RedPredict", RedPredict.class),
            new SingleBallPredictAlgorithmRegistry("FirstRedPredict", FirstRedPredict.class),
            new SingleBallPredictAlgorithmRegistry("SimilarityFrequency", SimilarityFrequency.class),
            new SingleBallPredictAlgorithmRegistry("NeighborhoodCluster", NeighborhoodCluster.class),
            new SingleBallPredictAlgorithmRegistry("SvmSimulation", SvmSimulation.class),
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
