package cqwang.doubleball.detection.utils;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.result.PredictValueModel;
import org.checkerframework.checker.nullness.qual.NonNull;

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
        int predictValue = getPredictValue(intersectionRedCount, equalsBlue);
        boolean hasRedEquals = intersectionRedCount >= 4;
        return new PredictValueModel(predictValue, equalsBlue, hasRedEquals, predictResult.getSimpleInfo());
    }

    private static @NonNull int getPredictValue(int intersectionRedCount, boolean equalsBlue) {
        if (intersectionRedCount == 6) {
            return equalsBlue ? 10000000 : 100000;
        }
        if (intersectionRedCount == 5) {
            return equalsBlue ? 3000 : 200;
        }
        if (intersectionRedCount == 4) {
            return equalsBlue ? 200 : 10;
        }

        return equalsBlue ? 5 : defaultValue;
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
