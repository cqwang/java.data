package cqwang.doubleball.algorithm.detection;

import lombok.Getter;
import lombok.Setter;

/**
 * 算法
 */
public class AlgorithmRegistry {
    @Getter
    final String name;
    @Getter
    final Class<? extends PredictionAlgorithm> algorithmClass;
    @Getter
    final PredictionAlgorithm instance;

    /**
     * 历史预测价值合计
     */
    @Getter
    @Setter
    private int historyPredictValueSum;

    AlgorithmRegistry(String name, Class<? extends PredictionAlgorithm> algorithmClass) {
        this.name = name;
        this.algorithmClass = algorithmClass;
        try {
            this.instance = algorithmClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
