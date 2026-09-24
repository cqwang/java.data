package cqwang.doubleball.detection.algorithm.batchball.imp;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 最近15期最高频率
 * 选择最近15期中频率最高的6个号码
 */
public class BatchRecentMaxFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        var sub = batchBall.sub(15);
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchRecentMaxFrequency(sub, range, option);
    }

    private static BatchResult batchRecentMaxFrequency(BatchBall sub, Range<Integer> range, BatchPredictOption option) {
        List<Map.Entry<Integer, Integer>> frequencyList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }
            int freq = sub.getFrequency(data);
            if (freq > 0) {
                frequencyList.add(new AbstractMap.SimpleEntry<>(data, freq));
            }
        }

        frequencyList.sort((a, b) -> {
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

        for (Map.Entry<Integer, Integer> entry : frequencyList) {
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
