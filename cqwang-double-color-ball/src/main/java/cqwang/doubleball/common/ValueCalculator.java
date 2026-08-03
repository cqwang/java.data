package cqwang.doubleball.common;

import cqwang.doubleball.common.model.DoubleColorBallItem;

/**
 * 预测价值计算器
 */
public class ValueCalculator {
    private static int defaultValue = 0;


    public static int calculateBlue(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        boolean equalsBlue = predictResult.getBlueValue().intValue() == target.getBlueValue().intValue();
        return equalsBlue ? 5 : defaultValue;
    }

    /**
     * 计算预测结果的价值
     *
     * @param predictResult
     * @param target
     * @return
     */
    public static int calculate(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
        var intersectionRedCount = calculateIntersectionRedCount(predictResult, target);
        boolean equalsBlue = predictResult.getBlueValue().intValue() == target.getBlueValue().intValue();
        if (intersectionRedCount == 6) {
            return equalsBlue ? 10000000 : 100000;
        }
        if (intersectionRedCount == 5){
            return equalsBlue ? 3000 : 200;
        }
        if (intersectionRedCount == 4) {
            return equalsBlue ? 200 : 10;
        }

        return equalsBlue ? 5 : defaultValue;
    }

    private static int calculateIntersectionRedCount(DoubleColorBallItem predictResult, DoubleColorBallItem target) {
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
