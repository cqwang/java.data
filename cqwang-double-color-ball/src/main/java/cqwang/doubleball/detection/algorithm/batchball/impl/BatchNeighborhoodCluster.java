package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 邻域聚集
 * 参考 SingleBall 的 NeighborhoodCluster 算法
 * 优先选择与其他高频数相邻的值
 */
public class BatchNeighborhoodCluster implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchNeighborhoodCluster(batchBall, range, option);
    }

    private static BatchResult batchNeighborhoodCluster(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        // 计算所有号码的邻域强度
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();
        int period = 2;

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            int freq = batchBall.getFrequency(data);
            if (freq == 0) {
                continue;
            }

            // 计算邻域强度：周围PERIOD范围内的频率总和
            int neighborStrength = 0;
            for (int j = Math.max(range.getMinimum(), data - period);
                 j <= Math.min(range.getMaximum(), data + period); j++) {
                neighborStrength += batchBall.getFrequency(j);
            }

            // 评分 = 自身频率 + 邻域强度权重
            double score = freq * 2.0 + (neighborStrength - freq) * 0.8;

            scoreList.add(new AbstractMap.SimpleEntry<>(data, score));
        }

        // 按分数降序排序
        scoreList.sort((a, b) -> {
            double scoreDiff = b.getValue() - a.getValue();
            if (Math.abs(scoreDiff) < 1e-6) {
                return b.getKey() - a.getKey();
            }
            return scoreDiff > 0 ? 1 : -1;
        });

        // 选出前6个
        List<Integer> result = new ArrayList<>();
        Set<Integer> allowSet = option.getRedAllows();

        // 如果有白名单，优先选白名单中的号码
        if (!allowSet.isEmpty()) {
            for (int value : allowSet) {
                if (result.size() < 6 && !option.isBlock(value)) {
                    result.add(value);
                }
            }
        }

        // 补充其他号码
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
