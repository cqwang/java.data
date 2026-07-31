package cqwang.doubleball.algorithm;

import cqwang.doubleball.algorithm.select.impl.BlueAlgorithmSelector;
import cqwang.doubleball.algorithm.select.impl.SingleAlgorithmSelector;
import cqwang.doubleball.algorithm.select.SelectMode;
import cqwang.doubleball.algorithm.selectedmixed.MixedAlgorithmSelector;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import cqwang.doubleball.preload.PreloadManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class FuturePredict {
    public static void predictMix() {
        PreloadManager.execute();
        var algorithmList = new MixedAlgorithmSelector().execute();
//        var targetIndex = DoubleColorBallDataPreload.allData().size();
//        for (var algorithm : algorithmList) {
//            var predict = algorithm.predict(targetIndex);
//            System.out.println(predict.getSimpleInfo());
//        }
    }

    public static void predict() {
        PreloadManager.execute();
        var algorithmList = new SingleAlgorithmSelector().execute(SelectMode.RE_CALCULATE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        var resultSet = new HashSet<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.getInstance().predict(targetIndex);
            resultSet.add(predict.getSimpleInfo());
        }
        var list = resultSet.stream().collect(Collectors.toList());
        list.sort(String::compareTo);
        for (var result : list) {
            System.out.println(result);
        }
    }

    public static void predictBlue() {
        PreloadManager.execute();
        var algorithmList = new BlueAlgorithmSelector().execute(SelectMode.RE_CALCULATE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        for (var algorithm : algorithmList) {
            var predict = algorithm.getInstance().predict(targetIndex);
            System.out.println(predict.getSimpleInfo());
        }
    }
}
