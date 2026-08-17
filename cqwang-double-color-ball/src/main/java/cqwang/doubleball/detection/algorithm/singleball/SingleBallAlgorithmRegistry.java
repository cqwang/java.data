package cqwang.doubleball.detection.algorithm.singleball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SingleBallAlgorithmRegistry extends AlgorithmRegistry {
    /**
     * 算法实现类名称
     */
    @Getter
    private String algorithmName;

    @Getter
    @JsonIgnore
    private Class<? extends SingleBallAlgorithm> algorithmClass;
    @Getter
    @JsonIgnore
    private SingleBallAlgorithm instance;

    public SingleBallAlgorithmRegistry(String name, Class<? extends SingleBallAlgorithm> algorithmClass) {
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
