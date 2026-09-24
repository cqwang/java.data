package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 多窗口加权频率
 * 参考 SingleBall 的 RedRecommend 算法
 * 一次预测6个红球号码
 */
public class BatchRedRecommend implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchDistributionWeight(batchBall, range, option);
    }

    private static BatchResult batchDistributionWeight(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var subList = new BatchBall[]{
                batchBall.sub(5),
                batchBall.sub(12),
                batchBall.sub(20),
                batchBall.sub(40)
        };
        var weightList = new double[]{-2, 10, 3, 1};

        // 计算所有号码的分数
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            double score = 0.0;
            for (int index = 0; index < subList.length; index++) {
                score += subList[index].getFrequency(data) * weightList[index];
            }

            scoreList.add(new AbstractMap.SimpleEntry<>(data, score));
        }

        // 按分数降序排序
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

