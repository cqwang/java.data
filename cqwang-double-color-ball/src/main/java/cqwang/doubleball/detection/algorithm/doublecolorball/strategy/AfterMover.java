package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;

import java.util.List;

/**
 * red后移
 */
public class AfterMover {

    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);

        var tempOption = option.clone();

        var flag5=origin.getRedValueList().get(5) >=33;
        if (flag5) {
            tempOption.setAllow(BallType.RED, 5, origin.getRedValueList().get(4));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        var flag4=origin.getRedValueList().get(4)>30;
        if(flag4 && flag5){
            tempOption.setAllow(BallType.RED, 5, origin.getRedValueList().get(4));
            tempOption.setAllow(BallType.RED, 4, origin.getRedValueList().get(3));
            ballList.add(generator.predict(targetIndex, tempOption));
        }


//
//        tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(0));
//        tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(1));
//        tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(2));
//        tempOption.setAllow(BallType.RED, 4, origin.getRedValueList().get(3));
    }
}
