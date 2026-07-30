package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 谱聚类算法 - 基于数据聚类特征的预测
 */
public class SpectralClusteringAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictBySpectralClustering(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictBySpectralClustering(blueBallDataDetail, blueRange);
    }

    private int predictBySpectralClustering(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 找出数据的聚集中心
        int[] clusters = new int[3];
        int[] clusterCounts = {0, 0, 0};

        for (int val : dataList) {
            int clusterIdx = (val * 3) / (range.getMaximum() + 1);
            if (clusterIdx >= 3) clusterIdx = 2;
            clusters[clusterIdx] += val;
            clusterCounts[clusterIdx]++;
        }

        // 找频率最高的聚类
        int maxCount = 0;
        int result = range.getMinimum();
        for (int i = 0; i < 3; i++) {
            if (clusterCounts[i] > maxCount) {
                maxCount = clusterCounts[i];
                result = clusters[i] / Math.max(1, clusterCounts[i]);
            }
        }

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
