package cqwang.doubleball;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmSelector;
import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorListAlgorithmSelector;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;

import java.util.*;

public class FuturePredict {
    public static void predict() {
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var targetIndex = DoubleColorBallPreload.getAllData().size();
        Map<String, Integer> result = new HashMap<>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.predict(targetIndex, new PredictOption());
            var last = result.getOrDefault(predict.getSimpleInfo(), 0);
            result.put(predict.getSimpleInfo(), last + 1);
        }

        printInfo(result);
    }

    public static void predictList() {
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorListAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var targetIndex = DoubleColorBallPreload.getAllData().size();
        Map<String, Integer> result = new HashMap<>();
        for (var algorithm : algorithmList) {
            var predictList = algorithm.predictList(targetIndex, new PredictOption());
            for (var predict : predictList) {
                var last = result.getOrDefault(predict.getSimpleInfo(), 0);
                result.put(predict.getSimpleInfo(), last + 1);
            }
        }

        printInfo(result);
    }


    private static void printInfo(Map<String, Integer> result) {
        var list = new ArrayList<String>();
        for (var entry : result.entrySet()) {
            list.add(entry.getKey());
        }
        list.sort(String::compareTo);
        for (var predict : list) {
            System.out.println(predict + " ===份数=== " + result.get(predict));
        }
    }

}
