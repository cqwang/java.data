package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 集成算法
 * 综合多个算法的投票
 * 多窗口加权 + 连续性 + 邻域聚集 + 突跃检测
 */
public class BatchEnsembleOptimized implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchEnsemble(batchBall, range, option);
    }

    private static BatchResult batchEnsemble(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        Map<Integer, Double> scoreMap = new HashMap<>();

        // 方案1: 多窗口加权 (权重: 50%)
        addWeightedFrequencyScore(batchBall, range, scoreMap, 0.5);

        // 方案2: 连续性特征 (权重: 20%)
        addContinuityScore(batchBall, range, scoreMap, 0.2);

        // 方案3: 邻域聚集 (权重: 15%)
        addNeighborhoodScore(batchBall, range, scoreMap, 0.15);

        // 方案4: 频率突跃 (权重: 15%)
        addSurgeScore(batchBall, range, scoreMap, 0.15);

        // 排序
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>(scoreMap.entrySet());
        scoreList.sort((a, b) -> {
            double diff = b.getValue() - a.getValue();
            if (Math.abs(diff) < 1e-6) {
                return b.getKey() - a.getKey();
            }
            return diff > 0 ? 1 : -1;
        });

        // 选出前6个
        List<Integer> result = new ArrayList<>();
        Set<Integer> allowSet = option.getRedAllows();

        if (!allowSet.isEmpty()) {
            for (int value : allowSet) {
                if (result.size() < 6 && !option.isBlock(value)) {
                    result.add(value);
                }
            }
        }

        for (Map.Entry<Integer, Double> entry : scoreList) {
            if (result.size() >= 6) {
                break;
            }
            if (!option.isBlock(entry.getKey()) && !result.contains(entry.getKey())) {
                result.add(entry.getKey());
            }
        }

        Collections.sort(result);
        return new BatchResult(result, result.size() == 6);
    }

    private static void addWeightedFrequencyScore(BatchBall batchBall, Range<Integer> range,
                                                   Map<Integer, Double> scoreMap, double weight) {
        var subList = new BatchBall[]{
                batchBall.sub(5),
                batchBall.sub(12),
                batchBall.sub(20),
                batchBall.sub(40)
        };
        var weightList = new double[]{-2, 10, 3, 1};

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            double score = 0.0;
            for (int index = 0; index < subList.length; index++) {
                score += subList[index].getFrequency(data) * weightList[index];
            }
            scoreMap.merge(data, score * weight, Double::sum);
        }
    }

    private static void addContinuityScore(BatchBall batchBall, Range<Integer> range,
                                           Map<Integer, Double> scoreMap, double weight) {
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            int freq = batchBall.getFrequency(data);
            int maxContinuous = batchBall.getMaxContinuousFrequency(data);
            double score = (freq + maxContinuous * 2.0) * weight;
            scoreMap.merge(data, score, Double::sum);
        }
    }

    private static void addNeighborhoodScore(BatchBall batchBall, Range<Integer> range,
                                             Map<Integer, Double> scoreMap, double weight) {
        int period = 2;
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            int freq = batchBall.getFrequency(data);
            if (freq == 0) continue;

            int neighborStrength = 0;
            for (int j = Math.max(range.getMinimum(), data - period);
                 j <= Math.min(range.getMaximum(), data + period); j++) {
                neighborStrength += batchBall.getFrequency(j);
            }

            double score = (freq * 2.0 + (neighborStrength - freq) * 0.8) * weight;
            scoreMap.merge(data, score, Double::sum);
        }
    }

    private static void addSurgeScore(BatchBall batchBall, Range<Integer> range,
                                      Map<Integer, Double> scoreMap, double weight) {
        int period = 30;
        var midBall = batchBall.sub(period);
        var recentBall = batchBall.sub(period / 2);

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (midBall.getFrequency(data) == 0) continue;

            double surgeRatio = recentBall.getFrequency(data) * 2.0 / midBall.getFrequency(data);
            double score = midBall.getFrequency(data) * (1.0 + Math.min(surgeRatio, 2.0) * 0.5) * weight;
            scoreMap.merge(data, score, Double::sum);
        }
    }
}
