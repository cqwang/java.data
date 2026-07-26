package cqwang.doubleball.algorithm;

import cqwang.doubleball.model.DoubleColorBallItem;

import java.util.HashSet;

/**
 * 预测价值计算器
 */
public class ValueCalculator {
    /**
     * 计算预测结果的价值
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
        if (intersectionRedCount == 5 && equalsBlue) {
            return 3000;
        }
        return 0;
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
}
