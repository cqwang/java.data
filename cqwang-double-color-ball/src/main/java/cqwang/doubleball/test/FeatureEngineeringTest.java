package cqwang.doubleball.test;

import cqwang.doubleball.detection.cache.preload.DoubleColorBallPreload;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.data.SplitBall;

public class FeatureEngineeringTest {
    public static void main(String[] args) {
        System.out.println("========== 特征工程测试 ==========\n");

        DoubleColorBallPreload.execute();
        var splitBall = new SplitBall(500);
        var redBall0 = splitBall.getRedBall(0);

        testFeatureEngineer(redBall0);
        testFeatureVector(redBall0);

        System.out.println("\n========== 测试完成 ==========");
    }

    private static void testFeatureEngineer(SingleBall singleBall) {
        System.out.println("测试 FeatureEngineer 工具方法：");
        var data = singleBall.getDataList();

        // 测试移动平均
        var ma5 = FeatureEngineer.calculateMovingAverage(data, 5);
        System.out.println("✓ 移动平均 (MA5)：最后3个值 = " +
            ma5.subList(Math.max(0, ma5.size() - 3), ma5.size()));

        // 测试指数平滑
        var ema = FeatureEngineer.calculateExponentialSmoothing(data, 0.3);
        System.out.println("✓ 指数平滑 (EMA)：最后3个值 = " +
            ema.subList(Math.max(0, ema.size() - 3), ema.size()));

        // 测试偏度和峰度
        double skewness = FeatureEngineer.calculateSkewness(data);
        double kurtosis = FeatureEngineer.calculateKurtosis(data);
        System.out.println("✓ 偏度 (Skewness) = " + String.format("%.4f", skewness));
        System.out.println("✓ 峰度 (Kurtosis) = " + String.format("%.4f", kurtosis));

        // 测试自相关
        double acf1 = FeatureEngineer.calculateAutoCorrelation(data, 1);
        double acf5 = FeatureEngineer.calculateAutoCorrelation(data, 5);
        System.out.println("✓ 自相关 (ACF lag=1) = " + String.format("%.4f", acf1));
        System.out.println("✓ 自相关 (ACF lag=5) = " + String.format("%.4f", acf5));

        // 测试异常值检测
        var outliers = FeatureEngineer.detectOutliers(data);
        System.out.println("✓ 异常值检测：发现 " + outliers.size() + " 个异常值");

        // 测试变异系数
        double cv = FeatureEngineer.calculateCoefficientOfVariation(data);
        System.out.println("✓ 变异系数 (CV) = " + String.format("%.4f", cv));

        // 测试标准差
        double stdDev = FeatureEngineer.calculateStandardDeviation(data);
        System.out.println("✓ 标准差 (StdDev) = " + String.format("%.4f", stdDev));

        // 测试趋势
        double trend = FeatureEngineer.calculateTrend(data);
        System.out.println("✓ 趋势 (Slope) = " + String.format("%.4f", trend));
    }

    private static void testFeatureVector(SingleBall singleBall) {
        System.out.println("\n测试 FeatureVector 构建：");
        var data = singleBall.getDataList();

        // 构建第一个球号的特征向量
        int testBallNumber = 15;
        FeatureVector fv = FeatureVector.buildFromData(data, testBallNumber);

        System.out.println("✓ 为球号 " + testBallNumber + " 构建 36 维特征向量");
        System.out.println("  - 维度: " + fv.getDimension());
        System.out.println("  - 前10个特征: [" + formatFeatures(fv.getFeatures(), 0, 10) + "]");
        System.out.println("  - 中间10个特征: [" + formatFeatures(fv.getFeatures(), 13, 23) + "]");
        System.out.println("  - 后10个特征: [" + formatFeatures(fv.getFeatures(), 26, 36) + "]");

        // 测试归一化
        FeatureVector fvNorm = FeatureVector.buildFromData(data, testBallNumber);
        fvNorm.normalize();
        System.out.println("\n✓ 归一化后的特征向量（应在 [0, 1]）:");
        System.out.println("  - 最小值: " + getMin(fvNorm.getFeatures()));
        System.out.println("  - 最大值: " + getMax(fvNorm.getFeatures()));

        // 测试标准化
        FeatureVector fvStd = FeatureVector.buildFromData(data, testBallNumber);
        fvStd.standardize();
        System.out.println("\n✓ 标准化后的特征向量（均值≈0, 方差≈1）:");
        System.out.println("  - 均值: " + String.format("%.6f", getMean(fvStd.getFeatures())));
        System.out.println("  - 方差: " + String.format("%.6f", getVariance(fvStd.getFeatures())));

        // 测试多个球号
        System.out.println("\n✓ 为多个球号构建特征向量:");
        for (int ball : new int[]{1, 5, 10, 15, 20, 25, 30, 33}) {
            FeatureVector fv2 = FeatureVector.buildFromData(data, ball);
            double freq = fv2.getFeatures()[2]; // 全局频率
            System.out.println("  - 球号 " + String.format("%2d", ball) + ": 全局频率 = " +
                String.format("%.1f", freq));
        }
    }

    private static String formatFeatures(double[] features, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < Math.min(end, features.length); i++) {
            if (i > start) sb.append(", ");
            sb.append(String.format("%.2f", features[i]));
        }
        return sb.toString();
    }

    private static double getMin(double[] array) {
        double min = Double.MAX_VALUE;
        for (double v : array) {
            if (v < min) min = v;
        }
        return min;
    }

    private static double getMax(double[] array) {
        double max = Double.MIN_VALUE;
        for (double v : array) {
            if (v > max) max = v;
        }
        return max;
    }

    private static double getMean(double[] array) {
        double sum = 0;
        for (double v : array) sum += v;
        return sum / array.length;
    }

    private static double getVariance(double[] array) {
        double mean = getMean(array);
        double sum = 0;
        for (double v : array) {
            sum += Math.pow(v - mean, 2);
        }
        return sum / array.length;
    }
}
