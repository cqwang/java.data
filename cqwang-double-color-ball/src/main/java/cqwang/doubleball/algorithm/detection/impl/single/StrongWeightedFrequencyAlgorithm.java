package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球强加权频率算法 - 优先选择最近和历史频率都高的值
 */
public class StrongWeightedFrequencyAlgorithm implements PredictionAlgorithm {
    private static final int RECENT_WINDOW = 25;
    private static final int MEDIUM_WINDOW = 60;

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
        int mediumStart = Math.max(0, dataList.size() - MEDIUM_WINDOW);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            // 计算不同窗口的频率
            int recentFreq = 0;
            int mediumFreq = 0;
            int totalFreq = ballDataDetail.getDataFrequencyMap().getOrDefault(i, 0);

            for (int j = recentStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    recentFreq++;
                }
            }

            for (int j = mediumStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    mediumFreq++;
                }
            }

            // 强加权计算：近期权重最高
            double score = recentFreq * 5.0 + mediumFreq * 2.0 + totalFreq * 0.5;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
