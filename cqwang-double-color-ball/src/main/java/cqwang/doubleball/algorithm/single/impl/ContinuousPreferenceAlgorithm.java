package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球连续出现偏好算法 - 偏好最近连续出现过的值
 */
public class ContinuousPreferenceAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 40;

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

        int startIdx = Math.max(0, dataList.size() - WINDOW_SIZE);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int totalFreq = 0;
            int consecutiveCount = 0;
            int maxConsecutive = 0;

            boolean lastWasTarget = false;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    totalFreq++;
                    if (lastWasTarget) {
                        consecutiveCount++;
                    } else {
                        maxConsecutive = Math.max(maxConsecutive, consecutiveCount);
                        consecutiveCount = 1;
                    }
                    lastWasTarget = true;
                } else {
                    lastWasTarget = false;
                }
            }
            maxConsecutive = Math.max(maxConsecutive, consecutiveCount);

            // 评分 = 总频率 * (1 + 连续出现次数权重)
            double score = totalFreq * (1.0 + maxConsecutive * 0.5);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
