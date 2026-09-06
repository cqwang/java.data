package cqwang.doubleball.test;

import java.util.*;

/**
 * 特征向量生成器 - 用于 ML 模型训练
 */
public class FeatureVectorizer {

    /**
     * 训练数据集
     */
    public static class TrainingDataset {
        public List<FeatureVector> features;
        public List<Integer> labels;
        public List<Integer> ballNumbers;

        public TrainingDataset() {
            this.features = new ArrayList<>();
            this.labels = new ArrayList<>();
            this.ballNumbers = new ArrayList<>();
        }

        public DatasetStats getStats() {
            DatasetStats stats = new DatasetStats();
            stats.totalSamples = labels.size();
            stats.positiveSamples = (int) labels.stream().filter(l -> l > 0).count();
            stats.negativeSamples = stats.totalSamples - stats.positiveSamples;
            stats.positiveRatio = stats.totalSamples > 0 ?
                (double) stats.positiveSamples / stats.totalSamples : 0;
            return stats;
        }
    }

    /**
     * 数据集统计信息
     */
    public static class DatasetStats {
        public int totalSamples;
        public int positiveSamples;
        public int negativeSamples;
        public double positiveRatio;

        @Override
        public String toString() {
            return String.format("总样本: %d, 正样本: %d (%.2f%%), 负样本: %d",
                totalSamples, positiveSamples, positiveRatio * 100, negativeSamples);
        }
    }

    /**
     * 生成特征向量列表（用于测试）
     */
    public static List<FeatureVector> generateFeatureVectors(List<Integer> data, int maxBall) {
        List<FeatureVector> vectors = new ArrayList<>();
        for (int ball = 1; ball <= maxBall; ball++) {
            FeatureVector fv = FeatureVector.buildFromData(data, ball);
            fv.normalize();
            vectors.add(fv);
        }
        return vectors;
    }
}
