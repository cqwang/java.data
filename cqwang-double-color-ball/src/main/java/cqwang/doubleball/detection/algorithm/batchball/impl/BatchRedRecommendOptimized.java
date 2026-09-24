package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 改进的多窗口加权频率 v2
 * 基于 RedRecommend 的优化版本
 * 优化了权重系数和特征融合
 */
public class BatchRedRecommendOptimized implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchDistributionWeightOptimized(batchBall, range, option);
    }

    private static BatchResult batchDistributionWeightOptimized(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var subList = new BatchBall[]{
                batchBall.sub(5),
                batchBall.sub(12),
                batchBall.sub(20),
                batchBall.sub(40)
        };
        var weightList = new double[]{-2, 11, 3.5, 1};  // 调整权重

        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            double score = 0.0;
            for (int index = 0; index < subList.length; index++) {
                score += subList[index].getFrequency(data) * weightList[index];
            }

            // 特征1: 连续性加分
            int maxContinuous = batchBall.getMaxContinuousFrequency(data);
            if (maxContinuous > 1) {
                score += Math.min(2.0, maxContinuous * 0.4);
            }

            // 特征2: 全局频率比
            int globalFreq = batchBall.getFrequency(data);
            double avgFreq = batchBall.getAvgFrequency();
            if (globalFreq > avgFreq * 1.2) {
                score += 1.5;
            }

            scoreList.add(new AbstractMap.SimpleEntry<>(data, score));
        }

        scoreList.sort((a, b) -> {
            double diff = b.getValue() - a.getValue();
            if (Math.abs(diff) < 1e-6) {
                return b.getKey() - a.getKey();
            }
            return diff > 0 ? 1 : -1;
        });

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
            if (!result.contains(entry.getKey())) {
                result.add(entry.getKey());
            }
        }

        Collections.sort(result);
        return new BatchResult(result, result.size() == 6);
    }
}
