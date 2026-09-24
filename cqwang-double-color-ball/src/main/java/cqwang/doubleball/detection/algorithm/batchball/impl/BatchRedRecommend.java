package cqwang.doubleball.detection.algorithm.batchball.impl;

import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;
import cqwang.doubleball.detection.model.result.SingleResult;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;

public class BatchRedRecommend implements BatchBallAlgorithm {
    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption batchPredictOption) {
        var option = batchPredictOption.clone();
        var range = Range.between(batchBall.getMinData(), batchBall.getMaxData());

        var resultList = new ArrayList<Integer>(6);
        for (int i = 0; i < 6; i++) {
            var result = distributionWeight(batchBall, range, true, option).getResult();
            resultList.add(result);
            option.addBlock(result);
        }
        return new BatchResult(resultList);
    }

    /**
     * 结合多个时间窗口的加权频次
     *
     * @param batchBall
     * @param range
     * @param option
     * @return
     */
    public static SingleResult distributionWeight(
            BatchBall batchBall,
            Range<Integer> range,
            boolean isCumulativeWeight,
            BatchPredictOption option) {
        var subList = new BatchBall[]{
                batchBall.sub(12),
                batchBall.sub(20),
                batchBall.sub(40),
                batchBall.sub(60)
        };
        var weightList = new double[]{-2, 10, 3, 1};

        double maxScore = 0;
        int result = range.getMinimum();
        boolean success = false;
        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) {
                continue;
            }
            if (option.isAllow(data)) {
                return new SingleResult(data, true);
            }


            var score = 0.0;
            for (var index = 0; index < subList.length; index++) {
                score += calculateScore(subList, isCumulativeWeight, weightList, data, index);
            }

            if (score > maxScore) {
                maxScore = score;
                result = data;
                success = true;
            }
        }

        if (!success) {
            for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
                if (option.isBlock(data)) {
                    continue;
                }
                return new SingleResult(data, true);
            }
        }

        return new SingleResult(result, success);
    }

    public static double calculateScore(BatchBall[] subList, boolean isCumulativeWeight, double[] weightList, int data, int index) {
        if (isCumulativeWeight) {
            return subList[index].getFrequency(data) * weightList[index];
        }

        if (index > 0) {
            return (subList[index].getFrequency(data) - subList[index - 1].getFrequency(data)) * weightList[index];
        } else {
            return subList[index].getFrequency(data) * weightList[index];
        }
    }
}
