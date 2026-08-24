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
    public static void execute(List<DoubleColorBall> ballList, int targetIndex, PredictOption option, DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);

        replaceRed(ballList, targetIndex, option.clone(), origin, 0, generator);
//        replaceBlue(ballList, targetIndex, option.clone(),origin,10, generator);
//        replaceRedNeighbor(ballList, targetIndex, option.clone(), origin, 2, generator);
//        replaceRedNeighbor(ballList, targetIndex, option.clone(), origin, 5, generator);
//        replaceBlueNeighbor(ballList, targetIndex, option.clone(), origin, 5, generator);

    }

    private static int getTryTimes(DoubleColorBall origin, int redIndex) {
        var maxTimes = 5;
        var result = maxTimes;
        if (redIndex == 0) {
            result = origin.getRedValueList().get(redIndex + 1) - origin.getRedValueList().get(redIndex) - 1;
        } else {
            result = origin.getRedValueList().get(redIndex) - origin.getRedValueList().get(redIndex - 1) - 1;
        }
        return Math.min(maxTimes, result);
    }

    private static void replaceReds(List<DoubleColorBall> resultList, int targetIndex, PredictOption option, DoubleColorBall origin, Set<Integer> indexList, int defaultTryTimes, DoubleColorAlgorithmRegistry generator) {
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

    private static void replaceRed(List<DoubleColorBall> resultList, int targetIndex, PredictOption option, DoubleColorBall origin, int index, DoubleColorAlgorithmRegistry generator) {
        var tryTimes = getTryTimes(origin, index);
        option.addBlock(BallType.RED, index, origin.getRedValueList().get(index));
        for (int i = 0; i < tryTimes; i++) {
            var firstRedBlock = generator.predict(targetIndex, option);
            resultList.add(firstRedBlock);
            if (option.isBlock(BallType.RED, index, firstRedBlock.getRedValueList().get(index))) {
                return;
            }
            option.addBlock(BallType.RED, index, firstRedBlock.getRedValueList().get(index));
        }
    }

    private static void replaceRedNeighbor(List<DoubleColorBall> resultList, int targetIndex, PredictOption option, DoubleColorBall origin, int index, DoubleColorAlgorithmRegistry generator) {

        var tryTimes = getTryTimes(origin, index);
        if (!extracted(option, origin, index)) {
            return;
        }
        for (int i = 0; i < tryTimes; i++) {
            var result = generator.predict(targetIndex, option);
            resultList.add(result);

            if (!extracted(option, result, index)) {
                return;
            }
        }
    }

    private static boolean extracted(PredictOption option, DoubleColorBall ball, int index) {
        var success = false;
        var data = ball.getRedValueList().get(index);
        var preData = data;
        if (index > 1) {
            preData = ball.getRedValueList().get(index - 1);
        }
        var afterData = data;
        if (index < 5) {
            afterData = ball.getRedValueList().get(index + 1);
        }

        option.addBlock(BallType.RED, index, data);
        if (afterData - data > 1) {
            option.addBlock(BallType.RED, index, data + 1);
            success = true;
        }
        if (data - preData > 1) {
            option.addBlock(BallType.RED, index, data - 1);
            success = true;
        }
        return success;
    }

    private static void replaceBlue(List<DoubleColorBall> resultList, int targetIndex, PredictOption option, DoubleColorBall origin, int tryTimes, DoubleColorAlgorithmRegistry generator) {
        // 替换blue
        var blueOption = option.cloneAndAddBlock(BallType.BLUE, 0, origin.getBlueValue());
        for (int i = 0; i < tryTimes; i++) {
            var blueBlock = generator.predict(targetIndex, blueOption);
            resultList.add(blueBlock);
            if (blueOption.isBlock(BallType.BLUE, 0, blueBlock.getBlueValue())) {
                return;
            }
            blueOption.addBlock(BallType.BLUE, 0, blueBlock.getBlueValue());
        }
    }

    private static void replaceBlueNeighbor(List<DoubleColorBall> resultList, int targetIndex, PredictOption option, DoubleColorBall origin, int tryTimes, DoubleColorAlgorithmRegistry generator) {
        // 替换blue
        var blueOption = option.clone();
        if (!extracted(origin, blueOption)) {
            return;
        }
        for (int i = 0; i < tryTimes; i++) {
            var blueBlock = generator.predict(targetIndex, blueOption);
            resultList.add(blueBlock);
            if (!extracted(blueBlock, blueOption)) {
                return;
            }
        }
    }

    private static boolean extracted(DoubleColorBall origin, PredictOption blueOption) {
        var success = false;
        blueOption.addBlock(BallType.BLUE, 0, origin.getBlueValue());
        if (origin.getBlueValue() > 2) {
            blueOption.addBlock(BallType.BLUE, 0, origin.getBlueValue() - 1);
            success = true;
        }
        if (origin.getBlueValue() < 15) {
            blueOption.addBlock(BallType.BLUE, 0, origin.getBlueValue() + 1);
            success = true;
        }
        return success;
    }
}
