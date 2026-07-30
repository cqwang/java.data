package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球快速衰减算法 - 极端衰减(0.88)，最近数据影响力极大
 */
public class FastDecayAlgorithm implements PredictionAlgorithm {
    private static final int SAMPLE_SIZE = 48;
    private static final double FAST_DECAY = 0.88;

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
                    weight += Math.pow(FAST_DECAY, decayCount);
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
