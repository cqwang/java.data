package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.data.SplitBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.utils.AlgorithmUtils;

import java.util.List;

public class ColdBest {
    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {
        var splitBall = new SplitBall(targetIndex);

//         使用冷red
        addColdRed(ballList,splitBall, generator, targetIndex, 0, option);
        addColdRed(ballList,splitBall, generator, targetIndex, 5, option);
        addColdRed(ballList,splitBall, generator, targetIndex, 3, option);

//         使用冷blue
        var origin = ballList.get(0);
        var coldBlueList = AlgorithmUtils.findColdList(splitBall, BallType.BLUE, 0, 15);
        for (var blue : coldBlueList) {
            ballList.add(origin.clone(blue.getData(), null));
        }
    }

    private static void addColdRed(
            List<DoubleColorBall> ballList,
            SplitBall splitBall,
            DoubleColorAlgorithmRegistry generator,
            int targetIndex,
            int redIndex,
            PredictOption option) {
        var coldRedList = AlgorithmUtils.findColdList(splitBall, BallType.RED, redIndex, 30);
        for (var red : coldRedList) {
            var result = generator.predict(targetIndex, option.cloneAndSetAllow(BallType.RED, redIndex, red.getData()));
            ballList.add(result);
        }
    }
}
