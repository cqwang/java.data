package cqwang.doubleball.algorithm;

/**
 * 算法
 */
public class AlgorithmRegistry {
    final String name;
    final Class<? extends PredictionAlgorithm> algorithmClass;

    AlgorithmRegistry(String name, Class<? extends PredictionAlgorithm> algorithmClass) {
        this.name = name;
        this.algorithmClass = algorithmClass;
    }
}
