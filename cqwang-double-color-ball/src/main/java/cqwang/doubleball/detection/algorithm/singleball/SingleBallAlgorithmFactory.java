package cqwang.doubleball.detection.algorithm.singleball;

import cqwang.doubleball.detection.algorithm.singleball.impl.*;

import java.util.Arrays;
import java.util.List;

public class SingleBallAlgorithmFactory {
    private static final SingleBallAlgorithmRegistry[] ALGORITHMS = {
            new SingleBallAlgorithmRegistry("ContinuityWeightFrequency", ContinuityWeightFrequency.class),
            new SingleBallAlgorithmRegistry("DistributionFrequency", DistributionFrequency.class),
            new SingleBallAlgorithmRegistry("MaxDistributionCumulativeWeightFrequency", MaxDistributionCumulativeWeightFrequency.class),
            new SingleBallAlgorithmRegistry("MaxDistributionSplitWeightFrequency", MaxDistributionSplitWeightFrequency.class),
            new SingleBallAlgorithmRegistry("MaxFrequency", MaxFrequency.class),
            new SingleBallAlgorithmRegistry("NeighborhoodCluster", NeighborhoodCluster.class),
            new SingleBallAlgorithmRegistry("RecentMaxFrequency", RecentMaxFrequency.class),
            new SingleBallAlgorithmRegistry("RecentMaxWeightFrequency", RecentMaxWeightFrequency.class),
            new SingleBallAlgorithmRegistry("SimilarityFrequency", SimilarityFrequency.class),
            new SingleBallAlgorithmRegistry("SurgeFrequency", SurgeFrequency.class),
            new SingleBallAlgorithmRegistry("SvmSimulation", SvmSimulation.class),
            new SingleBallAlgorithmRegistry("BlueRecommend", BlueRecommend.class),

    };

    public static List<SingleBallAlgorithmRegistry> getAlgorithmPool() {
        return Arrays.asList(ALGORITHMS);
    }

    public static SingleBallAlgorithmRegistry getAlgorithm(String name) {
        for (var registry : ALGORITHMS) {
            if (registry.getAlgorithmName().equals(name)) {
                return registry;
            }
        }
        return null;
    }
}
