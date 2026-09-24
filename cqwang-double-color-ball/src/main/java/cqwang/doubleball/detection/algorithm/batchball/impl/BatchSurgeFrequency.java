package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 频率突跃
 * 参考 SingleBall 的 SurgeFrequency 算法
 * 检测最近期相比中期频率的提升比例
 */
public class BatchSurgeFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchSurgeFrequency(batchBall, range, option);
    }

    private static BatchResult batchSurgeFrequency(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        int period = 30;
        var midBall = batchBall.sub(period);
        var recentBall = batchBall.sub(period / 2);

        // 计算所有号码的突跃分数
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            if (midBall.getFrequency(data) == 0) {
                continue;
            }

            // 检测频率突跃：最近一半的频率相比整体频率的提升
            double surgeRatio = recentBall.getFrequency(data) * 2.0 / midBall.getFrequency(data);
            double score = midBall.getFrequency(data) * (1.0 + Math.min(surgeRatio, 2.0) * 0.5);

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
