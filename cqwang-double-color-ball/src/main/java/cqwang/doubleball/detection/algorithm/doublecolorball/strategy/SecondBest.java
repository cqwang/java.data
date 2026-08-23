package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;

import java.util.List;
import java.util.Set;

public class SecondBest {

    /**
     * 用算法本身的次优，替换 red
     *
     * @param ballList
     * @param targetIndex
     * @param option
     * @param generator
     */
    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);

        var continuesOption = option.clone();
        if (origin.getRedValueList().get(0) <= 2) {
            // 用算法本身的次优，替换first red，这是1950的突起，不稳定
            replaceRed(ballList, targetIndex, continuesOption, origin, 0, 15, generator);
        }
    }

    private static int getTryTimes(DoubleColorBall origin, int redIndex) {
        var maxTimes = 5;
        var result = 5;
        if (redIndex == 0) {
            result = origin.getRedValueList().get(redIndex + 1) - origin.getRedValueList().get(redIndex) - 1;
        }
        if (redIndex == origin.getRedValueList().size() - 1) {
            result = origin.getRedValueList().get(redIndex) - origin.getRedValueList().get(redIndex - 1) - 1;
        }
        return Math.min(maxTimes, result);
    }

    private static void replaceReds(
            List<DoubleColorBall> resultList,
            int targetIndex,
            PredictOption option,
            DoubleColorBall origin,
            Set<Integer> indexList,
            int defaultTryTimes,
            DoubleColorAlgorithmRegistry generator) {
        var tryTimes = defaultTryTimes;
        for (var index : indexList) {
            option.addBlock(BallType.RED, index, origin.getRedValueList().get(index));
            var times = getTryTimes(origin, index);
            if (times < tryTimes) {
                tryTimes = times;
            }
        }

        for (int i = 0; i < tryTimes; i++) {
            var firstRedBlock = generator.predict(targetIndex, option);
            resultList.add(firstRedBlock);
            for (var index : indexList) {
                option.addBlock(BallType.RED, index, firstRedBlock.getRedValueList().get(index));
            }
        }
    }

    private static void replaceRed(
            List<DoubleColorBall> resultList,
            int targetIndex,
            PredictOption option,
            DoubleColorBall origin,
            int index,
            int tryTimes,
            DoubleColorAlgorithmRegistry generator) {
        option.addBlock(BallType.RED, index, origin.getRedValueList().get(index));
        for (int i = 0; i < tryTimes; i++) {
            var firstRedBlock = generator.predict(targetIndex, option);
            resultList.add(firstRedBlock);
            option.addBlock(BallType.RED, index, firstRedBlock.getRedValueList().get(index));
        }
    }

    private static void replaceBlue(
            List<DoubleColorBall> resultList,
            int targetIndex,
            PredictOption option,
            DoubleColorBall origin,
            int tryTimes,
            DoubleColorAlgorithmRegistry generator) {
        if (origin.getBlueValue() < 4) {
            // 替换blue
            var blueOption = option.cloneAndAddBlock(BallType.BLUE, 0, origin.getBlueValue());
            for (int i = 0; i < tryTimes; i++) {
                var blueBlock = generator.predict(targetIndex, blueOption);
                resultList.add(blueBlock);
                blueOption.addBlock(BallType.BLUE, 0, blueBlock.getBlueValue());
            }
        }
    }
}
