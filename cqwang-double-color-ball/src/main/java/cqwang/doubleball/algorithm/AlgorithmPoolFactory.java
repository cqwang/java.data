package cqwang.doubleball.algorithm;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithmRegistry;
import cqwang.doubleball.algorithm.relevance.impl.*;
import cqwang.doubleball.algorithm.single.SingleAlgorithmRegistry;
import cqwang.doubleball.algorithm.single.impl.*;
import cqwang.doubleball.algorithm.single.impl.milestone.*;

import java.util.Arrays;
import java.util.List;

/**
 * 算法池工厂 - 注册所有可用的预测算法
 */
public class AlgorithmPoolFactory {
    private static final SingleAlgorithmRegistry[] SINGLE_ALGORITHMS = {
            new SingleAlgorithmRegistry("FrequencyAnalysis", FrequencyAnalysisAlgorithm.class),
            new SingleAlgorithmRegistry("IntervalDistribution", IntervalDistributionAlgorithm.class),
            new SingleAlgorithmRegistry("Average", AverageAlgorithm.class),
            new SingleAlgorithmRegistry("Median", MedianAlgorithm.class),
            new SingleAlgorithmRegistry("RecentFrequency", RecentFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("VarianceWeighted", VarianceWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("CyclicFrequency", CyclicFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("CyclicFrequencyChange", CyclicFrequencyWeightAlgorithm.class),
            new SingleAlgorithmRegistry("MissingValueCompensation", MissingValueCompensationAlgorithm.class),
            new SingleAlgorithmRegistry("WeightedMovingAverage", WeightedMovingAverageAlgorithm.class),
            new SingleAlgorithmRegistry("MinMaxScaling", MinMaxScalingAlgorithm.class),
            new SingleAlgorithmRegistry("CosineSimilarity", CosineSimilarityAlgorithm.class),
            new SingleAlgorithmRegistry("ExponentialSmoothing3", ExponentialSmoothing3Algorithm.class),
            new SingleAlgorithmRegistry("ExponentialSmoothing5", ExponentialSmoothing5Algorithm.class),
            new SingleAlgorithmRegistry("ProbabilityDistribution", ProbabilityDistributionAlgorithm.class),
            new SingleAlgorithmRegistry("KNearestNeighbors3", KNearestNeighbors3Algorithm.class),
            new SingleAlgorithmRegistry("KNearestNeighbors5", KNearestNeighbors5Algorithm.class),
            new SingleAlgorithmRegistry("SeasonalMovingAverage52", SeasonalMovingAverage52Algorithm.class),
            new SingleAlgorithmRegistry("SeasonalMovingAverage101", SeasonalMovingAverage101Algorithm.class),
            new SingleAlgorithmRegistry("SeasonalMovingAverage206", SeasonalMovingAverage206Algorithm.class),
            new SingleAlgorithmRegistry("RandomForestSimulation", RandomForestSimulationAlgorithm.class),
            new SingleAlgorithmRegistry("XGBoostSimulation", XGBoostSimulationAlgorithm.class),
            new SingleAlgorithmRegistry("LightGBMSimulation", LightGBMSimulationAlgorithm.class),
            new SingleAlgorithmRegistry("LSTMSimulation", LSTMSimulationAlgorithm.class),
            new SingleAlgorithmRegistry("LogisticRegressionSimulation", LogisticRegressionSimulationAlgorithm.class),
            new SingleAlgorithmRegistry("FourierAnalysis", FourierAnalysisAlgorithm.class),
            new SingleAlgorithmRegistry("Quartile", QuartileAlgorithm.class),
            new SingleAlgorithmRegistry("DifferentialSmoothing", DifferentialSmoothingAlgorithm.class),
            new SingleAlgorithmRegistry("SpectralClustering", SpectralClusteringAlgorithm.class),
            new SingleAlgorithmRegistry("SVMSimulation4", SVMSimulation4Algorithm.class),
            new SingleAlgorithmRegistry("SVMSimulation6", SVMSimulation6Algorithm.class),
            new SingleAlgorithmRegistry("GaussianMixture", GaussianMixtureAlgorithm.class),
            new SingleAlgorithmRegistry("HiddenMarkovModel", HiddenMarkovModelAlgorithm.class),
            new SingleAlgorithmRegistry("BayesianNetworkDown", BayesianNetworkDownAlgorithm.class),
            new SingleAlgorithmRegistry("RecentModified", RecentModifiedAlgorithm.class),
            new SingleAlgorithmRegistry("DecayFrequency", DecayFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("IntervalCycle", IntervalCycleAlgorithm.class),
            new SingleAlgorithmRegistry("AbsenceCompensation", AbsenceCompensationAlgorithm.class),
            new SingleAlgorithmRegistry("ProbabilityDistributionModified", ProbabilityDistributionModifiedAlgorithm.class),
            new SingleAlgorithmRegistry("QuartileModified", QuartileModifiedAlgorithm.class),
            new SingleAlgorithmRegistry("DualCycleFrequency", DualCycleFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("HotColdMixed", HotColdMixedAlgorithm.class),
            new SingleAlgorithmRegistry("WeightedMode", WeightedModeAlgorithm.class),
            new SingleAlgorithmRegistry("StandardDeviationWeighted", StandardDeviationWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("StrongWeightedFrequency", StrongWeightedFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("ExtremeFrequency", ExtremeFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("RecentBurst", RecentBurstAlgorithm.class),
            new SingleAlgorithmRegistry("ThreePhaseWeighted", ThreePhaseWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("AdaptiveThreshold", AdaptiveThresholdAlgorithm.class),
            new SingleAlgorithmRegistry("ContinuousPreference", ContinuousPreferenceAlgorithm.class),
            new SingleAlgorithmRegistry("AggressiveDecay", AggressiveDecayAlgorithm.class),
            new SingleAlgorithmRegistry("UltimateFrequency", UltimateFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("MidpointBalance", MidpointBalanceAlgorithm.class),
            new SingleAlgorithmRegistry("FastDecay", FastDecayAlgorithm.class),
            new SingleAlgorithmRegistry("HighFrequency", HighFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("RecentWeighted", RecentWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("MultiCycle", MultiCycleAlgorithm.class),
            new SingleAlgorithmRegistry("Interval", IntervalAlgorithm.class),
            new SingleAlgorithmRegistry("ExtremeRecent", ExtremeRecentAlgorithm.class),
            new SingleAlgorithmRegistry("DynamicAverage", DynamicAverageAlgorithm.class),
            new SingleAlgorithmRegistry("Continuous", ContinuousAlgorithm.class),
            new SingleAlgorithmRegistry("ThreeLayerWeighted", ThreeLayerWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("HybridOptimized", HybridOptimizedAlgorithm.class),
            new SingleAlgorithmRegistry("BurstFrequency", BurstFrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("SmartAdaptive", SmartAdaptiveAlgorithm.class),
            new SingleAlgorithmRegistry("UltraFastDecay", UltraFastDecayAlgorithm.class),
            new SingleAlgorithmRegistry("ExtremeValue", ExtremeValueAlgorithm.class),
            new SingleAlgorithmRegistry("FourSegmentWeighted", FourSegmentWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("PeakClustering", PeakClusteringAlgorithm.class),
            new SingleAlgorithmRegistry("DistributionBalance", DistributionBalanceAlgorithm.class),
            new SingleAlgorithmRegistry("VariableDecay", VariableDecayAlgorithm.class),
            new SingleAlgorithmRegistry("FrequencySurge", FrequencySurgeAlgorithm.class),
            new SingleAlgorithmRegistry("NeighborhoodCluster", NeighborhoodClusterAlgorithm.class),
            new SingleAlgorithmRegistry("FiveLayerProgressive", FiveLayerProgressiveAlgorithm.class),
            new SingleAlgorithmRegistry("SuperWeighted", SuperWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("UltimateOptimization", UltimateOptimizationAlgorithm.class),

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
