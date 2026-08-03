package cqwang.doubleball.algorithm.combination;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.single.AlgorithmSelector;
import cqwang.doubleball.common.model.SelectMode;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CombinationAlgorithmRegistrySelector {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;

    /**
     * 高级算法的最小收入门槛
     */
    int ADVANCED_MIX_AMOUNT = 5500;

    /**
     *
     * @param selectMode
     * @return
     */
    public List<CombinationAlgorithmRegistry> execute(SelectMode selectMode) {
        if (selectMode == SelectMode.RE_CALCULATE) {
            var algorithmList = reCalculate();
            System.out.println(JSON.toJSONString(algorithmList)); // 保存到文件  手动保存到resource目录下
            return algorithmList;
        }

        if(selectMode == SelectMode.RE_CALCULATE_FROM_FILE){
            var algorithmList = reCalculateFromFile();
            System.out.println(JSON.toJSONString(algorithmList)); // 保存到文件  手动保存到resource目录下
            return algorithmList;
        }

        return readFromFile();
    }

    private static List<CombinationAlgorithmRegistry> readFromFile() {
        return FileProvider.readFile("/CombinationAlgorithm.json", new TypeReference<List<CombinationAlgorithmRegistry>>() {
        });
    }

    /**
     * 重新计算价值，挑选算法
     *
     * @return
     */
    private List<CombinationAlgorithmRegistry> reCalculate() {
        ArrayList<CombinationAlgorithmRegistry> selectedAlgorithmList = new ArrayList<>();

        var algorithmRegistryList = new AlgorithmSelector().execute(SelectMode.FROM_FILE);
        for (var red0 : algorithmRegistryList) {
            for (var red1 : algorithmRegistryList) {
                for (var red2 : algorithmRegistryList) {
                    for (var red3 : algorithmRegistryList) {
                        for (var red4 : algorithmRegistryList) {
                            for (var red5 : algorithmRegistryList) {
                                for (var blue : algorithmRegistryList) {
                                    var advancedAlgorithm = new CombinationAlgorithmRegistry(blue, red0, red1, red2, red3, red4, red5, blue);
                                    calculateHistoryPredictValueSum(advancedAlgorithm);
                                    var sumValue = advancedAlgorithm.getHistoryPredictValueSum();
                                    if (ValueCalculator.hasNoValue(sumValue) || sumValue < ADVANCED_MIX_AMOUNT) {
                                        continue;
                                    }
                                    selectedAlgorithmList.add(advancedAlgorithm);
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(20, selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }

    private List<CombinationAlgorithmRegistry> reCalculateFromFile() {
        var algorithmList = readFromFile();
        for(var algorithm : algorithmList){
            calculateHistoryPredictValueSum(algorithm);
        }

        algorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(20, algorithmList.size());
        return algorithmList.subList(0, actualCount);
    }

    /**
     * 计算价值
     *
     * @param algorithm
     * @return
     */
    private void calculateHistoryPredictValueSum(CombinationAlgorithmRegistry algorithm) {
        int sumValue = 0;
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallDataPreload.allData().size(); targetIndex++) {
            var predict = algorithm.predict(targetIndex);
            var target = DoubleColorBallDataPreload.allData().get(targetIndex);
            var value = ValueCalculator.calculate(predict, target);
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
}
