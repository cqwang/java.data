package cqwang.doubleball.algorithm.detection.single.single;

import cqwang.doubleball.algorithm.detection.single.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 频率分析算法 - 基于出现频率最高的数值进行预测
 * 单中算法不太合适
 */
public class FrequencyAnalysisAlgorithm implements PredictionAlgorithm {
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
