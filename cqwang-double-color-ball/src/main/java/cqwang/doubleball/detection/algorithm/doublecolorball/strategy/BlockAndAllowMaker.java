package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorListAlgorithmSelector;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;

import java.util.List;

public class BlockAndAllowMaker {
    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {

    }

    public static int getProfit(){
        if(DoubleColorBallPreload.getAllData() == null || DoubleColorBallPreload.getAllData().isEmpty()){
            DoubleColorBallPreload.execute();
        }

        var algorithmList = new DoubleColorListAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        return algorithmList.get(0).getPredictResult().getProfit();
    }
}
