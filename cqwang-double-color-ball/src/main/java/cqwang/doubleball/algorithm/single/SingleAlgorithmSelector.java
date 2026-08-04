package cqwang.doubleball.algorithm.single;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.AlgorithmSelector;
import cqwang.doubleball.common.ValueCalculator;
import cqwang.doubleball.common.model.SelectMode;
import cqwang.doubleball.common.model.DoubleColorBallItem;

import java.util.ArrayList;
import java.util.List;

public class SingleAlgorithmSelector implements AlgorithmSelector {

    /**
     *
     * @param selectMode
     * @return
     */
    public List<SingleAlgorithmRegistry> execute(SelectMode selectMode) {
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

    private List<SingleAlgorithmRegistry> reCalculateFromFile() {
        ArrayList<SingleAlgorithmRegistry> selectedAlgorithmList = new ArrayList<>(getMaxCount());

        var algorithmList = readFromFile();
        for (var algorithm : algorithmList) {
            calculateHistoryPredictValueSum(algorithm);
            selectedAlgorithmList.add(algorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(getMaxCount(), selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }

    /**
     * 重新计算价值，挑选算法
     *
     * @return
     */
    private List<SingleAlgorithmRegistry> reCalculate() {
        ArrayList<SingleAlgorithmRegistry> selectedAlgorithmList = new ArrayList<>(getMaxCount());

        var algorithmList = AlgorithmPoolFactory.getSingleAlgorithmPool();
        for (var algorithm : algorithmList) {
            calculateHistoryPredictValueSum(algorithm);
            selectedAlgorithmList.add(algorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(getMaxCount(), selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }

    /**
     * 从文件读取已保存算法
     *
     * @return
     */
    private List<SingleAlgorithmRegistry> readFromFile() {
        var algorithmList = FileProvider.readFile(getFilePath(), new TypeReference<List<SingleAlgorithmRegistry>>() {
        });

        var memoryAlgorithmList = new ArrayList<SingleAlgorithmRegistry>(algorithmList.size());
        for (var algorithm : algorithmList) {
            var memoryAlgorithm = AlgorithmPoolFactory.getSingleAlgorithm(algorithm.getName());
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
    public String getFilePath() {
        return "/SingleSelectedAlgorithm.json";
    }

    @Override
    public int getMaxCount() {
        return 100;
    }

    @Override
    public int calculateValue(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        return ValueCalculator.calculate(predictResult, target);
    }
}
