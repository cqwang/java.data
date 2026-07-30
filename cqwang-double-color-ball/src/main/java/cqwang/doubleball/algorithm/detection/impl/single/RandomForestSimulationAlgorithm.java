package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 随机森林模拟算法 - 基于集成多个随机规则的预测
 */
public class RandomForestSimulationAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByRandomForest(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByRandomForest(blueBallDataDetail, blueRange);
    }

    private int predictByRandomForest(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 融合多个规则的预测
        double pred1 = dataList.get(dataList.size() - 1); // 最后一个值
        double pred2 = dataList.stream().mapToDouble(Integer::doubleValue).average().orElse(range.getMinimum()); // 平均值
        double pred3 = dataList.stream().mapToInt(Integer::intValue).max().orElse(range.getMaximum()); // 最大值

        int result = (int) Math.round((pred1 + pred2 + pred3) / 3);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
