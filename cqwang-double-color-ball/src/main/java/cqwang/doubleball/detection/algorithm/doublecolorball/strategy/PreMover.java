package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;

import java.util.List;

/**
 * red迁移算法
 */
public class PreMover {
    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);

        var tempOption = option.clone();
        var flag0 = origin.getRedValueList().get(0) <= 2;
        if (flag0) {
            // 第2个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        var flag1 = origin.getRedValueList().get(1) <= 9;
        if (flag0 && flag1) {
            // 第2、3个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        var flag2 = origin.getRedValueList().get(2) <= 15;
        if (flag0 && flag1 && flag2) {
            // 第2、3、4个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        var flag3 = origin.getRedValueList().get(3) <= 15;
        if (flag0 && flag1 && flag2 && flag3) {
            // 第2、3、4、5个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        var flag4 = origin.getRedValueList().get(4) <= 30;
        if (flag0 && flag1 && flag2 && flag3 && flag4) {
            // 第2、3、4、5、6个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
            tempOption.setAllow(BallType.RED, 4, origin.getRedValueList().get(5));
            ballList.add(generator.predict(targetIndex, tempOption));
        }


    }

    public static void specialMove(List<DoubleColorBall> ballList,
                                   int targetIndex,
                                   PredictOption option,
                                   DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);
        var tempOption = option.clone();

//        var flag0 = origin.getRedValueList().get(0) <= 2;
//        if (flag0) {
//            tempOption.addBlock(BallType.RED, 0, origin.getRedValueList().get(0));
//            tempOption.addBlock(BallType.RED, 2, origin.getRedValueList().get(2));
//            tempOption.addBlock(BallType.RED, 4, origin.getRedValueList().get(4));
//            ballList.add(generator.predict(targetIndex, tempOption));
//        }
//
//        var flag3 = origin.getRedValueList().get(4) - origin.getRedValueList().get(3) <= 4;
//        if (flag0 && flag3) {
//            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
//            ballList.add(generator.predict(targetIndex, tempOption));
//        }


//        var flag0 = origin.getRedValueList().get(4) - origin.getRedValueList().get(2) <= 8
//                &&  origin.getRedValueList().get(4) - origin.getRedValueList().get(2)>=6
//                && origin.getRedValueList().get(5) - origin.getRedValueList().get(0)>=31;
//        if (flag0) {
//            tempOption.addBlock(BallType.RED, 0, origin.getRedValueList().get(0));
//            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
//            ballList.add(generator.predict(targetIndex, tempOption));
//        }


//        var flag2 = origin.getRedValueList().get(2) / 2 > origin.getRedValueList().get(1);
//        if (flag01 && flag2) {
//            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(2) / 2);
//        }
//
//        var flag3 = origin.getRedValueList().get(3) * 3 / 4 > origin.getRedValueList().get(2);
//        if (flag01 && flag2 && flag3) {
//            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(3) * 3 / 4);
//            ballList.add(generator.predict(targetIndex, tempOption));
//        }

    }
}
