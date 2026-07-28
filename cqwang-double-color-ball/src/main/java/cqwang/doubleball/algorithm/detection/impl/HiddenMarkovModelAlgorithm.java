package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 隐马尔可夫模型算法 - 基于状态转移的序列预测
 */
public class HiddenMarkovModelAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByHMM(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByHMM(blueBallDataDetail, blueRange);
    }

    private int predictByHMM(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.size() < 2) {
            return range.getMinimum();
        }

        // 计算转移概率（前一个值到后一个值的变化）
        double transitionSum = 0;
        for (int i = 1; i < dataList.size(); i++) {
            transitionSum += dataList.get(i) - dataList.get(i - 1);
        }

        double avgTransition = transitionSum / (dataList.size() - 1);
        int prediction = (int) Math.round(dataList.get(dataList.size() - 1) + avgTransition * 0.5);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }
}
