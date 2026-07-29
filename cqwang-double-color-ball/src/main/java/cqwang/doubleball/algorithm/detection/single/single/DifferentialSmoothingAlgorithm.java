package cqwang.doubleball.algorithm.detection.single.single;

import cqwang.doubleball.algorithm.detection.single.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 差分平滑算法 - 基于一阶差分的平滑预测
 */
public class DifferentialSmoothingAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByDifferentialSmoothing(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByDifferentialSmoothing(blueBallDataDetail, blueRange);
    }

    private int predictByDifferentialSmoothing(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.size() < 2) {
            return range.getMinimum();
        }

        // 计算差分序列
        double diffSum = 0;
        for (int i = 1; i < dataList.size(); i++) {
            diffSum += dataList.get(i) - dataList.get(i - 1);
        }

        double avgDiff = diffSum / (dataList.size() - 1);
        int prediction = (int) Math.round(dataList.get(dataList.size() - 1) + avgDiff);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }
}
