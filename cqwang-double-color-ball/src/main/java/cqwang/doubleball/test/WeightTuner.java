package cqwang.doubleball.test;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;

import java.util.*;

public class WeightTuner {
    private static final int SAMPLE_SIZE = 500;

    public static void main(String[] args) {
        DoubleColorBallPreload.execute();

        System.out.println("\n========== Algorithm Weight Tuning ==========\n");

        var algorithmPool = SingleBallAlgorithmFactory.getAlgorithmPool();
        var blueReg = SingleBallAlgorithmFactory.getAlgorithm("BlueRecommend");

        // 计算每个算法的利润作为权重基准
        Map<String, Integer> profits = new HashMap<>();

        var allData = DoubleColorBallPreload.getAllData();
        int testStart = Math.max(10, allData.size() - SAMPLE_SIZE);

        for (var redReg : algorithmPool) {
            if ("EnsemblePredictor".equals(redReg.getAlgorithmName()) ||
                "AdvancedEnsemblePredictor".equals(redReg.getAlgorithmName()) ||
                "RandomForestPredictor".equals(redReg.getAlgorithmName())) {
                continue;
            }

            var algo = new DoubleColorAlgorithmRegistry(blueReg, redReg);

            for (int i = testStart; i < allData.size(); i++) {
                var predict = algo.predict(i, new PredictOption());
            }

            int profit = algo.getPredictResult().getProfit();
            profits.put(redReg.getAlgorithmName(), profit);
        }

        // 输出排序的利润和建议权重
        System.out.println("Algorithm Performance (last " + SAMPLE_SIZE + " tests):");
        System.out.println();

        profits.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .forEach(entry -> {
                int profit = entry.getValue();
                // 基于利润的权重：利润越高，权重越高（归一化到 1-10）
                double weight = Math.max(1.0, Math.min(10.0, (profit / 500.0)));

                System.out.println(String.format("%-40s | Profit: %6d | Weight: %.1f",
                    entry.getKey(), profit, weight));
            });

        // 生成改进的权重配置代码
        System.out.println("\n========== Suggested Weight Configuration ==========\n");
        int maxProfit = profits.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        profits.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .forEach(entry -> {
                double normalizedWeight = ((double) entry.getValue() / maxProfit) * 10.0;
                System.out.println(String.format("baseWeights.put(\"%s\", %.1f);",
                    entry.getKey(), Math.max(1.0, normalizedWeight)));
            });

        System.out.println("\n========== Summary ==========\n");
        double avgProfit = profits.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        System.out.println(String.format("Average Profit: %.0f", avgProfit));
        System.out.println("Best Algorithm: " + profits.entrySet().stream()
            .max(Comparator.comparingInt(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse("N/A"));
    }
}
