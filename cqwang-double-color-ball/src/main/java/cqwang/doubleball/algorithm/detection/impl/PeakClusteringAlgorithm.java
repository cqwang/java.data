package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 峰值聚集算法 - 优先选择频率高的"峰值"
 */
public class PeakClusteringAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 42;

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

        // 计算窗口内每个值的出现情况
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            int lastPos = -1;
            int clusterCount = 0;

            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                    // 如果与上次出现距离在3以内，认为是一个集群
                    if (lastPos != -1 && j - lastPos <= 3) {
                        clusterCount++;
                    }
                    lastPos = j;
                }
            }

            // 评分 = 频率 + 集群数 * 权重
            double score = freq * 2.0 + clusterCount * 1.5;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
