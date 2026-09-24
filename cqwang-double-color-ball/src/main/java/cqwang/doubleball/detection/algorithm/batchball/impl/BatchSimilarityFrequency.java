package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 多维度相似度
 * 综合考虑：频率、邻近性、全局频率
 */
public class BatchSimilarityFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchSimilarity(batchBall, range, option);
    }

    private static BatchResult batchSimilarity(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var sub40 = batchBall.sub(40);
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            if (option.isBlock(candidate)) {
                continue;
            }

            double score = 0;

            // 频率得分
            score += sub40.getFrequency(candidate) * 10;

            // 邻近性得分
            for (var data : sub40.getDataList()) {
                int diff = Math.abs(data - candidate);
                if (diff <= 2) {
                    score += 5;
                } else if (diff <= 5) {
                    score += 2;
                }
            }

            // 全局频率补充
            int globalFreq = batchBall.getFrequency(candidate);
            score += globalFreq * 0.5;

            if (score > 0) {
                scoreList.add(new AbstractMap.SimpleEntry<>(candidate, score));
            }
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
