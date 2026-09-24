package cqwang.doubleball.test;

import cqwang.doubleball.FuturePredict;
import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.cache.preload.DoubleColorBallPreload;

import java.util.*;

/**
 * BatchBall 算法性能分析工具
 * 评估所有 BatchBall 算法与各个蓝球算法组合的收益
 */
public class BatchBallPerformanceAnalyzer {

    static class AlgorithmPerformance {
        String batchAlgorithm;
        String blueAlgorithm;
        int profit;

        AlgorithmPerformance(String batchAlgorithm, String blueAlgorithm, int profit) {
            this.batchAlgorithm = batchAlgorithm;
            this.blueAlgorithm = blueAlgorithm;
            this.profit = profit;
        }

        @Override
        public String toString() {
            return String.format("%-50s + %-30s = %7d", batchAlgorithm, blueAlgorithm, profit);
        }
    }

    public static void main(String[] args) {
        DoubleColorBallPreload.execute();

        System.out.println("========== BatchBall Algorithm Performance Analysis ==========\n");

        var batchAlgorithms = BatchBallAlgorithmFactory.getAlgorithmPool();
        var blueAlgorithms = new String[]{
            "BlueRecommend",
            "MaxFrequency",
            "ContinuityWeightFrequency"
        };

        List<AlgorithmPerformance> results = new ArrayList<>();

        System.out.println("Testing " + batchAlgorithms.size() + " batch algorithms × " + blueAlgorithms.length + " blue algorithms\n");

        int count = 0;
        int total = batchAlgorithms.size() * blueAlgorithms.length;

        for (var batchReg : batchAlgorithms) {
            for (var blueAlg : blueAlgorithms) {
                count++;
                System.out.print(String.format("[%3d/%3d] Testing %-50s + %-30s ... ",
                    count, total, batchReg.getAlgorithmName(), blueAlg));

                try {
                    int profit = FuturePredict.getProfit(batchReg.getAlgorithmName(), blueAlg);
                    System.out.println("Profit = " + profit);
                    results.add(new AlgorithmPerformance(batchReg.getAlgorithmName(), blueAlg, profit));
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        }

        // 排序显示结果
        System.out.println("\n========== Results Sorted by Profit ==========\n");
        results.sort((a, b) -> Integer.compare(b.profit, a.profit));

        System.out.println("Top 20 Performance:");
        for (int i = 0; i < Math.min(20, results.size()); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }

        System.out.println("\nBottom 10 Performance:");
        for (int i = Math.max(0, results.size() - 10); i < results.size(); i++) {
            System.out.println((results.size() - i) + ". " + results.get(i));
        }

        // 按 BatchBall 算法分组统计
        System.out.println("\n========== Average Profit by BatchBall Algorithm ==========\n");
        Map<String, List<Integer>> profitsByBatch = new HashMap<>();

        for (var perf : results) {
            profitsByBatch.computeIfAbsent(perf.batchAlgorithm, k -> new ArrayList<>())
                .add(perf.profit);
        }

        List<Map.Entry<String, List<Integer>>> batchEntries = new ArrayList<>(profitsByBatch.entrySet());
        batchEntries.sort((a, b) -> {
            int avgA = (int) a.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            int avgB = (int) b.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            return Integer.compare(avgB, avgA);
        });

        for (var entry : batchEntries) {
            List<Integer> profits = entry.getValue();
            int sum = profits.stream().mapToInt(Integer::intValue).sum();
            int avg = sum / profits.size();
            int max = profits.stream().mapToInt(Integer::intValue).max().orElse(0);
            int min = profits.stream().mapToInt(Integer::intValue).min().orElse(0);

            System.out.println(String.format("%-50s | Avg: %6d | Max: %6d | Min: %6d | Tests: %d",
                entry.getKey(), avg, max, min, profits.size()));
        }

        // 按蓝球算法分组统计
        System.out.println("\n========== Average Profit by Blue Algorithm ==========\n");
        Map<String, List<Integer>> profitsByBlue = new HashMap<>();

        for (var perf : results) {
            profitsByBlue.computeIfAbsent(perf.blueAlgorithm, k -> new ArrayList<>())
                .add(perf.profit);
        }

        List<Map.Entry<String, List<Integer>>> blueEntries = new ArrayList<>(profitsByBlue.entrySet());
        blueEntries.sort((a, b) -> {
            int avgA = (int) a.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            int avgB = (int) b.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
            return Integer.compare(avgB, avgA);
        });

        for (var entry : blueEntries) {
            List<Integer> profits = entry.getValue();
            int sum = profits.stream().mapToInt(Integer::intValue).sum();
            int avg = sum / profits.size();
            int max = profits.stream().mapToInt(Integer::intValue).max().orElse(0);
            int min = profits.stream().mapToInt(Integer::intValue).min().orElse(0);

            System.out.println(String.format("%-30s | Avg: %6d | Max: %6d | Min: %6d | Tests: %d",
                entry.getKey(), avg, max, min, profits.size()));
        }

        // 总体统计
        System.out.println("\n========== Overall Statistics ==========\n");
        int totalProfit = results.stream().mapToInt(r -> r.profit).sum();
        double avgProfit = results.stream().mapToInt(r -> r.profit).average().orElse(0);
        int maxProfit = results.stream().mapToInt(r -> r.profit).max().orElse(0);
        int minProfit = results.stream().mapToInt(r -> r.profit).min().orElse(0);

        System.out.println(String.format("Total Profit: %d", totalProfit));
        System.out.println(String.format("Average Profit: %.2f", avgProfit));
        System.out.println(String.format("Max Profit: %d", maxProfit));
        System.out.println(String.format("Min Profit: %d", minProfit));
        System.out.println(String.format("Total Tests: %d", results.size()));
    }
}
