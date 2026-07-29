package cqwang.doubleball.algorithm.detection.single.single;

import cqwang.doubleball.algorithm.detection.single.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * LightGBM模拟算法 - 轻量梯度提升的预测
 */
public class LightGBMSimulationAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByLightGBM(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByLightGBM(blueBallDataDetail, blueRange);
    }

    private int predictByLightGBM(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 分桶处理数据
        int bucketSize = 5;
        double sum = 0;
        int count = 0;

        for (int i = Math.max(0, dataList.size() - bucketSize); i < dataList.size(); i++) {
            sum += dataList.get(i);
            count++;
        }

        int result = (int) Math.round(sum / count);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
