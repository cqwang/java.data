package cqwang.doubleball.test;

import java.util.*;
import java.util.stream.Collectors;

public class FeatureEngineer {

    /**
     * 计算移动平均
     */
    public static List<Double> calculateMovingAverage(List<Integer> data, int window) {
        List<Double> ma = new ArrayList<>();
        if (data.size() < window) return ma;

        for (int i = 0; i <= data.size() - window; i++) {
            double sum = 0;
            for (int j = i; j < i + window; j++) {
                sum += data.get(j);
            }
            ma.add(sum / window);
        }
        return ma;
    }

    /**
     * 计算指数平滑
     */
    public static List<Double> calculateExponentialSmoothing(List<Integer> data, double alpha) {
        List<Double> ema = new ArrayList<>();
        if (data.isEmpty()) return ema;

        ema.add((double) data.get(0));
        for (int i = 1; i < data.size(); i++) {
            double newEma = alpha * data.get(i) + (1 - alpha) * ema.get(i - 1);
            ema.add(newEma);
        }
        return ema;
    }

    /**
     * 计算偏度 (Skewness)
     */
    public static double calculateSkewness(List<Integer> data) {
        if (data.size() < 3) return 0.0;

        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double stdDev = calculateStandardDeviation(data);

        if (stdDev == 0) return 0.0;

        double sum = 0;
        for (int val : data) {
            sum += Math.pow((val - mean) / stdDev, 3);
        }

        return sum / data.size();
    }

    /**
     * 计算峰度 (Kurtosis)
     */
    public static double calculateKurtosis(List<Integer> data) {
        if (data.size() < 4) return 0.0;

        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double stdDev = calculateStandardDeviation(data);

        if (stdDev == 0) return 0.0;

        double sum = 0;
        for (int val : data) {
            sum += Math.pow((val - mean) / stdDev, 4);
        }

        return (sum / data.size()) - 3; // 超额峰度
    }

    /**
     * 计算标准差
     */
    public static double calculateStandardDeviation(List<Integer> data) {
        if (data.isEmpty()) return 0.0;

        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double variance = data.stream()
            .mapToDouble(x -> Math.pow(x - mean, 2))
            .average()
            .orElse(0);

        return Math.sqrt(variance);
    }

    /**
     * 计算自相关系数 (ACF - AutoCorrelation Function)
     */
    public static double calculateAutoCorrelation(List<Integer> data, int lag) {
        if (data.size() <= lag) return 0.0;

        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double c0 = 0, c_lag = 0;

        for (int i = 0; i < data.size() - lag; i++) {
            c0 += (data.get(i) - mean) * (data.get(i + lag) - mean);
        }

        for (int i = 0; i < data.size(); i++) {
            c_lag += Math.pow(data.get(i) - mean, 2);
        }

        return c_lag > 0 ? c0 / c_lag : 0.0;
    }

    /**
     * 检测异常值 (IQR方法)
     */
    public static List<Integer> detectOutliers(List<Integer> data) {
        List<Integer> sorted = new ArrayList<>(data);
        Collections.sort(sorted);

        int n = sorted.size();
        double q1 = sorted.get(n / 4);
        double q3 = sorted.get((3 * n) / 4);
        double iqr = q3 - q1;

        double lower = q1 - 1.5 * iqr;
        double upper = q3 + 1.5 * iqr;

        return data.stream()
            .filter(x -> x < lower || x > upper)
            .collect(Collectors.toList());
    }

    /**
     * 计算百分位数
     */
    public static double calculatePercentile(List<Integer> data, double percentile) {
        if (data.isEmpty()) return 0.0;

        List<Integer> sorted = new ArrayList<>(data);
        Collections.sort(sorted);

        int index = (int) (percentile * sorted.size());
        return sorted.get(Math.min(index, sorted.size() - 1));
    }

    /**
     * 计算变异系数 (Coefficient of Variation)
     */
    public static double calculateCoefficientOfVariation(List<Integer> data) {
        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        if (mean == 0) return 0.0;

        double stdDev = calculateStandardDeviation(data);
        return stdDev / mean;
    }

    /**
     * 计算Pearson相关系数
     */
    public static double calculatePearsonCorrelation(List<Integer> x, List<Integer> y) {
        if (x.size() != y.size() || x.isEmpty()) return 0.0;

        double meanX = x.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double meanY = y.stream().mapToDouble(Double::valueOf).average().orElse(0);

        double numerator = 0, sumSqX = 0, sumSqY = 0;

        for (int i = 0; i < x.size(); i++) {
            double dx = x.get(i) - meanX;
            double dy = y.get(i) - meanY;
            numerator += dx * dy;
            sumSqX += dx * dx;
            sumSqY += dy * dy;
        }

        double denominator = Math.sqrt(sumSqX * sumSqY);
        return denominator > 0 ? numerator / denominator : 0.0;
    }

    /**
     * 计算值的排名(Rank)
     */
    public static Map<Integer, Integer> calculateRanks(List<Integer> data) {
        Map<Integer, Integer> ranks = new HashMap<>();
        List<Integer> sorted = new ArrayList<>(data);
        Collections.sort(sorted);

        for (int i = 0; i < sorted.size(); i++) {
            ranks.put(sorted.get(i), i + 1);
        }

        return ranks;
    }

    /**
     * 归一化特征到 [0, 1]
     */
    public static List<Double> normalize(List<Integer> data) {
        if (data.isEmpty()) return new ArrayList<>();

        int min = Collections.min(data);
        int max = Collections.max(data);
        double range = max - min;

        if (range == 0) {
            return data.stream().map(x -> 0.5).collect(Collectors.toList());
        }

        return data.stream()
            .map(x -> (x - min) / range)
            .collect(Collectors.toList());
    }

    /**
     * 标准化特征到均值0、方差1
     */
    public static List<Double> standardize(List<Integer> data) {
        if (data.isEmpty()) return new ArrayList<>();

        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double stdDev = calculateStandardDeviation(data);

        if (stdDev == 0) {
            return data.stream().map(x -> 0.0).collect(Collectors.toList());
        }

        return data.stream()
            .map(x -> (x - mean) / stdDev)
            .collect(Collectors.toList());
    }

    /**
     * 计算趋势 (斜率)
     */
    public static double calculateTrend(List<Integer> data) {
        if (data.size() < 2) return 0.0;

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = data.size();

        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += data.get(i);
            sumXY += i * data.get(i);
            sumX2 += i * i;
        }

        double denominator = n * sumX2 - sumX * sumX;
        if (denominator == 0) return 0.0;

        return (n * sumXY - sumX * sumY) / denominator;
    }
}
