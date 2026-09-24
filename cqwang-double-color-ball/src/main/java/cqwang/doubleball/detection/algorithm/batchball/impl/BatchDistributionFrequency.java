package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 分布特征
 * 多维度条件过滤：全局热、长期热、中期稳、短期冷
 */
public class BatchDistributionFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchDistribution(batchBall, range, option);
    }

    private static BatchResult batchDistribution(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var sub5 = batchBall.sub(5);
        var sub20 = batchBall.sub(20);
        var sub70 = batchBall.sub(70);

        List<Map.Entry<Integer, Integer>> candidateList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            // 全局热：频率相对于平均频率 < 2倍
            if (batchBall.toAvgFrequencyRatio(data) < 2
                    && batchBall.getTotalFrequency() > 200
                    && batchBall.getMaxContinuousFrequency(data) < 2) {
                continue;
            }

            // 长期热：最近70期相对于平均频率 < 2倍
            if (sub70.toAvgFrequencyRatio(data) < 2) {
                continue;
            }

            // 中期稳：最近20期相对于平均频率在 0.5-2 范围内，连续不超过3
            if (sub20.toAvgFrequencyRatio(data) < 0.5
                    || sub20.toAvgFrequencyRatio(data) > 2
                    || sub20.getMaxContinuousFrequency(data) >= 3) {
                continue;
            }

            // 短期冷：最近5期相对于平均频率 < 0.8，连续不超过2
            if (sub5.toAvgFrequencyRatio(data) > 0.8
                    || sub5.getMaxContinuousFrequency(data) >= 2) {
                continue;
            }

            candidateList.add(new AbstractMap.SimpleEntry<>(data, batchBall.getFrequency(data)));
        }

        // 按频率降序排序
        candidateList.sort((a, b) -> {
            int freqDiff = b.getValue() - a.getValue();
            if (freqDiff == 0) {
                return b.getKey() - a.getKey();
            }
            return freqDiff;
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

        for (Map.Entry<Integer, Integer> entry : candidateList) {
            if (result.size() >= 6) {
                break;
            }
            if (!result.contains(entry.getKey())) {
                result.add(entry.getKey());
            }
        }

        // 如果不足6个，补充其他频率高的号码
        if (result.size() < 6) {
            candidateList.sort((a, b) -> b.getValue() - a.getValue());
            for (Map.Entry<Integer, Integer> entry : candidateList) {
                if (result.size() >= 6) {
                    break;
                }
                if (!result.contains(entry.getKey())) {
                    result.add(entry.getKey());
                }
            }
        }

        Collections.sort(result);
        return new BatchResult(result, result.size() == 6);
    }
}
