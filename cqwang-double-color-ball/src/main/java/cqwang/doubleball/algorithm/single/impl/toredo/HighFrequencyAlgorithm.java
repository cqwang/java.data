package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球高频率优先算法 - 基于全局最高频率选择红球
 */
public class HighFrequencyAlgorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return findMaxFrequencyValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return findMaxFrequencyValue(blueBallDataDetail, blueRange);
    }

    private int findMaxFrequencyValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = ballDataDetail.getDataFrequencyMap().getOrDefault(i, 0);
            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }
        return result;
    }
}
