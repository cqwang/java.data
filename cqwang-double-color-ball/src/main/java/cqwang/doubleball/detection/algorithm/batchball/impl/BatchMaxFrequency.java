package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 批量红球预测 - 频率最高
 * 选择历史总体频率最高的6个号码
 */
public class BatchMaxFrequency implements BatchBallAlgorithm {

    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(batchBall.getMinData(), batchBall.getMaxData());
        return batchMaxFrequency(batchBall, range, option);
    }

    private static BatchResult batchMaxFrequency(BatchBall batchBall, Range<Integer> range, BatchPredictOption option) {
        // 计算所有号码的频率
        List<Map.Entry<Integer, Integer>> frequencyList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }
            int freq = batchBall.getFrequency(data);
            frequencyList.add(new AbstractMap.SimpleEntry<>(data, freq));
        }

        // 按频率降序排序
        frequencyList.sort((a, b) -> {
            int freqDiff = b.getValue() - a.getValue();
            if (freqDiff == 0) {
                return b.getKey() - a.getKey();
            }
            return freqDiff;
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
