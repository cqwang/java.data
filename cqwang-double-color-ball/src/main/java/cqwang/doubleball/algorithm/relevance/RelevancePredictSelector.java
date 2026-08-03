package cqwang.doubleball.algorithm.relevance;


import cqwang.doubleball.algorithm.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.AlgorithmSelector;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.common.model.DoubleColorBallItem;

import java.util.ArrayList;
import java.util.List;

public class RelevancePredictSelector implements AlgorithmSelector {
    public List<RelevanceAlgorithmRegistry> reCalculate(){
        var selectedAlgorithmList = new ArrayList<RelevanceAlgorithmRegistry>();
        var algorithmList = AlgorithmPoolFactory.getRelevanceAlgorithmPool();
        for (var algorithm : algorithmList) {
            calculateHistoryPredictValueSum(algorithm);
            selectedAlgorithmList.add(algorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(getMaxCount(), selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }

    @Override
    public int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        return ValueCalculator.calculateBlue(predictResult, target);
    }

    @Override
    public int getMaxCount() {
        return 50;
    }

    @Override
    public String getFilePath() {
        return "";
    }
}
