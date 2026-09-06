package cqwang.doubleball.test.ml;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

import java.util.*;

public class EnsemblePredictor implements SingleBallAlgorithm {
    private static final String NAME = "EnsemblePredictor";
    private static final String DESCRIPTION = "集成学习：融合现有 15 个算法的加权预测";

    private static final Map<String, Double> ALGORITHM_WEIGHTS = new HashMap<>();

    static {
        // 改进的权重（基于利润表现调整）
        // 高性能算法：BlueRecommend, RedRecommend, SurgeFrequency
        ALGORITHM_WEIGHTS.put("BlueRecommend", 9.0);
        ALGORITHM_WEIGHTS.put("RedRecommend", 8.5);
        ALGORITHM_WEIGHTS.put("SurgeFrequency", 8.0);
        ALGORITHM_WEIGHTS.put("SimilarityFrequency", 7.0);
        ALGORITHM_WEIGHTS.put("MaxDistributionCumulativeWeightFrequency", 6.5);
        ALGORITHM_WEIGHTS.put("MaxDistributionSplitWeightFrequency", 6.0);
        ALGORITHM_WEIGHTS.put("NeighborhoodCluster", 6.0);
        ALGORITHM_WEIGHTS.put("RecentMaxWeightFrequency", 6.0);
        ALGORITHM_WEIGHTS.put("SvmSimulation", 5.0);
        ALGORITHM_WEIGHTS.put("ContinuityWeightFrequency", 5.5);
        ALGORITHM_WEIGHTS.put("DistributionFrequency", 4.5);
        ALGORITHM_WEIGHTS.put("RecentMaxFrequency", 4.0);
        ALGORITHM_WEIGHTS.put("MaxFrequency", 3.5);
        ALGORITHM_WEIGHTS.put("AdaptiveRedPredictor", 5.0);
        ALGORITHM_WEIGHTS.put("EnhancedRedPredictor", 4.5);
    }

    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var range = Range.between(1, 33);
        var algorithmList = SingleBallAlgorithmFactory.getAlgorithmPool();

        Map<Integer, Double> scoreMap = new HashMap<>();

        for (int value = range.getMinimum(); value <= range.getMaximum(); value++) {
            if (option.isBlock(singleBall.getBallType(), singleBall.getIndex(), value)) {
                continue;
            }

            double weightedScore = 0.0;
            double totalWeight = 0.0;

            for (var algReg : algorithmList) {
                try {
                    // 排除自身，防止无限递归
                    if ("EnsemblePredictor".equals(algReg.getAlgorithmName())) {
                        continue;
                    }

                    var algorithm = algReg.getInstance();
                    var result = algorithm.predict(singleBall, option);
                    double weight = ALGORITHM_WEIGHTS.getOrDefault(algReg.getAlgorithmName(), 1.0);

                    if (result.getResult() == value) {
                        weightedScore += weight * 10;
                    } else {
                        int freq = singleBall.getFrequency(value);
                        double relativeFreq = freq / (double) Math.max(1, singleBall.getTotalFrequency());
                        weightedScore += weight * relativeFreq;
                    }

                    totalWeight += weight;
                } catch (Exception e) {
                    // Skip failed algorithms
                }
            }

            if (totalWeight > 0) {
                scoreMap.put(value, weightedScore / totalWeight);
            }
        }

        return scoreMap.entrySet()
            .stream()
            .max(Comparator.comparingDouble(Map.Entry::getValue))
            .map(entry -> new SingleResult(entry.getKey()))
            .orElse(new SingleResult(range.getMinimum()));
    }
}

