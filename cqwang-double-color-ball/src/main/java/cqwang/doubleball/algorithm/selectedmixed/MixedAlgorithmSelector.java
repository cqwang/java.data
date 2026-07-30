package cqwang.doubleball.algorithm.selectedmixed;

import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.detection.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.detection.AlgorithmRegistry;
import cqwang.doubleball.algorithm.select.ValueCalculator;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;

import java.util.ArrayList;
import java.util.List;

/**
 * 预测，写一遍selector算法
 */
public class MixedAlgorithmSelector {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;

    public List<MixedAlgorithm> execute() {
        var selectedAlgorithmList = reCalculate();
        for (var algorithm : selectedAlgorithmList) {
            System.out.println(algorithm.toString());
        }
        return selectedAlgorithmList;
    }

    public List<MixedAlgorithm> reCalculate() {
        ArrayList<MixedAlgorithm> selectedAlgorithmList = new ArrayList<>(8);

        var algorithmList = MixedAlgorithmFactory.getAlgorithmPool();
        for (var algorithm : algorithmList) {
            var sumValue = calculateHistoryPredictValueSum(algorithm);
            if (ValueCalculator.hasNoValue(sumValue)) {
                continue;
            }
            algorithm.setHistoryPredictValueSum(sumValue);
            selectedAlgorithmList.add(algorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(18, selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }


    /**
     * 计算价值
     *
     * @param algorithm
     * @return
     */
    private int calculateHistoryPredictValueSum(MixedAlgorithm algorithm) {
        int sumValue = 0;
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallDataPreload.allData().size(); targetIndex++) {
            var predict = algorithm.predict(targetIndex);
            var target = DoubleColorBallDataPreload.allData().get(targetIndex);
            var value = ValueCalculator.calculate(predict, target);
            sumValue += value;
        }
        return sumValue;
    }
}
