package cqwang.doubleball.algorithm.detection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * 算法
 */
public class AlgorithmRegistry {
    @Getter
    private String name;
    @Getter
    @JsonIgnore
    private Class<? extends PredictionAlgorithm> algorithmClass;
    @Getter
    @JsonIgnore
    private PredictionAlgorithm instance;

    /**
     * 历史预测价值合计
     */
    @Getter
    @Setter
    private int historyPredictValueSum = 0;

    /**
     * 历史命中次数
     */
    @Getter
    @Setter
    private int historyHitCount = 0;

    /**
     * 最大金额
     */
    @Getter
    @Setter
    private int maxAmount;

    AlgorithmRegistry() {
    }

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
