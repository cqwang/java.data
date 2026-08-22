package cqwang.doubleball.detection.utils;

import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public static List<Integer> getRedDiffIndexList(DoubleColorBall left, DoubleColorBall right) {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < left.getRedValueList().size(); i++) {
            if (left.getRedValueList().get(i) != right.getRedValueList().get(i)) {
                list.add(i);
            }
        }
        return list;
    }

}
