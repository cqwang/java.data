package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 连续性加权
 * 参考 SingleBall 的 ContinuityWeightFrequency 算法
 * score = frequency + maxContinuousFrequency * 3
 */
public class BatchContinuityWeight implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchContinuityWeight(batchBall, range, option);
    }

    private static BatchResult batchContinuityWeight(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        // 计算所有号码的连续性加权分数
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            int freq = batchBall.getFrequency(data);
            int maxContinuous = batchBall.getMaxContinuousFrequency(data);
            double score = freq * 1.0 + maxContinuous * 3.0;

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
