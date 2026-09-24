package cqwang.doubleball.detection.algorithm.batchball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BatchBallAlgorithmRegistry extends AlgorithmRegistry {
    /**
     * 算法实现类名称
     */
    private String algorithmName;

    @JsonIgnore
    private Class<? extends BatchBallAlgorithm> algorithmClass;
    @JsonIgnore
    private BatchBallAlgorithm instance;

    public BatchBallAlgorithmRegistry(String name, Class<? extends BatchBallAlgorithm> algorithmClass) {
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
