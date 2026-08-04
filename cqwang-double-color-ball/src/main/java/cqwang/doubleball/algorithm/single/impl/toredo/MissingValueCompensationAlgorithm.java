package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 缺失值补偿算法 - 补偿出现次数较少的值
 */
public class MissingValueCompensationAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return findUnderrepresentedValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return findUnderrepresentedValue(blueBallDataDetail, blueRange);
    }

    private int findUnderrepresentedValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        int minFreq = Integer.MAX_VALUE;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = ballDataDetail.getDataFrequencyMap().getOrDefault(i, 0);
            if (freq < minFreq) {
                minFreq = freq;
                result = i;
            }
        }
        return result;
    }
}
