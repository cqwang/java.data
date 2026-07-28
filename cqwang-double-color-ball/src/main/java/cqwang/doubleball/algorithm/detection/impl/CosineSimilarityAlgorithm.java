package cqwang.doubleball.algorithm.detection.impl;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 余弦相似度算法 - 基于最相似历史数据的预测
 */
public class CosineSimilarityAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictBySimilarity(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictBySimilarity(blueBallDataDetail, blueRange);
    }

    private int predictBySimilarity(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 取最后 10 个数据作为目标向量
        int windowSize = Math.min(10, dataList.size());
        double[] targetVector = new double[windowSize];
        for (int i = 0; i < windowSize; i++) {
            targetVector[i] = dataList.get(dataList.size() - windowSize + i);
        }

        double maxSimilarity = -1;
        int result = range.getMinimum();

        for (int candidate = range.getMinimum(); candidate <= range.getMaximum(); candidate++) {
            double similarity = 0;
            for (double val : targetVector) {
                similarity += Math.cos(Math.toRadians(Math.abs(val - candidate)));
            }
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                result = candidate;
            }
        }
        return result;
    }
}
