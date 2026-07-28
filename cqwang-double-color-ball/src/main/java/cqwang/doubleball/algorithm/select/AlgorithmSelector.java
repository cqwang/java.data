package cqwang.doubleball.algorithm.select;

import cqwang.doubleball.algorithm.detection.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.detection.AlgorithmRegistry;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmSelector {
    private static final int MAX_COUNT = 8;
    /**
     * 最小样本量
     */
    private static final int MIN_SAMPLE_COUNT = 100;


    public static List<AlgorithmRegistry> execute(SelectMode selectMode){
        if(selectMode == SelectMode.RE_CALCULATE) {
            var algorithmList = reCalculate();
            // 保存到文件
            return algorithmList;
        }

        return readFromFile();
    }

    public static List<AlgorithmRegistry> readFromFile() {

    }


    public static List<AlgorithmRegistry> reCalculate() {
        ArrayList<AlgorithmRegistry> selectedAlgorithmList = new ArrayList<>(MAX_COUNT);

        var algorithmList = AlgorithmPoolFactory.getAlgorithmPool();
        for (var algorithm : algorithmList) {
            var sumValue = calculateHistoryPredictValueSum(algorithm);
            if (ValueCalculator.hasNoValue(sumValue)) {
                continue;
            }
            algorithm.setHistoryPredictValueSum(sumValue);
            selectedAlgorithmList.add(algorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(MAX_COUNT, selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }

    /**
     * 历史回归，进行预测，计算价值合计
     *
     * @param algorithmRegistry
     * @return
     */
    private static int calculateHistoryPredictValueSum(AlgorithmRegistry algorithmRegistry) {
        int sumValue = 0;
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallDataPreload.allData().size(); targetIndex++) {
            var predict = algorithmRegistry.getInstance().predict(targetIndex);
            var target = DoubleColorBallDataPreload.allData().get(targetIndex);
            var value = ValueCalculator.calculate(predict, target);
            sumValue += value;
        }
        return sumValue;
    }
}
