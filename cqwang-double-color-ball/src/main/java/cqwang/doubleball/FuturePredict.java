package cqwang.doubleball;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmSelector;
import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorListAlgorithmSelector;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FuturePredict {
    public static void predict() {
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var targetIndex = DoubleColorBallPreload.getAllData().size();
        var list = new ArrayList<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.predict(targetIndex, new PredictOption());
            list.add(predict.getSimpleInfo());
        }

        printInfo(list);
    }

    public static void predictList() {
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorListAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var targetIndex = DoubleColorBallPreload.getAllData().size();
        var list = new ArrayList<String>();
        for (var algorithm : algorithmList) {
            var predictList = algorithm.predictList(targetIndex, new PredictOption());
            for (var predict : predictList) {
                list.add(predict.getSimpleInfo());
            }
        }

        printInfo(list);
    }


    private static void printInfo(List<String> list) {
        list.sort(String::compareTo);
        for (var result : list) {
            System.out.println(result);
        }
    }

}
