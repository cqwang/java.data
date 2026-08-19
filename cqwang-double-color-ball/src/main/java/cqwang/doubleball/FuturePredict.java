package cqwang.doubleball;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmSelector;
import cqwang.doubleball.detection.model.option.DoublePredictOption;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;

import java.util.HashSet;
import java.util.stream.Collectors;

public class FuturePredict {
    public static void predict(){
        DoubleColorBallPreload.execute();
        var algorithmList = new DoubleColorAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var targetIndex = DoubleColorBallPreload.getAllData().size();
        var resultSet = new HashSet<String>();
        for (var algorithm : algorithmList) {
            var predict = algorithm.predict(targetIndex, new DoublePredictOption());
            resultSet.add(predict.getSimpleInfo());

            predict = algorithm.predict(targetIndex, new DoublePredictOption().addRedBlock(predict.getRedValueList().get(0)));
            resultSet.add(predict.getSimpleInfo());
            predict = algorithm.predict(targetIndex, new DoublePredictOption().addBlueBlock(predict.getBlueValue()));
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
