package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球极限近期算法 - 只关注最近15次数据
 */
public class RedExtremeRecentAlgorithm implements PredictionAlgorithm {
    private static final int EXTREME_WINDOW = 15;

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

        int startIdx = Math.max(0, dataList.size() - EXTREME_WINDOW);
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                }
            }

            if (freq > maxFreq) {
                maxFreq = freq;
                result = i;
            }
        }

        // 如果没有找到频数，使用全局最高频
        if (maxFreq == 0) {
            var freqMap = ballDataDetail.getDataFrequencyMap();
            for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
                int freq = freqMap.getOrDefault(i, 0);
                if (freq > maxFreq) {
                    maxFreq = freq;
                    result = i;
                }
            }
        }

        return result;
    }
}
