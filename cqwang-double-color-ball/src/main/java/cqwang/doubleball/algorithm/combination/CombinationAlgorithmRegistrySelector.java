package cqwang.doubleball.algorithm.combination;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.AlgorithmSelector;
import cqwang.doubleball.algorithm.single.SingleAlgorithmSelector;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.common.model.SelectMode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CombinationAlgorithmRegistrySelector implements AlgorithmSelector {

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

        if (selectMode == SelectMode.RE_CALCULATE_FROM_FILE) {
            var algorithmList = reCalculateFromFile();
            System.out.println(JSON.toJSONString(algorithmList)); // 保存到文件  手动保存到resource目录下
            return algorithmList;
        }

        return readFromFile();
    }

    public List<CombinationAlgorithmRegistry> readFromFile() {
        return FileProvider.readFile(getFilePath(), new TypeReference<List<CombinationAlgorithmRegistry>>() {
        });
    }

    /**
     * 重新计算价值，挑选算法
     *
     * @return
     */
    private List<CombinationAlgorithmRegistry> reCalculate() {
        ArrayList<CombinationAlgorithmRegistry> selectedAlgorithmList = new ArrayList<>();

        var algorithmRegistryList = new SingleAlgorithmSelector().execute(SelectMode.FROM_FILE);
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
        for (var algorithm : algorithmList) {
            calculateHistoryPredictValueSum(algorithm);
        }
        algorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        return algorithmList;
    }


    @Override
    public int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        return ValueCalculator.calculate(predictResult, target);
    }

    @Override
    public int getMaxCount() {
        return 20;
    }

    @Override
    public String getFilePath() {
        return "/CombinationAlgorithm.json";
    }
}
