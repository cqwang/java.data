package cqwang.doubleball.algorithm.combination;

import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.relevance.RelevancePredictSelector;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.common.model.SelectMode;

import java.util.ArrayList;
import java.util.List;

public class CombinationRelevanceSelector {
    /**
     * 高级算法的最小收入门槛
     */
    int ADVANCED_MIX_AMOUNT = 5500;

    public List<CombinationAlgorithmRegistry> execute(SelectMode selectMode) {


        if (selectMode == SelectMode.RE_CALCULATE_FROM_FILE_FOR_COMBINATION) {
            var algorithmList = reCalculateRelevance();
            System.out.println(JSON.toJSONString(algorithmList)); // 保存到文件  手动保存到resource目录下
            return algorithmList;
        }
        if (selectMode == SelectMode.FROM_FILE) {
            return readFromFile();
        }
        return null;
    }

    private List<CombinationAlgorithmRegistry> reCalculateRelevance() {
        var selectedAlgorithmList = new ArrayList<CombinationAlgorithmRegistry>();

        var combinationAlgorithmRegistrySelector = new CombinationAlgorithmRegistrySelector();
        var algorithmList = new CombinationAlgorithmRegistrySelector().readFromFile();
        var relevanceAlgorithmList = AlgorithmPoolFactory.getRelevanceAlgorithmPool();
        for (var algorithm : algorithmList) {
            for (var relevanceAlgorithm : relevanceAlgorithmList) {
                var combinationAlgorithmRegistry = new CombinationAlgorithmRegistry(algorithm, relevanceAlgorithm);
                combinationAlgorithmRegistrySelector.calculateHistoryPredictValueSum(combinationAlgorithmRegistry);
                var sumValue = combinationAlgorithmRegistry.getHistoryPredictValueSum();
                System.out.println(sumValue);
                if (ValueCalculator.hasNoValue(sumValue) || sumValue < ADVANCED_MIX_AMOUNT) {
                    continue;
                }
                selectedAlgorithmList.add(combinationAlgorithmRegistry);
            }
        }
        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        return selectedAlgorithmList;
    }

    private List<CombinationAlgorithmRegistry> readFromFile() {
        var selectedAlgorithmList = new ArrayList<CombinationAlgorithmRegistry>();

        var algorithmList = new CombinationAlgorithmRegistrySelector().readFromFile();
        var relevanceAlgorithmList = new RelevancePredictSelector().readFromFile();
        for (var algorithm : algorithmList) {
            for (var relevanceAlgorithm : relevanceAlgorithmList) {
                var combinationAlgorithmRegistry = new CombinationAlgorithmRegistry(algorithm, relevanceAlgorithm);
                selectedAlgorithmList.add(combinationAlgorithmRegistry);
            }
        }
        return selectedAlgorithmList;
    }
}
