package cqwang.doubleball;

import cqwang.doubleball.algorithm.combination.CombinationAlgorithmRegistrySelector;
import cqwang.doubleball.algorithm.single.AlgorithmSelector;
import cqwang.doubleball.common.model.SelectMode;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import cqwang.doubleball.preload.PreloadManager;

import java.util.HashSet;
import java.util.stream.Collectors;

public class FuturePredict {

    /**
     * 聚合预测结果
     */
    public static void aggPredict() {
        PreloadManager.execute();
        var algorithmList = new AlgorithmSelector().execute(SelectMode.FROM_FILE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        var resultSet = new HashSet<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.getInstance().predict(targetIndex);
            resultSet.add(predict.getSimpleInfo());
        }

        var advancedAlgorithmList = new CombinationAlgorithmRegistrySelector().execute(SelectMode.FROM_FILE);
        for (var algorithm : advancedAlgorithmList) {
            var predict = algorithm.predict(targetIndex);
            resultSet.add(predict.getSimpleInfo());
        }
        printInfo(resultSet);
    }


    /**
     * 多算法组合预测
     */
    public static void combinationPredict() {
        PreloadManager.execute();
        var algorithmList = new CombinationAlgorithmRegistrySelector().execute(SelectMode.RE_CALCULATE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        var resultSet = new HashSet<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.predict(targetIndex);
            resultSet.add(predict.getSimpleInfo());
        }
        printInfo(resultSet);
    }

    /**
     * 单种算法预测
     */
    public static void singlePredict() {
        PreloadManager.execute();
        var algorithmList = new AlgorithmSelector().execute(SelectMode.RE_CALCULATE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        var resultSet = new HashSet<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.getInstance().predict(targetIndex);
            resultSet.add(predict.getSimpleInfo());
        }
        printInfo(resultSet);
    }

    private static void printInfo(HashSet<String> resultSet) {
        var list = resultSet.stream().collect(Collectors.toList());
        list.sort(String::compareTo);
        for (var result : list) {
            System.out.println(result);
        }
    }
}
