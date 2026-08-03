package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 频率突跃算法 - 检测并偏好频率的突跃点
 */
public class FrequencySurgeAlgorithm implements SingleAlgorithm {
    private static final int OBSERVATION_WINDOW = 55;

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

        int startIdx = Math.max(0, dataList.size() - OBSERVATION_WINDOW);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            int recentFreq = 0;
            int halfPoint = (dataList.size() + startIdx) / 2;

            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                    if (j >= halfPoint) {
                        recentFreq++;
                    }
                }
            }

            if (freq == 0) continue;

            // 检测频率突跃：最近一半的频率相比整体频率的提升
            double surgeRatio = recentFreq * 2.0 / freq;
            double score = freq * (1.0 + Math.min(surgeRatio, 2.0) * 0.5);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
