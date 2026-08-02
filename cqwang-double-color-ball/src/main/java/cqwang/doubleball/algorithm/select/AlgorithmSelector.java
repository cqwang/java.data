package cqwang.doubleball.algorithm.select;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.detection.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.detection.AlgorithmRegistry;
import cqwang.doubleball.helper.SelectMode;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;

import java.util.ArrayList;
import java.util.List;

public interface AlgorithmSelector {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;

    /**
     *
     * @param selectMode
     * @return
     */
    default List<AlgorithmRegistry> execute(SelectMode selectMode) {
        if (selectMode == SelectMode.RE_CALCULATE) {
            var algorithmList = reCalculate();
            System.out.println(JSON.toJSONString(algorithmList)); // 保存到文件  手动保存到resource目录下
            return algorithmList;
        }

        return readFromFile();
    }

    /**
     * 重新计算价值，挑选算法
     *
     * @return
     */
    private List<AlgorithmRegistry> reCalculate() {
        ArrayList<AlgorithmRegistry> selectedAlgorithmList = new ArrayList<>(getMaxCount());

        var algorithmList = AlgorithmPoolFactory.getAlgorithmPool();
        for (var algorithm : algorithmList) {
            calculateHistoryPredictValueSum(algorithm);
            selectedAlgorithmList.add(algorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(getMaxCount(), selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }

    /**
     * 计算价值
     *
     * @param algorithm
     * @return
     */
    private void calculateHistoryPredictValueSum(AlgorithmRegistry algorithm) {
        int sumValue = 0;
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallDataPreload.allData().size(); targetIndex++) {
            var predict = algorithm.getInstance().predict(targetIndex);
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

    /**
     * 从文件读取已保存算法
     *
     * @return
     */
    private List<AlgorithmRegistry> readFromFile() {
        var algorithmList = FileProvider.readFile(getFilePath(), new TypeReference<List<AlgorithmRegistry>>() {
        });

        var memoryAlgorithmList = new ArrayList<AlgorithmRegistry>(algorithmList.size());
        for (var algorithm : algorithmList) {
            var memoryAlgorithm = AlgorithmPoolFactory.getAlgorithm(algorithm.getName());
            if (memoryAlgorithm == null) {
                continue;
            }
            memoryAlgorithm.setHistoryHitCount(algorithm.getHistoryHitCount());
            memoryAlgorithm.setMaxAmount(algorithm.getMaxAmount());
            memoryAlgorithm.setHistoryPredictValueSum(algorithm.getHistoryPredictValueSum());
            memoryAlgorithmList.add(memoryAlgorithm);
        }
        return memoryAlgorithmList;
    }

    String getFilePath();

    int getMaxCount();

    int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target);
}
