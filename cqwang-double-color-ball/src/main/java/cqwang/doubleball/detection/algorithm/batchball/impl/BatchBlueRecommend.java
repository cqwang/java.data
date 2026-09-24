package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;

public class BatchBlueRecommend implements BatchBallAlgorithm {
    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption batchPredictOption) {
        var option = batchPredictOption.clone();
        var range = Range.between(batchBall.getMinData(), batchBall.getMaxData());

        var resultList = new ArrayList<Integer>(6);
        for (int i = 0; i < 6; i++) {
            var result = surge(batchBall, range, option).getResult();
            resultList.add(result);
            option.addBlock(result);
        }
        return new BatchResult(resultList);
    }


    /**
     * 冷-》热
     * 热-》冷-》快热
     *
     * @param batchBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult surge(
            BatchBall batchBall,
            Range<Integer> range,
            BatchPredictOption option) {
        var sub30 = batchBall.sub(50);
        var sub15 = batchBall.sub(25);

        double maxScore = 0;
        boolean success = false;
        int result = range.getMinimum();
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            if (option.isBlock(i)) {
                continue;
            }
            if (option.isAllow(i)) {
                return new SingleResult(i, true);
            }

            if (sub30.getFrequency(i) == 0) {
                continue;
            }

            if (!becomeHot(batchBall, sub30, i, 1.5)) {
                continue;
            }

            // 检测频率突跃：最近的频率相比整体频率的提升
            double score = sub30.getFrequency(i) + scale(sub30, sub15, i, 1.0);

            if (score > maxScore) {
                maxScore = score;
                result = i;
                success = true;
            }
        }

        if (!success) {
            for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
                if (option.isBlock(i)) {
                    continue;
                }
                return new SingleResult(i, true);
            }
        }

        return new SingleResult(result, success);
    }

    public static boolean becomeHot(BatchBall longBall, BatchBall recentBall, int data, double factor) {
        return scale(longBall, recentBall, data, 1.0) > longBall.getFrequency(data) * factor;
    }


    /**
     * 把短期频次 按照比例扩展为长期频次
     *
     * @param longBall
     * @param recentBall
     * @param data
     * @return
     */
    public static double scale(BatchBall longBall, BatchBall recentBall, int data, double factor) {
        return recentBall.getFrequency(data) * calcFactor(longBall, recentBall);
    }

    public static double calcFactor(BatchBall longBall, BatchBall recentBall) {
        return 1.0 * longBall.getDataList().size() / recentBall.getDataList().size();
    }
}
