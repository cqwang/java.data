package cqwang.doubleball.test.model;

import java.time.LocalDateTime;
import java.util.*;

public class PerformanceMonitor {
    public static class PerformanceMetric {
        public LocalDateTime timestamp;
        public double redHitRate;
        public double blueHitRate;
        public int profit;
        public String modelName;

        public PerformanceMetric(String modelName, double redHitRate, double blueHitRate, int profit) {
            this.modelName = modelName;
            this.timestamp = LocalDateTime.now();
            this.redHitRate = redHitRate;
            this.blueHitRate = blueHitRate;
            this.profit = profit;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s - Red: %.2f%%, Blue: %.2f%%, Profit: %d",
                    timestamp, modelName, redHitRate * 100, blueHitRate * 100, profit);
        }
    }

    private final String modelName;
    private final List<PerformanceMetric> metrics = new ArrayList<>();
    private final double degradationThreshold;

    public PerformanceMonitor(String modelName, double degradationThreshold) {
        this.modelName = modelName;
        this.degradationThreshold = degradationThreshold;
    }

    public void recordMetric(double redHitRate, double blueHitRate, int profit) {
        metrics.add(new PerformanceMetric(modelName, redHitRate, blueHitRate, profit));
    }

    public boolean detectDegradation() {
        if (metrics.size() < 2) {
            return false;
        }

        PerformanceMetric latest = metrics.get(metrics.size() - 1);
        PerformanceMetric previous = metrics.get(metrics.size() - 2);

        double degradation = (previous.redHitRate - latest.redHitRate) / Math.max(0.001, previous.redHitRate);
        return degradation > degradationThreshold;
    }

    public double getAverageRedHitRate() {
        if (metrics.isEmpty()) {
            return 0;
        }
        return metrics.stream().mapToDouble(m -> m.redHitRate).average().orElse(0);
    }

    public int getTotalProfit() {
        return metrics.stream().mapToInt(m -> m.profit).sum();
    }

    public void printMetrics() {
        System.out.println("\n========== Performance Metrics for " + modelName + " ==========");
        metrics.forEach(System.out::println);
        System.out.println("Average Red Hit Rate: " + String.format("%.2f%%", getAverageRedHitRate() * 100));
        System.out.println("Total Profit: " + getTotalProfit());
    }

    public List<PerformanceMetric> getMetrics() {
        return new ArrayList<>(metrics);
    }
}
