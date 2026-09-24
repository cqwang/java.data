package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 差分加权频率
 * 多窗口差分频率：freq[12]*10 + (freq[20]-freq[12])*4 + (freq[40]-freq[20])*1
 * 只考虑每个窗口新增的频率
 */
public class BatchMaxDistributionSplitWeightFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchDistributionWeightSplit(batchBall, range, option);
    }

    private static BatchResult batchDistributionWeightSplit(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var subList = new BatchBall[3];
        subList[0] = batchBall.sub(12);
        subList[1] = batchBall.sub(20);
        subList[2] = batchBall.sub(40);

        var weightList = new double[]{10, 4, 1};

        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            double score = 0.0;
            // 第一个窗口直接计算
            score += subList[0].getFrequency(data) * weightList[0];
            // 后续窗口计算差分
            for (int index = 1; index < subList.length; index++) {
                int diff = subList[index].getFrequency(data) - subList[index - 1].getFrequency(data);
                score += diff * weightList[index];
            }

            if (score > 0) {
                scoreList.add(new AbstractMap.SimpleEntry<>(data, score));
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
