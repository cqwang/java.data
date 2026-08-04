package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 邻域聚集算法 - 优先选择与其他高频数相邻的值
 */
public class NeighborhoodClusterAlgorithm implements SingleAlgorithm {
    private static final int WINDOW_SIZE = 48;
    private static final int NEIGHBOR_RANGE = 3;

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

        // 首先计算每个值的频率
        java.util.Map<Integer, Integer> windowFreq = new java.util.HashMap<>();
        for (int j = startIdx; j < dataList.size(); j++) {
            int val = dataList.get(j);
            windowFreq.put(val, windowFreq.getOrDefault(val, 0) + 1);
        }

        // 计算每个值的邻域强度
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = windowFreq.getOrDefault(i, 0);
            if (freq == 0) continue;

            // 计算邻域强度：周围NEIGHBOR_RANGE内的频率总和
            int neighborStrength = 0;
            for (int j = Math.max(range.getMinimum(), i - NEIGHBOR_RANGE);
                 j <= Math.min(range.getMaximum(), i + NEIGHBOR_RANGE); j++) {
                neighborStrength += windowFreq.getOrDefault(j, 0);
            }

            // 评分 = 自身频率 + 邻域强度权重
            double score = freq * 2.0 + (neighborStrength - freq) * 0.8;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
