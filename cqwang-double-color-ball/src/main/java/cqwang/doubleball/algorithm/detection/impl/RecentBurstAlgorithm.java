package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球近期爆发算法 - 优先选择最近10次中出现过的值
 */
public class RecentBurstAlgorithm implements PredictionAlgorithm {
    private static final int BURST_WINDOW = 10;
    private static final int SECONDARY_WINDOW = 35;

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

        int burstStart = Math.max(0, dataList.size() - BURST_WINDOW);
        int secondaryStart = Math.max(0, dataList.size() - SECONDARY_WINDOW);

        // 第一步：检查最近10次中是否有出现
        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            for (int j = burstStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    return i; // 最近10次出现过，直接返回
                }
            }
        }

        // 第二步：在35次内找最高频数
        int maxFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = secondaryStart; j < dataList.size(); j++) {
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
