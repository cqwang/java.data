package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.option.PredictOption;

import java.util.List;

/**
 * 只取预测成功的数据/ 只取预测失败的数据
 * 好像没啥用，因为red预测率太低
 */
public class PointSelector {
    public static void execute(
            List<DoubleColorBall> ballList,
            int targetIndex,
            PredictOption option,
            DoubleColorAlgorithmRegistry generator) {
        var origin = ballList.get(0);

    }
}
