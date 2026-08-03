package cqwang.doubleball.algorithm.single.impl.milestone;

import cqwang.doubleball.common.MathUtils;
import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 支持向量机模拟算法 - 基于边界优化的预测
 */
public class SVMSimulation4Algorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return MathUtils.svmSimulation(redBallDataDetail, redRange, getWeight());
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return MathUtils.svmSimulation(blueBallDataDetail, blueRange, getWeight());
    }



    protected double getWeight() {
        return 0.4;// 更接近max的支持向量
    }
}
