package cqwang.doubleball.test;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import cqwang.doubleball.detection.utils.ValueCalculator;
import cqwang.doubleball.detection.model.result.SingleResult;

import java.util.*;

public class WeightedEnsembleOptimizer {
    private static final int SAMPLE_SIZE = 500;

    public static void main(String[] args) {
        DoubleColorBallPreload.execute();

        System.out.println("\n========== Ensemble Weight Optimization ==========\n");

        // 计算每个算法的性能
        var algorithmPool = SingleBallAlgorithmFactory.getAlgorithmPool();
        Map<String, Double> algorithmScores = new HashMap<>();

        var allData = DoubleColorBallPreload.getAllData();
        int testStart = Math.max(10, allData.size() - SAMPLE_SIZE);

        for (var algReg : algorithmPool) {
            if ("EnsemblePredictor".equals(algReg.getAlgorithmName())) {
                continue;
            }

            int hitCount = 0;
            for (int i = testStart; i < allData.size(); i++) {
                var ball = allData.get(i);
                var redBalls = ball.getRedValueList();

                for (int pos = 0; pos < 6; pos++) {
                    SingleBall singleBall = new SingleBall(pos + 1,
                        cqwang.doubleball.detection.model.data.features.BallType.RED);
                    for (int j = 0; j < i; j++) {
                        singleBall.addData(DoubleColorBallPreload.getAllData().get(j).getRedValueList().get(pos));
                    }

                    try {
                        var algo = algReg.getInstance();
                        var result = algo.predict(singleBall, new PredictOption());
                        if (result.getResult() == redBalls.get(pos)) {
                            hitCount++;
                        }
                    } catch (Exception e) {
                        // skip
                    }
                }
            }

            double hitRate = (double) hitCount / (SAMPLE_SIZE * 6);
            algorithmScores.put(algReg.getAlgorithmName(), hitRate);
            System.out.println(String.format("%s: %.2f%%", algReg.getAlgorithmName(), hitRate * 100));
        }

        // 归一化权重
        System.out.println("\n========== Optimized Weights ==========\n");
        double maxScore = algorithmScores.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);

        algorithmScores.forEach((name, score) -> {
            double normalizedWeight = (score / maxScore) * 10;
            System.out.println(String.format("ALGORITHM_WEIGHTS.put(\"%s\", %.1f);", name, normalizedWeight));
        });
    }
}
