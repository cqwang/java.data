package cqwang.doubleball.algorithm;

import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithmRegistry;
import cqwang.doubleball.algorithm.relevance.impl.*;
import cqwang.doubleball.algorithm.single.SingleAlgorithmRegistry;
import cqwang.doubleball.algorithm.single.impl.milestone.*;
import cqwang.doubleball.algorithm.single.impl.FrequencyAlgorithm;
import cqwang.doubleball.algorithm.single.impl.toredo.*;

import java.util.Arrays;
import java.util.List;

/**
 * 算法池工厂 - 注册所有可用的预测算法
 */
public class AlgorithmPoolFactory {
    private static final SingleAlgorithmRegistry[] SINGLE_ALGORITHMS = {
            new SingleAlgorithmRegistry("Frequency", FrequencyAlgorithm.class),
            new SingleAlgorithmRegistry("IntervalDistribution", IntervalDistributionAlgorithm.class),
            new SingleAlgorithmRegistry("Median", MedianAlgorithm.class),
            new SingleAlgorithmRegistry("VarianceWeighted", VarianceWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("MissingValueCompensation", MissingValueCompensationAlgorithm.class),
            new SingleAlgorithmRegistry("WeightedMovingAverage", WeightedMovingAverageAlgorithm.class),
            new SingleAlgorithmRegistry("MinMaxScaling", MinMaxScalingAlgorithm.class),
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
            new SingleAlgorithmRegistry("Quartile", QuartileAlgorithm.class),
            new SingleAlgorithmRegistry("SpectralClustering", SpectralClusteringAlgorithm.class),
            new SingleAlgorithmRegistry("SVMSimulation4", SVMSimulation4Algorithm.class),
            new SingleAlgorithmRegistry("SVMSimulation6", SVMSimulation6Algorithm.class),
            new SingleAlgorithmRegistry("BayesianNetworkDown", BayesianNetworkDownAlgorithm.class),
            new SingleAlgorithmRegistry("RecentModified", RecentModifiedAlgorithm.class),
            new SingleAlgorithmRegistry("IntervalCycle", IntervalCycleAlgorithm.class),
            new SingleAlgorithmRegistry("ProbabilityDistributionModified", ProbabilityDistributionModifiedAlgorithm.class),
            new SingleAlgorithmRegistry("QuartileModified", QuartileModifiedAlgorithm.class),
            new SingleAlgorithmRegistry("WeightedMode", WeightedModeAlgorithm.class),
            new SingleAlgorithmRegistry("StandardDeviationWeighted", StandardDeviationWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("RecentBurst", RecentBurstAlgorithm.class),
            new SingleAlgorithmRegistry("ThreePhaseWeighted", ThreePhaseWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("AdaptiveThreshold", AdaptiveThresholdAlgorithm.class),
            new SingleAlgorithmRegistry("AggressiveDecay", AggressiveDecayAlgorithm.class),
            new SingleAlgorithmRegistry("MidpointBalance", MidpointBalanceAlgorithm.class),
            new SingleAlgorithmRegistry("RecentWeighted", RecentWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("MultiCycle", MultiCycleAlgorithm.class),
            new SingleAlgorithmRegistry("Interval", IntervalAlgorithm.class),
            new SingleAlgorithmRegistry("ExtremeRecent", ExtremeRecentAlgorithm.class),
            new SingleAlgorithmRegistry("ThreeLayerWeighted", ThreeLayerWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("HybridOptimized", HybridOptimizedAlgorithm.class),
            new SingleAlgorithmRegistry("SmartAdaptive", SmartAdaptiveAlgorithm.class),
            new SingleAlgorithmRegistry("UltraFastDecay", UltraFastDecayAlgorithm.class),
            new SingleAlgorithmRegistry("PeakClustering", PeakClusteringAlgorithm.class),
            new SingleAlgorithmRegistry("VariableDecay", VariableDecayAlgorithm.class),
            new SingleAlgorithmRegistry("NeighborhoodCluster", NeighborhoodClusterAlgorithm.class),
            new SingleAlgorithmRegistry("SuperWeighted", SuperWeightedAlgorithm.class),
            new SingleAlgorithmRegistry("UltimateOptimization", UltimateOptimizationAlgorithm.class),
            new SingleAlgorithmRegistry("IQR", IQRAlgorithm.class),
            new SingleAlgorithmRegistry("KalmanFilter", KalmanFilterAlgorithm.class),
            new SingleAlgorithmRegistry("LaggedValue", LaggedValueAlgorithm.class),
            new SingleAlgorithmRegistry("MaximumValue", MaximumValueAlgorithm.class),
            new SingleAlgorithmRegistry("MinimumValue", MinimumValueAlgorithm.class),
            new SingleAlgorithmRegistry("Mode", ModeAlgorithm.class),
            new SingleAlgorithmRegistry("MovingAverage", MovingAverageAlgorithm.class),
            new SingleAlgorithmRegistry("Percentile", PercentileAlgorithm.class),
            new SingleAlgorithmRegistry("PolynomialRegression", PolynomialRegressionAlgorithm.class),
            new SingleAlgorithmRegistry("RandomWalk", RandomWalkAlgorithm.class),
            new SingleAlgorithmRegistry("RangeAverage", RangeAverageAlgorithm.class),
            new SingleAlgorithmRegistry("Regression", RegressionAlgorithm.class),
            new SingleAlgorithmRegistry("Skewness", SkewnessAlgorithm.class),
            new SingleAlgorithmRegistry("SmoothingSpline", SmoothingSplineAlgorithm.class),
            new SingleAlgorithmRegistry("SpectralAnalysis", SpectralAnalysisAlgorithm.class),
            new SingleAlgorithmRegistry("StandardDeviation", StandardDeviationAlgorithm.class),
            new SingleAlgorithmRegistry("TrendAnalysis", TrendAnalysisAlgorithm.class),
            new SingleAlgorithmRegistry("TrimmedMean", TrimmedMeanAlgorithm.class),
            new SingleAlgorithmRegistry("Variance", VarianceAlgorithm.class),
            new SingleAlgorithmRegistry("Wavelet", WaveletAlgorithm.class),
            new SingleAlgorithmRegistry("WinsorizedMean", WinsorizedMeanAlgorithm.class),

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
