package cqwang.doubleball.algorithm;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithmRegistry;
import cqwang.doubleball.algorithm.relevance.impl.*;
import cqwang.doubleball.algorithm.single.SingleAlgorithmRegistry;
import cqwang.doubleball.algorithm.single.impl.FrequencyAlgorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 算法池工厂 - 注册所有可用的预测算法
 */
public class AlgorithmPoolFactory {
    private static final SingleAlgorithmRegistry[] SINGLE_ALGORITHMS = {
            new SingleAlgorithmRegistry("Frequency", FrequencyAlgorithm.class),

    };

    public static List<SingleAlgorithmRegistry> getSingleAlgorithmPool() {
        return Arrays.asList(SINGLE_ALGORITHMS);
    }

    public static SingleAlgorithmRegistry getSingleAlgorithm(String name) {
        for (SingleAlgorithmRegistry registry : SINGLE_ALGORITHMS) {
            if (registry.getName().equals(name)) {
                return registry;
            }
        }
        return null;
    }





    private static final RelevanceAlgorithmRegistry[] RELEVANCE_ALGORITHMS = {
            new RelevanceAlgorithmRegistry("CompositeRelevance", CompositeRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("CosineSimilarityRelevance", CosineSimilarityRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("EuclideanDistanceRelevance", EuclideanDistanceRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("PearsonCorrelationRelevance", PearsonCorrelationRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("IntervalGapRelevance", IntervalGapRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("IntervalFrequencyRelevance", IntervalFrequencyRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("DecayFrequencyRelevance", DecayFrequencyRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("ContinuousPatternRelevance", ContinuousPatternRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("ExtremeFrequencyRelevance", ExtremeFrequencyRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("RecentFrequencyRelevance", RecentFrequencyRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("BurstFrequencyRelevance", BurstFrequencyRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("AggressiveDecayRelevance", AggressiveDecayRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("ExtremeRecentRelevance", ExtremeRecentRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("WeightedModeRelevance", WeightedModeRelevanceAlgorithm.class),
            new RelevanceAlgorithmRegistry("HybridOptimizedRelevance", HybridOptimizedRelevanceAlgorithm.class),

    };

    public static List<RelevanceAlgorithmRegistry> getRelevanceAlgorithmPool() {
        return Arrays.asList(RELEVANCE_ALGORITHMS);
    }

    public static RelevanceAlgorithmRegistry getRelevanceAlgorithm(String name) {
        for (RelevanceAlgorithmRegistry registry : RELEVANCE_ALGORITHMS) {
            if (registry.getName().equals(name)) {
                return registry;
            }
        }
        return null;
    }




}
