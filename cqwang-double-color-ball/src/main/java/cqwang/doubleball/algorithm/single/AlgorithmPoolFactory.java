package cqwang.doubleball.algorithm.single;

import cqwang.doubleball.algorithm.single.impl.*;
import cqwang.doubleball.algorithm.single.impl.milestone.*;


import java.util.Arrays;
import java.util.List;

/**
 * 算法池工厂 - 注册所有可用的预测算法
 */
public class AlgorithmPoolFactory {
    private static final AlgorithmRegistry[] ALGORITHMS = {
            new AlgorithmRegistry("FrequencyAnalysis", FrequencyAnalysisAlgorithm.class),
            new AlgorithmRegistry("IntervalDistribution", IntervalDistributionAlgorithm.class),
            new AlgorithmRegistry("Average", AverageAlgorithm.class),
            new AlgorithmRegistry("Median", MedianAlgorithm.class),
            new AlgorithmRegistry("RecentFrequency", RecentFrequencyAlgorithm.class),
            new AlgorithmRegistry("VarianceWeighted", VarianceWeightedAlgorithm.class),
            new AlgorithmRegistry("CyclicFrequency", CyclicFrequencyAlgorithm.class),
            new AlgorithmRegistry("CyclicFrequencyChange", CyclicFrequencyWeightAlgorithm.class),
            new AlgorithmRegistry("MissingValueCompensation", MissingValueCompensationAlgorithm.class),
            new AlgorithmRegistry("WeightedMovingAverage", WeightedMovingAverageAlgorithm.class),
            new AlgorithmRegistry("MinMaxScaling", MinMaxScalingAlgorithm.class),
            new AlgorithmRegistry("CosineSimilarity", CosineSimilarityAlgorithm.class),
            new AlgorithmRegistry("ExponentialSmoothing3", ExponentialSmoothing3Algorithm.class),
            new AlgorithmRegistry("ExponentialSmoothing5", ExponentialSmoothing5Algorithm.class),
            new AlgorithmRegistry("ProbabilityDistribution", ProbabilityDistributionAlgorithm.class),
            new AlgorithmRegistry("KNearestNeighbors3", KNearestNeighbors3Algorithm.class),
            new AlgorithmRegistry("KNearestNeighbors5", KNearestNeighbors5Algorithm.class),
            new AlgorithmRegistry("SeasonalMovingAverage52", SeasonalMovingAverage52Algorithm.class),
            new AlgorithmRegistry("SeasonalMovingAverage101", SeasonalMovingAverage101Algorithm.class),
            new AlgorithmRegistry("SeasonalMovingAverage206", SeasonalMovingAverage206Algorithm.class),
            new AlgorithmRegistry("RandomForestSimulation", RandomForestSimulationAlgorithm.class),
            new AlgorithmRegistry("XGBoostSimulation", XGBoostSimulationAlgorithm.class),
            new AlgorithmRegistry("LightGBMSimulation", LightGBMSimulationAlgorithm.class),
            new AlgorithmRegistry("LSTMSimulation", LSTMSimulationAlgorithm.class),
            new AlgorithmRegistry("LogisticRegressionSimulation", LogisticRegressionSimulationAlgorithm.class),
            new AlgorithmRegistry("FourierAnalysis", FourierAnalysisAlgorithm.class),
            new AlgorithmRegistry("Quartile", QuartileAlgorithm.class),
            new AlgorithmRegistry("DifferentialSmoothing", DifferentialSmoothingAlgorithm.class),
            new AlgorithmRegistry("SpectralClustering", SpectralClusteringAlgorithm.class),
            new AlgorithmRegistry("SVMSimulation4", SVMSimulation4Algorithm.class),
            new AlgorithmRegistry("SVMSimulation6", SVMSimulation6Algorithm.class),
            new AlgorithmRegistry("GaussianMixture", GaussianMixtureAlgorithm.class),
            new AlgorithmRegistry("HiddenMarkovModel", HiddenMarkovModelAlgorithm.class),
            new AlgorithmRegistry("BayesianNetworkDown", BayesianNetworkDownAlgorithm.class),
            new AlgorithmRegistry("RecentModified", RecentModifiedAlgorithm.class),
            new AlgorithmRegistry("DecayFrequency", DecayFrequencyAlgorithm.class),
            new AlgorithmRegistry("IntervalCycle", IntervalCycleAlgorithm.class),
            new AlgorithmRegistry("AbsenceCompensation", AbsenceCompensationAlgorithm.class),
            new AlgorithmRegistry("ProbabilityDistributionModified", ProbabilityDistributionModifiedAlgorithm.class),
            new AlgorithmRegistry("QuartileModified", QuartileModifiedAlgorithm.class),
            new AlgorithmRegistry("DualCycleFrequency", DualCycleFrequencyAlgorithm.class),
            new AlgorithmRegistry("HotColdMixed", HotColdMixedAlgorithm.class),
            new AlgorithmRegistry("WeightedMode", WeightedModeAlgorithm.class),
            new AlgorithmRegistry("StandardDeviationWeighted", StandardDeviationWeightedAlgorithm.class),
            new AlgorithmRegistry("StrongWeightedFrequency", StrongWeightedFrequencyAlgorithm.class),
            new AlgorithmRegistry("ExtremeFrequency", ExtremeFrequencyAlgorithm.class),
            new AlgorithmRegistry("RecentBurst", RecentBurstAlgorithm.class),
            new AlgorithmRegistry("ThreePhaseWeighted", ThreePhaseWeightedAlgorithm.class),
            new AlgorithmRegistry("AdaptiveThreshold", AdaptiveThresholdAlgorithm.class),
            new AlgorithmRegistry("ContinuousPreference", ContinuousPreferenceAlgorithm.class),
            new AlgorithmRegistry("AggressiveDecay", AggressiveDecayAlgorithm.class),
            new AlgorithmRegistry("UltimateFrequency", UltimateFrequencyAlgorithm.class),
            new AlgorithmRegistry("MidpointBalance", MidpointBalanceAlgorithm.class),
            new AlgorithmRegistry("FastDecay", FastDecayAlgorithm.class),
            new AlgorithmRegistry("HighFrequency", HighFrequencyAlgorithm.class),
            new AlgorithmRegistry("RecentWeighted", RecentWeightedAlgorithm.class),
            new AlgorithmRegistry("MultiCycle", MultiCycleAlgorithm.class),
            new AlgorithmRegistry("Interval", IntervalAlgorithm.class),
            new AlgorithmRegistry("ExtremeRecent", ExtremeRecentAlgorithm.class),
            new AlgorithmRegistry("DynamicAverage", DynamicAverageAlgorithm.class),
            new AlgorithmRegistry("Continuous", ContinuousAlgorithm.class),
            new AlgorithmRegistry("ThreeLayerWeighted", ThreeLayerWeightedAlgorithm.class),
            new AlgorithmRegistry("HybridOptimized", HybridOptimizedAlgorithm.class),
            new AlgorithmRegistry("BurstFrequency", BurstFrequencyAlgorithm.class),
            new AlgorithmRegistry("SmartAdaptive", SmartAdaptiveAlgorithm.class),
            new AlgorithmRegistry("UltraFastDecay", UltraFastDecayAlgorithm.class),
            new AlgorithmRegistry("ExtremeValue", ExtremeValueAlgorithm.class),
            new AlgorithmRegistry("FourSegmentWeighted", FourSegmentWeightedAlgorithm.class),
            new AlgorithmRegistry("PeakClustering", PeakClusteringAlgorithm.class),
            new AlgorithmRegistry("DistributionBalance", DistributionBalanceAlgorithm.class),
            new AlgorithmRegistry("VariableDecay", VariableDecayAlgorithm.class),
            new AlgorithmRegistry("FrequencySurge", FrequencySurgeAlgorithm.class),
            new AlgorithmRegistry("NeighborhoodCluster", NeighborhoodClusterAlgorithm.class),
            new AlgorithmRegistry("FiveLayerProgressive", FiveLayerProgressiveAlgorithm.class),
            new AlgorithmRegistry("SuperWeighted", SuperWeightedAlgorithm.class),
            new AlgorithmRegistry("UltimateOptimization", UltimateOptimizationAlgorithm.class),

    };

    public static List<AlgorithmRegistry> getAlgorithmPool() {
        return Arrays.asList(ALGORITHMS);
    }

    public static AlgorithmRegistry getAlgorithm(String name) {
        for (AlgorithmRegistry registry : ALGORITHMS) {
            if (registry.getName().equals(name)) {
                return registry;
            }
        }
        return null;
    }
}
