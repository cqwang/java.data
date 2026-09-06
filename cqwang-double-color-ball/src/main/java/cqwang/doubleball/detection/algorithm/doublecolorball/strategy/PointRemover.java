package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SplitBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.utils.AlgorithmUtils;

import java.util.List;

/**
 * 删除部分节点来预测
 */
public class PointRemover {
    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);
        var startIndex = Math.max(0, targetIndex - 80);
        var size = Math.min(targetIndex, 80);
        var splitBall = new SplitBall(startIndex, size);

        // 去除TOP N 最冷
        var changeOption = option.clone();
        for (int redIndex = 0; redIndex < 3; redIndex++) {
            extracted(splitBall, changeOption, BallType.RED, redIndex);
        }
//        extracted(splitBall, changeOption, BallType.BLUE, 0);

        var predictResult = generator.predict(targetIndex, changeOption);
        if (!predictResult.getSimpleInfo().equals(origin.getSimpleInfo())) {
            ballList.add(predictResult);
        }
    }

    private static void extracted(SplitBall splitBall, PredictOption changeOption, BallType ballType, int index) {
        extracted(splitBall, changeOption, ballType, index,40);
        extracted(splitBall, changeOption, ballType, index,20);
        extracted(splitBall, changeOption, ballType, index,10);
    }

    private static void extracted(SplitBall splitBall, PredictOption changeOption, BallType ballType, int index, int windowSize) {
        var coldList = AlgorithmUtils.findColdList(splitBall, ballType, index, windowSize);
        var maxSize = ballType == BallType.BLUE ? 16 : 33;
        maxSize = maxSize*2/3;
        for (int i = 0; i < maxSize; i++) {
            changeOption.addBlock(ballType, index, coldList.get(i).getData());
        }
    }
}
