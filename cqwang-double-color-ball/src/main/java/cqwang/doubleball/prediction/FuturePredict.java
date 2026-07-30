package cqwang.doubleball.prediction;

import cqwang.doubleball.algorithm.select.impl.BlueAlgorithmSelector;
import cqwang.doubleball.algorithm.select.impl.SingleAlgorithmSelector;
import cqwang.doubleball.algorithm.select.SelectMode;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import cqwang.doubleball.preload.PreloadManager;

public class FuturePredict {
    public static void predict() {
        PreloadManager.execute();
        var algorithmList = new SingleAlgorithmSelector().execute(SelectMode.FROM_FILE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        for (var algorithm : algorithmList) {
            var predict = algorithm.getInstance().predict(targetIndex);
            System.out.println(predict.getSimpleInfo());
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
