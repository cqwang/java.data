package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 热度转变检测优化
 * 检测号码从冷到热的转变，并结合全局热度信息
 */
public class BatchHotColdTransition implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchHotColdTransition(batchBall, range, option);
    }

    private static BatchResult batchHotColdTransition(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var sub30 = batchBall.sub(30);
        var sub15 = batchBall.sub(15);
        var sub5 = batchBall.sub(5);

        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            if (sub30.getFrequency(data) == 0) {
                continue;
            }

            double score = 0;

            // 基础分：中期频率
            score += sub30.getFrequency(data);

            // 热度转变：短期相比中期的提升比例
            double heatRatio = sub15.getFrequency(data) * 2.0 / sub30.getFrequency(data);
            if (heatRatio > 1.0) {
                score += (heatRatio - 1.0) * sub30.getFrequency(data);
            }

            // 最近期热度：非常最近的频率
            int recentFreq = sub5.getFrequency(data);
            if (recentFreq > 0) {
                score += recentFreq * 1.5;
            }

            // 全局热度：全局频率相对于平均
            int globalFreq = batchBall.getFrequency(data);
            double avgFreq = batchBall.getAvgFrequency();
            if (globalFreq > avgFreq * 1.3) {
                score += 2.0;
            } else if (globalFreq > avgFreq) {
                score += 1.0;
            }

            // 连续性考虑
            int maxContinuous = batchBall.getMaxContinuousFrequency(data);
            if (maxContinuous > 1) {
                score += maxContinuous * 0.5;
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
