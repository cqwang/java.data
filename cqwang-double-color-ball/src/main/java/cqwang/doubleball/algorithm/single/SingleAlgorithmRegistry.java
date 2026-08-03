package cqwang.doubleball.algorithm.single;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.algorithm.AlgorithmRegistry;
import lombok.Getter;
import lombok.Setter;

/**
 * 算法
 */
public class SingleAlgorithmRegistry extends AlgorithmRegistry {
    @Getter
    @JsonIgnore
    private Class<? extends SingleAlgorithm> algorithmClass;
    @Getter
    @JsonIgnore
    private SingleAlgorithm instance;

    public SingleAlgorithmRegistry() {
    }

    public SingleAlgorithmRegistry(String name, Class<? extends SingleAlgorithm> algorithmClass) {
        super.setName(name);
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
