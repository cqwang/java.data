package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 超级加权算法 - 综合频率、最近性和稳定性的超级加权
 */
public class SuperWeightedAlgorithm implements PredictionAlgorithm {
    private static final int RECENT_WINDOW = 32;
    private static final int STABLE_WINDOW = 70;

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

        int recentStart = Math.max(0, dataList.size() - RECENT_WINDOW);
        int stableStart = Math.max(0, dataList.size() - STABLE_WINDOW);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int recentFreq = 0;
            int stableFreq = 0;
            double recencyWeight = 0;

            for (int j = recentStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    recentFreq++;
                    // 越靠后权重越高
                    recencyWeight += 1.0 + (double) (j - recentStart) / (dataList.size() - recentStart);
                }
            }

            for (int j = stableStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    stableFreq++;
                }
            }

            // 综合评分 = 近期加权频率 + 稳定频率 + 连贯性奖励
            double consistency = recentFreq > 0 ? (double) recentFreq / (stableFreq > 0 ? stableFreq : 1) : 0;
            double score = recencyWeight * 3.0 + stableFreq * 1.0 + consistency * 2.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
