package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球最近修正算法 - 基于最近30次数据中频率最高的值，如果频率为0则选择区间中点
 */
public class RecentModifiedAlgorithm implements PredictionAlgorithm {
    private static final int RECENT_SIZE = 30;

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

        int startIdx = Math.max(0, dataList.size() - RECENT_SIZE);
        int maxFreq = 0;
        int result = (range.getMinimum() + range.getMaximum()) / 2;

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
        return result;
    }
}
