package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 支持向量机模拟算法 - 基于边界优化的预测
 */
public class SVMSimulation4Algorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictBySVM(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictBySVM(blueBallDataDetail, blueRange);
    }

    private int predictBySVM(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 寻找支持向量（边界值）
        int min = dataList.stream().mapToInt(Integer::intValue).min().orElse(range.getMinimum());
        int max = dataList.stream().mapToInt(Integer::intValue).max().orElse(range.getMaximum());

        // 支持向量机的决策边界：在min和max之间的加权中点
        double weight = getWeight();
        int result = (int) Math.round(min * (1 - weight) + max * weight);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }

    protected double getWeight() {
        return 0.4;// 更接近max的支持向量
    }
}
