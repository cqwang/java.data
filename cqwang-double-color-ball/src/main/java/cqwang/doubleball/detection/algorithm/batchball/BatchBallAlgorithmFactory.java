package cqwang.doubleball.detection.algorithm.batchball;

import java.util.Arrays;
import java.util.List;

public class BatchBallAlgorithmFactory {
    private static final BatchBallAlgorithmRegistry[] ALGORITHMS = {

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