package cqwang.doubleball.detection.utils;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.value.PredictValueModel;
import cqwang.doubleball.detection.model.value.features.ValueFlag;

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
        boolean firstRed = predictResult.getRedValueList().get(0) == target.getRedValueList().get(0);
        if (intersectionRedCount == 6) {
            return equalsBlue ? new PredictValueModel(10000000, firstRed, ValueFlag.BLUE_RED) : new PredictValueModel(100000,firstRed, ValueFlag.RED);
        }
        if (intersectionRedCount == 5) {
            return equalsBlue ? new PredictValueModel(3000,firstRed, ValueFlag.BLUE_RED) : new PredictValueModel(200,firstRed, ValueFlag.RED);
        }
        if (intersectionRedCount == 4) {
            return equalsBlue ? new PredictValueModel(200,firstRed, ValueFlag.BLUE_RED) : new PredictValueModel(10,firstRed, ValueFlag.RED);
        }

        return equalsBlue ? new PredictValueModel(5, firstRed, ValueFlag.BlUE) : new PredictValueModel(defaultValue, firstRed, ValueFlag.NONE);
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
