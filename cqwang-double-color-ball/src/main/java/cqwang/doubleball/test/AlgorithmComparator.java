package cqwang.doubleball.test;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.cache.preload.DoubleColorBallPreload;

public class AlgorithmComparator {
    public static void main(String[] args) {
        DoubleColorBallPreload.execute();

        System.out.println("\n========== Algorithm Comparison Test ==========\n");

        var blueReg = SingleBallAlgorithmFactory.getAlgorithm("BlueRecommend");
        var redOldReg = SingleBallAlgorithmFactory.getAlgorithm("RedRecommend");
        var redEnsembleReg = SingleBallAlgorithmFactory.getAlgorithm("EnsemblePredictor");
        var redAdvancedEnsembleReg = SingleBallAlgorithmFactory.getAlgorithm("AdvancedEnsemblePredictor");
        var redHMMReg = SingleBallAlgorithmFactory.getAlgorithm("HMMPredictor");

        // Baseline: RedRecommend + BlueRecommend
        var baselineAlgo = new DoubleColorAlgorithmRegistry(blueReg, redOldReg);
        historyTest(baselineAlgo, "RedRecommend + BlueRecommend");

        // ML: EnsemblePredictor + BlueRecommend
        var ensembleAlgo = new DoubleColorAlgorithmRegistry(blueReg, redEnsembleReg);
        historyTest(ensembleAlgo, "EnsemblePredictor + BlueRecommend");

        // ML: AdvancedEnsemblePredictor + BlueRecommend
        var advancedEnsembleAlgo = new DoubleColorAlgorithmRegistry(blueReg, redAdvancedEnsembleReg);
        historyTest(advancedEnsembleAlgo, "AdvancedEnsemblePredictor + BlueRecommend");

        // TimeSeries: HMMPredictor + BlueRecommend
        var hmmAlgo = new DoubleColorAlgorithmRegistry(blueReg, redHMMReg);
        historyTest(hmmAlgo, "HMMPredictor + BlueRecommend");

        System.out.println("\n========== Results Summary ==========");
        int totalSamples = DoubleColorBallPreload.getAllData().size() - 10;

        System.out.println("Baseline (RedRecommend): " +
            String.format("Profit: %d, Red: %.2f%%, Blue: %.2f%%",
                baselineAlgo.getPredictResult().getProfit(),
                (double) baselineAlgo.getPredictResult().getHitRedTotalCount() / totalSamples * 100,
                (double) baselineAlgo.getPredictResult().getHitBlueTotalCount() / totalSamples * 100));

        System.out.println("EnsemblePredictor: " +
            String.format("Profit: %d, Red: %.2f%%, Blue: %.2f%%",
                ensembleAlgo.getPredictResult().getProfit(),
                (double) ensembleAlgo.getPredictResult().getHitRedTotalCount() / totalSamples * 100,
                (double) ensembleAlgo.getPredictResult().getHitBlueTotalCount() / totalSamples * 100));

        System.out.println("AdvancedEnsemblePredictor: " +
            String.format("Profit: %d, Red: %.2f%%, Blue: %.2f%%",
                advancedEnsembleAlgo.getPredictResult().getProfit(),
                (double) advancedEnsembleAlgo.getPredictResult().getHitRedTotalCount() / totalSamples * 100,
                (double) advancedEnsembleAlgo.getPredictResult().getHitBlueTotalCount() / totalSamples * 100));

        System.out.println("HMMPredictor: " +
            String.format("Profit: %d, Red: %.2f%%, Blue: %.2f%%",
                hmmAlgo.getPredictResult().getProfit(),
                (double) hmmAlgo.getPredictResult().getHitRedTotalCount() / totalSamples * 100,
                (double) hmmAlgo.getPredictResult().getHitBlueTotalCount() / totalSamples * 100));
    }

    private static void historyTest(DoubleColorAlgorithmRegistry registry, String name) {
        System.out.println("Testing: " + name);
        int count = 0;
        for (int targetIndex = 10; targetIndex < DoubleColorBallPreload.getAllData().size(); targetIndex++) {
            var predict = registry.predict(targetIndex, new PredictOption());
            var target = DoubleColorBallPreload.getAllData().get(targetIndex);
            var value = cqwang.doubleball.detection.utils.ValueCalculator.calculate(predict, target);
            registry.getPredictResult().add(targetIndex, value);
            count++;
            if (count % 100 == 0) {
                System.out.println("  Processed: " + count + " samples");
            }
        }
        System.out.println("  Total samples: " + count);
        System.out.println("  Profit: " + registry.getPredictResult().getProfit());
        System.out.println("  Red hit rate: " + String.format("%.2f%%",
            (double) registry.getPredictResult().getHitRedTotalCount() / count * 100));
        System.out.println("  Blue hit rate: " + String.format("%.2f%%",
            (double) registry.getPredictResult().getHitBlueTotalCount() / count * 100));
    }
}
