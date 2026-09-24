package cqwang.doubleball.detection.algorithm.batchball;

import cqwang.doubleball.detection.algorithm.batchball.impl.*;

import java.util.Arrays;
import java.util.List;

public class BatchBallAlgorithmFactory {
    private static final BatchBallAlgorithmRegistry[] ALGORITHMS = {
            new BatchBallAlgorithmRegistry("BatchRedRecommend", BatchRedRecommend.class),
            new BatchBallAlgorithmRegistry("BatchBlueRecommend", BatchBlueRecommend.class),
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