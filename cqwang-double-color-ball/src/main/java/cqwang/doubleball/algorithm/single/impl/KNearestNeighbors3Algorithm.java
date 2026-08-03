package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.PredictionAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * K近邻算法 - 基于历史最相近数据的预测
 */
public class KNearestNeighbors3Algorithm implements PredictionAlgorithm {
    private static final int K = 3;

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByKNN(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByKNN(blueBallDataDetail, blueRange);
    }

    private int predictByKNN(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int lastValue = dataList.get(dataList.size() - 1);
        double sum = 0;
        int count = 0;

        // 找最接近的K个值
        for (int i = Math.max(0, dataList.size() - getK()); i < dataList.size() - 1; i++) {
            sum += dataList.get(i);
            count++;
        }

        int result = (int) Math.round(sum / Math.max(1, count));
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }

    protected int getK(){
        return K;
    }
}
