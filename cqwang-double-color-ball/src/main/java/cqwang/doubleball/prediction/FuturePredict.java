package cqwang.doubleball.prediction;

import cqwang.doubleball.algorithm.select.AlgorithmSelector;
import cqwang.doubleball.algorithm.select.SelectMode;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import cqwang.doubleball.preload.PreloadManager;

public class FuturePredict {
    public static void predict() {
        PreloadManager.execute();
        var algorithmList = AlgorithmSelector.execute(SelectMode.FROM_FILE);
        var targetIndex = DoubleColorBallDataPreload.allData().size();
        for (var algorithm : algorithmList) {
            var predict = algorithm.getInstance().predict(targetIndex);
            System.out.println(predict);
        }
    }
}
