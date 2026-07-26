package cqwang.doubleball.algorithm;

import java.util.List;

/**
 *
 */
public class AlgorithmPoolFactory {
    private static final AlgorithmRegistry[] ALGORITHMS = {
            new AlgorithmRegistry("XGBoost", XGBoostAlgorithm.class),
    }

    public static List<AlgorithmRegistry> getAlgorithmPool() {

    }

}
