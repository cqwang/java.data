package cqwang.doubleball.v2.algorithm.singleball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.v2.algorithm.AlgorithmRegistry;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 单个球的预测算法注册表
 */
@NoArgsConstructor
public class SingleBallPredictAlgorithmRegistry extends AlgorithmRegistry {
    /**
     * 算法实现类名称
     */
    @Getter
    private String algorithmName;

    @Getter
    @JsonIgnore
    private Class<? extends SingleBallPredictAlgorithm> algorithmClass;
    @Getter
    @JsonIgnore
    private SingleBallPredictAlgorithm instance;

    public SingleBallPredictAlgorithmRegistry(String name, Class<? extends SingleBallPredictAlgorithm> algorithmClass) {
        this.algorithmName = name;
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
