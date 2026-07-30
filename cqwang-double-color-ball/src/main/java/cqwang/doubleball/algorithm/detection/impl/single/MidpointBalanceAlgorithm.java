package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球中点平衡算法 - 在中点附近寻找最高频数
 */
public class MidpointBalanceAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 45;

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
        int midPoint = (range.getMinimum() + range.getMaximum()) / 2;

        // 分别计算两侧的评分
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq = 0;
            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    freq++;
                }
            }

            // 评分 = 频率 * (1 - 偏离中点的距离权重)
            int distance = Math.abs(i - midPoint);
            double distanceWeight = 1.0 - (double) distance / (range.getMaximum() - range.getMinimum());
            double score = freq * (0.7 + distanceWeight * 0.3);

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
