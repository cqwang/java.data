package cqwang.doubleball;

import cqwang.doubleball.v2.algorithm.doublecolorball.DoubleColorPredictionAlgorithmSelector;
import cqwang.doubleball.v2.model.option.RunOption;
import cqwang.doubleball.v2.preload.DoubleColorBallPreload;

import java.util.HashSet;
import java.util.stream.Collectors;

public class FuturePredictV2 {
    public static void predict(){
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorPredictionAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var targetIndex = DoubleColorBallPreload.getAllData().size();
        var resultSet = new HashSet<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.predict(targetIndex);
            resultSet.add(predict.getSimpleInfo());
        }

        printInfo(resultSet);
    }


    public static void predictBlue(){
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorPredictionAlgorithmSelector().execute(RunOption.RE_CALCULATE_JUST_FOR_BLUE);
    }

    private static void printInfo(HashSet<String> resultSet) {
        var list = resultSet.stream().collect(Collectors.toList());
        list.sort(String::compareTo);
        for (var result : list) {
            System.out.println(result);
        }
    }

}
