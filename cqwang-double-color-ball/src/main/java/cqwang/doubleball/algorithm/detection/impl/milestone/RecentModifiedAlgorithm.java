package cqwang.doubleball.algorithm.detection.impl.milestone;

import cqwang.doubleball.algorithm.detection.MathUtils;
import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球最近修正算法 - 基于最近30次数据中频率最高的值，如果频率为0则选择区间中点
 */
public class RecentModifiedAlgorithm implements PredictionAlgorithm {
    private static final int RECENT_SIZE = 30;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.recentModified(redBallDataDetail, redRange, RECENT_SIZE);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.recentModified(blueBallDataDetail, blueRange, RECENT_SIZE);
    }


}
