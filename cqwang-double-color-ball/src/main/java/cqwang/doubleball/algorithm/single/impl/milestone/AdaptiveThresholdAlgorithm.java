package cqwang.doubleball.algorithm.single.impl.milestone;

import cqwang.doubleball.common.MathUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球动态阈值算法 - 根据近期数据动态调整选择阈值
 */
public class AdaptiveThresholdAlgorithm implements SingleAlgorithm {
    private static final int ANALYSIS_WINDOW = 45;
    private static final int RECENT_SIZE = 30;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.recentModified(redBallDataDetail, redRange, RECENT_SIZE);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.adaptiveThreshold(blueBallDataDetail, blueRange, ANALYSIS_WINDOW);
    }


}
