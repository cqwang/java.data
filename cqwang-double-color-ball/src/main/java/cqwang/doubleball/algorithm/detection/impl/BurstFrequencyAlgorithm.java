package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 爆发型频率算法 - 关注最近的高频爆发
 */
public class BurstFrequencyAlgorithm implements PredictionAlgorithm {
    private static final int BURST_WINDOW = 12;
    private static final int STABLE_WINDOW = 50;

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
        int stableStart = Math.max(0, dataList.size() - STABLE_WINDOW);

        // 第一步：检查爆发窗口中最高频数
        int maxBurstFreq = 0;
        int burstResult = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int burstFreq = 0;
            for (int j = burstStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    burstFreq++;
                }
            }
            if (burstFreq > maxBurstFreq) {
                maxBurstFreq = burstFreq;
                burstResult = i;
            }
        }

        // 第二步：如果爆发值有出现，返回爆发值；否则返回稳定窗口的最高频数
        if (maxBurstFreq > 0) {
            return burstResult;
        }

        int maxStableFreq = 0;
        int stableResult = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int stableFreq = 0;
            for (int j = stableStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    stableFreq++;
                }
            }
            if (stableFreq > maxStableFreq) {
                maxStableFreq = stableFreq;
                stableResult = i;
            }
        }

        return stableResult;
    }
}
