package cqwang.doubleball.algorithm.single.impl.milestone;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球递进衰减算法 - 更激进的衰减系数(0.92)
 */
public class AggressiveDecayAlgorithm implements PredictionAlgorithm {
    private static final int SAMPLE_SIZE = 55;
    private static final double AGGRESSIVE_DECAY = 0.92;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictValue(blueBallDataDetail, blueRange);
    }

    private int predictValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int startIdx = Math.max(0, dataList.size() - SAMPLE_SIZE);
        double maxWeight = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weight = 0;
            int decayCount = 0;
            for (int j = dataList.size() - 1; j >= startIdx; j--) {
                if (dataList.get(j) == i) {
                    weight += Math.pow(AGGRESSIVE_DECAY, decayCount);
                }
                decayCount++;
            }
            if (weight > maxWeight) {
                maxWeight = weight;
                result = i;
            }
        }

        return result;
    }
}
