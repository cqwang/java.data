package cqwang.doubleball.algorithm.relevance;


import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.AlgorithmSelector;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.common.model.inner.SelectMode;

import java.util.ArrayList;
import java.util.List;

public class RelevancePredictSelector implements AlgorithmSelector {
    public List<RelevanceAlgorithmRegistry> execute(SelectMode selectMode) {
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
    private List<RelevanceAlgorithmRegistry> reCalculate() {
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

    private List<RelevanceAlgorithmRegistry> reCalculateFromFile() {
        var algorithmList = readFromFile();
        for (var algorithm : algorithmList) {
            calculateHistoryPredictValueSum(algorithm);
        }
        algorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        return algorithmList;
    }

    public List<RelevanceAlgorithmRegistry> readFromFile() {
        var algorithmList = FileProvider.readFile(getFilePath(), new TypeReference<List<RelevanceAlgorithmRegistry>>() {
        });
        var memoryAlgorithmList = new ArrayList<RelevanceAlgorithmRegistry>(algorithmList.size());
        for (var algorithm : algorithmList) {
            var memoryAlgorithm = AlgorithmPoolFactory.getRelevanceAlgorithm(algorithm.getName());
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
        return "/BlueRelevanceAlgorithm.json";
    }
}
