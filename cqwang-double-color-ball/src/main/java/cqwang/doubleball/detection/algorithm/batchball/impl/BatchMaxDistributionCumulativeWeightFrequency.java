package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 累积加权频率
 * 多窗口累积频率：freq[12]*7 + freq[20]*3 + freq[40]*1
 */
public class BatchMaxDistributionCumulativeWeightFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchDistributionWeightCumulative(batchBall, range, option);
    }

    private static BatchResult batchDistributionWeightCumulative(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        var subList = new BatchBall[3];
        subList[0] = batchBall.sub(12);
        subList[1] = batchBall.sub(20);
        subList[2] = batchBall.sub(40);

        var weightList = new double[]{7, 3, 1};

        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            double score = 0.0;
            for (int index = 0; index < subList.length; index++) {
                score += subList[index].getFrequency(data) * weightList[index];
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
