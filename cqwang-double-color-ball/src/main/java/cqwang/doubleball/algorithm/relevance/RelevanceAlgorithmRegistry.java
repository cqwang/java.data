package cqwang.doubleball.algorithm.relevance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.algorithm.AlgorithmRegistry;
import lombok.Getter;

public class RelevanceAlgorithmRegistry extends AlgorithmRegistry {
    @Getter
    @JsonIgnore
    private Class<? extends RelevanceAlgorithm> algorithmClass;
    @Getter
    @JsonIgnore
    private RelevanceAlgorithm instance;

    public RelevanceAlgorithmRegistry() {
    }

    public RelevanceAlgorithmRegistry(String name, Class<? extends RelevanceAlgorithm> algorithmClass) {
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
