package cqwang.doubleball.detection.algorithm.batchball;

import cqwang.doubleball.detection.algorithm.batchball.impl.*;
import java.util.Arrays;
import java.util.List;

public class BatchBallAlgorithmFactory {
    private static final BatchBallAlgorithmRegistry[] ALGORITHMS = {
            new BatchBallAlgorithmRegistry("BatchRedRecommend", BatchRedRecommend.class),
            new BatchBallAlgorithmRegistry("BatchMaxFrequency", BatchMaxFrequency.class),
            new BatchBallAlgorithmRegistry("BatchContinuityWeight", BatchContinuityWeight.class),
            new BatchBallAlgorithmRegistry("BatchSurgeFrequency", BatchSurgeFrequency.class),
            new BatchBallAlgorithmRegistry("BatchNeighborhoodCluster", BatchNeighborhoodCluster.class),
            new BatchBallAlgorithmRegistry("BatchRecentMaxFrequency", BatchRecentMaxFrequency.class),
            new BatchBallAlgorithmRegistry("BatchRecentMaxWeightFrequency", BatchRecentMaxWeightFrequency.class),
            new BatchBallAlgorithmRegistry("BatchSimilarityFrequency", BatchSimilarityFrequency.class),
            new BatchBallAlgorithmRegistry("BatchDistributionFrequency", BatchDistributionFrequency.class),
            new BatchBallAlgorithmRegistry("BatchMaxDistributionCumulativeWeightFrequency", BatchMaxDistributionCumulativeWeightFrequency.class),
            new BatchBallAlgorithmRegistry("BatchMaxDistributionSplitWeightFrequency", BatchMaxDistributionSplitWeightFrequency.class),
            new BatchBallAlgorithmRegistry("BatchRedRecommendOptimized", BatchRedRecommendOptimized.class),
            new BatchBallAlgorithmRegistry("BatchEnsembleOptimized", BatchEnsembleOptimized.class),
            new BatchBallAlgorithmRegistry("BatchHotColdTransition", BatchHotColdTransition.class),
    };

    public static List<BatchBallAlgorithmRegistry> getAlgorithmPool() {
        return Arrays.asList(ALGORITHMS);
    }

    public static BatchBallAlgorithmRegistry getAlgorithm(String name) {
        for (var registry : ALGORITHMS) {
            if (registry.getAlgorithmName().equals(name)) {
                return registry;
            }
        }
        return null;
    }
}