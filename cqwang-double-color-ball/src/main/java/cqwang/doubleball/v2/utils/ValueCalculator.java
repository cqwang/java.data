package cqwang.doubleball.v2.utils;

import cqwang.doubleball.v2.model.data.DoubleColorBall;
import cqwang.doubleball.v2.model.value.PredictValueModel;
import cqwang.doubleball.v2.model.value.features.ValueFlag;

public class ValueCalculator {
    private static int defaultValue = 0;

    /**
     * 计算预测结果的价值
     *
     * @param predictResult
     * @param target
     * @return
     */
    public static PredictValueModel calculate(DoubleColorBall predictResult, DoubleColorBall target) {
        var intersectionRedCount = calculateIntersectionRedCount(predictResult, target);
        boolean equalsBlue = predictResult.getBlueValue().intValue() == target.getBlueValue().intValue();
        if (intersectionRedCount == 6) {
            return equalsBlue ? new PredictValueModel(10000000, ValueFlag.BLUE_RED) : new PredictValueModel(100000, ValueFlag.RED);
        }
        if (intersectionRedCount == 5) {
            return equalsBlue ? new PredictValueModel(3000, ValueFlag.BLUE_RED) : new PredictValueModel(200, ValueFlag.RED);
        }
        if (intersectionRedCount == 4) {
            return equalsBlue ? new PredictValueModel(200, ValueFlag.BLUE_RED) : new PredictValueModel(10, ValueFlag.RED);
        }

        return equalsBlue ? new PredictValueModel(5, ValueFlag.BlUE) : new PredictValueModel(defaultValue, ValueFlag.NONE);
    }

    private static int calculateIntersectionRedCount(DoubleColorBall predictResult, DoubleColorBall target) {
        var intersectionRedCount = 0;
        for (var predictRed : predictResult.getRedValueList()) {
            if (target.getRedValueList().contains(predictRed)) {
                intersectionRedCount++;
            }
        }
        return intersectionRedCount;
    }


    public static boolean hasNoValue(int value) {
        return value <= defaultValue;
    }
}
