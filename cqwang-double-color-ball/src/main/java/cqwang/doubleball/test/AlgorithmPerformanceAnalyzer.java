package cqwang.doubleball.test;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.cache.preload.DoubleColorBallPreload;

import java.util.*;

public class AlgorithmPerformanceAnalyzer {

    public static void main(String[] args) {
        DoubleColorBallPreload.execute();

        System.out.println("\n========== Algorithm Performance Analysis ==========\n");

        var algorithmPool = SingleBallAlgorithmFactory.getAlgorithmPool();
        var blueReg = SingleBallAlgorithmFactory.getAlgorithm("BlueRecommend");

        List<AlgorithmResult> results = new ArrayList<>();

        for (var redReg : algorithmPool) {
            // 跳过集成算法
            if ("EnsemblePredictor".equals(redReg.getAlgorithmName()) ||
                "AdvancedEnsemblePredictor".equals(redReg.getAlgorithmName()) ||
                "RandomForestPredictor".equals(redReg.getAlgorithmName())) {
                continue;
            }

            var algo = new DoubleColorAlgorithmRegistry(blueReg, redReg);
            int totalTests = 0;

            var allData = DoubleColorBallPreload.getAllData();
            int testStart = Math.max(10, allData.size() - 1000);

            for (int i = testStart; i < allData.size(); i++) {
                var predict = algo.predict(i, new PredictOption());
                totalTests++;
            }

            int profit = algo.getPredictResult().getProfit();
            int redHits = algo.getPredictResult().getHitRedTotalCount();
            int blueHits = algo.getPredictResult().getHitBlueTotalCount();

            double redHitRate = (double) redHits / totalTests * 100;
            double blueHitRate = (double) blueHits / totalTests * 100;

            results.add(new AlgorithmResult(redReg.getAlgorithmName(), redHitRate, blueHitRate, profit));
            System.out.println(String.format("%-40s | Red: %6.2f%% | Blue: %6.2f%% | Profit: %6d",
                redReg.getAlgorithmName(), redHitRate, blueHitRate, profit));
        }

        // 排序显示
        System.out.println("\n========== Top Performers (by Red Hit Rate) ==========\n");
        results.stream()
            .sorted(Comparator.comparingDouble(AlgorithmResult::getRedHitRate).reversed())
            .limit(5)
            .forEach(r -> System.out.println(String.format("%-40s | Red: %6.2f%%",
                r.name, r.redHitRate)));
    }

    static class AlgorithmResult {
        String name;
        double redHitRate;
        double blueHitRate;
        int profit;

        AlgorithmResult(String name, double redHitRate, double blueHitRate, int profit) {
            this.name = name;
            this.redHitRate = redHitRate;
            this.blueHitRate = blueHitRate;
            this.profit = profit;
        }

        double getRedHitRate() {
            return redHitRate;
        }
    }
}

