package cqwang.doubleball.algorithm;

import cqwang.doubleball.algorithm.combination.CombinationAlgorithmRegistry;
import cqwang.doubleball.algorithm.single.SingleAlgorithmRegistry;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;

public interface AlgorithmSelector {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;


    /**
     * 高级算法的最小收入门槛
     */
    int ADVANCED_MIX_AMOUNT = 5500;

    /**
     * 计算价值
     *
     * @param algorithm
     * @return
     */
    default void calculateHistoryPredictValueSum(AlgorithmRegistry algorithm) {
        var predictionAlgorithm = getPredictionAlgorithm(algorithm);
        if(predictionAlgorithm == null){
            throw new RuntimeException("not find predictionAlgorithm");
        }

        int sumValue = 0;
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallDataPreload.allData().size(); targetIndex++) {
            var predict = predictionAlgorithm.predict(targetIndex);
            var target = DoubleColorBallDataPreload.allData().get(targetIndex);
            var value = calculateValue(predict, target);
            if (ValueCalculator.hasNoValue(value)) {
                continue;
            }
            algorithm.setHistoryHitCount(algorithm.getHistoryHitCount() + 1);
            sumValue += value;
            if (value > algorithm.getMaxAmount()) {
                algorithm.setMaxAmount(value);
            }
        }
        algorithm.setHistoryPredictValueSum(sumValue);
    }

    default PredictionAlgorithm getPredictionAlgorithm(AlgorithmRegistry algorithm) {
        if (algorithm instanceof SingleAlgorithmRegistry) {
            return ((SingleAlgorithmRegistry) algorithm).getInstance();
        } else if (algorithm instanceof CombinationAlgorithmRegistry) {
            return (CombinationAlgorithmRegistry) algorithm;
        }
        return null;
    }


    int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target);

    int getMaxCount();

    String getFilePath();
}
