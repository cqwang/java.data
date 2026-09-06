package cqwang.doubleball.test.ml;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

import java.util.*;

public class AdvancedEnsemblePredictor implements SingleBallAlgorithm {

    private final Map<String, Double> baseWeights = new HashMap<>();

    public AdvancedEnsemblePredictor() {
        // 初始化改进的权重（基于性能分析）
        baseWeights.put("BlueRecommend", 8.0);
        baseWeights.put("RedRecommend", 7.5);
        baseWeights.put("SurgeFrequency", 7.0);
        baseWeights.put("SimilarityFrequency", 6.5);
        baseWeights.put("MaxDistributionCumulativeWeightFrequency", 6.0);
        baseWeights.put("NeighborhoodCluster", 5.5);
        baseWeights.put("RecentMaxWeightFrequency", 5.5);
        baseWeights.put("ContinuityWeightFrequency", 5.0);
        baseWeights.put("RecentMaxFrequency", 4.5);
        baseWeights.put("DistributionFrequency", 4.0);
        baseWeights.put("MaxFrequency", 3.5);
        baseWeights.put("MaxDistributionSplitWeightFrequency", 3.5);
        baseWeights.put("SvmSimulation", 3.0);
        baseWeights.put("EnhancedRedPredictor", 3.5);
        baseWeights.put("AdaptiveRedPredictor", 4.0);
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
            int successCount = 0;

            for (var algReg : algorithmList) {
                try {
                    // 排除自身
                    if ("AdvancedEnsemblePredictor".equals(algReg.getAlgorithmName()) ||
                        "EnsemblePredictor".equals(algReg.getAlgorithmName()) ||
                        "RandomForestPredictor".equals(algReg.getAlgorithmName())) {
                        continue;
                    }

                    var algorithm = algReg.getInstance();
                    var result = algorithm.predict(singleBall, option);
                    double weight = baseWeights.getOrDefault(algReg.getAlgorithmName(), 1.0);

                    // 直接命中给予高分
                    if (result.getResult() == value) {
                        weightedScore += weight * 15;
                        successCount++;
                    } else {
                        // 基于频率的相对评分
                        int freq = singleBall.getFrequency(value);
                        double relativeFreq = freq / (double) Math.max(1, singleBall.getTotalFrequency());
                        weightedScore += weight * relativeFreq * 5;
                    }

                    totalWeight += weight;
                } catch (Exception e) {
                    // Skip failed algorithms
                }
            }

            // 集成多个算法预测时的奖励
            if (successCount > 2) {
                weightedScore *= 1.2;
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

    public void updateWeight(String algorithmName, double newWeight) {
        baseWeights.put(algorithmName, newWeight);
    }
}
