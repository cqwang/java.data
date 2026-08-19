package cqwang.doubleball.detection.utils;

import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;

import java.util.Comparator;

public class CompareUtils {
    public static final Comparator<AlgorithmRegistry> PREDICT_RESULT_COMPARE = (left, right) -> {
        var profitDiff = left.getPredictResult().getProfit() - right.getPredictResult().getProfit();
        if (profitDiff == 0) {
            var recentProfit = left.getPredictResult().getRecentProfit(100) - right.getPredictResult().getRecentProfit(100);
            if (recentProfit == 0) {
                return right.getPredictResult().getHitTotalCount() - left.getPredictResult().getHitTotalCount();
            }
            return -recentProfit;
        }
        return -profitDiff;
    };

}
