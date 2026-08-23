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
        // 移位算法
        if (origin.getRedValueList().get(0) <= 2) {
            // 第2个red前移
            var tempOption = option.cloneAndSetAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            ballList.add(generator.predict(targetIndex, tempOption));

            if (origin.getRedValueList().get(1) <= 9) {
                // 第2、3个red前移
                tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
                ballList.add(generator.predict(targetIndex, tempOption));
            }

            if (origin.getRedValueList().get(2) <= 16) {
                // 第2、3、4个red前移
                tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
                ballList.add(generator.predict(targetIndex, tempOption));
            }

            if (origin.getRedValueList().get(3) <= 25) {
                // 第2、3、4、5个red前移
                tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
                ballList.add(generator.predict(targetIndex, tempOption));
            }

            if (origin.getRedValueList().get(4) <= 27) {
                // 第2、3、4、5、6个red前移
                tempOption.setAllow(BallType.RED, 4, origin.getRedValueList().get(5));
                ballList.add(generator.predict(targetIndex, tempOption));
            }
        }
//
//        if (origin.getRedValueList().get(0) <= 2) {
//            // 第2个red前移
//            var tempOption = option.cloneAndSetAllow(BallType.RED, 0, origin.getRedValueList().get(1));
//            ballList.add(generator.predict(targetIndex, tempOption));
//
//            if (origin.getRedValueList().get(1) <= 9) {
//                // 第2、3个red前移
//                tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
//                ballList.add(generator.predict(targetIndex, tempOption));
//            }
//
//            if (origin.getRedValueList().get(2) <= 16) {
//                // 第2、3、4个red前移
//                tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
//                ballList.add(generator.predict(targetIndex, tempOption));
//            }
//
//            if (origin.getRedValueList().get(3) <= 25) {
//                // 第2、3、4、5个red前移
//                tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
//                ballList.add(generator.predict(targetIndex, tempOption));
//            }
//
//            if (origin.getRedValueList().get(4) <= 27) {
//                // 第2、3、4、5、6个red前移
//                tempOption.setAllow(BallType.RED, 4, origin.getRedValueList().get(5));
//                ballList.add(generator.predict(targetIndex, tempOption));
//            }
//        }

        var tempOption = option.clone();
        if (origin.getRedValueList().get(0) <= 2) {
            // 第2个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        if (origin.getRedValueList().get(1) <= 9) {
            // 第2、3个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        if (origin.getRedValueList().get(2) <= 13) {
            // 第2、3、4个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        if (origin.getRedValueList().get(3) <= 15) {
            // 第2、3、4、5个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
            ballList.add(generator.predict(targetIndex, tempOption));
        }

        if (origin.getRedValueList().get(4) <= 20) {
            // 第2、3、4、5、6个red前移
            tempOption.setAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            tempOption.setAllow(BallType.RED, 1, origin.getRedValueList().get(2));
            tempOption.setAllow(BallType.RED, 2, origin.getRedValueList().get(3));
            tempOption.setAllow(BallType.RED, 3, origin.getRedValueList().get(4));
            tempOption.setAllow(BallType.RED, 4, origin.getRedValueList().get(5));
            ballList.add(generator.predict(targetIndex, tempOption));
        }
    }
}
