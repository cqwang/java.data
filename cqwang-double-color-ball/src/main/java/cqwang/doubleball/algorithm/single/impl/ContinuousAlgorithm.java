package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球连续出现算法 - 偏好连续出现过的数值
 */
public class ContinuousAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 50;

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

            // 评分 = 总频率 + 连续出现次数权重
            double score = totalFreq * 2.0 + maxConsecutive * 3.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
