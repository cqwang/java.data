package cqwang.doubleball.algorithm.single.impl;

/**
 * 指数平滑算法 - 基于指数加权的平滑预测
 */
public class ExponentialSmoothing3Algorithm extends ExponentialSmoothing5Algorithm {
    private static final double ALPHA = 0.3;

    @Override
    protected double getAlpha() {
        return ALPHA;
    }
}
