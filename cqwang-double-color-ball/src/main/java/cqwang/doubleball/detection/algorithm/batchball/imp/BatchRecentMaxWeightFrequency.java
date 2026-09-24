package cqwang.doubleball.detection.algorithm.batchball.imp;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 最近45期加权众数
 * 最近出现的频率最高值权重更高
 * score = sum(1.0 + j/dataList.size()) for each occurrence
 */
public class BatchRecentMaxWeightFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        var sub = batchBall.sub(45);
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchSquareWeight(sub, range, option);
    }

    private static BatchResult batchSquareWeight(BatchBall sub, Range<Integer> range, BatchPredictOption option) {
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }

            double weightedFreq = 0;
            int occurrenceCount = 0;

            for (int j = 0; j < sub.getDataList().size(); j++) {
                if (sub.getDataList().get(j) == data) {
                    double weight = 1.0 + (double) j / sub.getDataList().size();
                    weightedFreq += weight;
                    occurrenceCount++;
                }
            }

            if (occurrenceCount > 0) {
                weightedFreq /= occurrenceCount;
                weightedFreq *= occurrenceCount;
                scoreList.add(new AbstractMap.SimpleEntry<>(data, weightedFreq));
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
