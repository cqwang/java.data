package cqwang.doubleball.common;

import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.List;

public class AverageAlgorithmUtils {

    /**
     * 使用加权平均替代简单平均，强调最近数据权重
     * @param
     * @param windowSize
     * @return
     */
    public static int weightAverage(BallDataDetail ballDataDetail, Range<Integer> redRange, int windowSize){
        double weighted = calculateWeightedAverage(
                ballDataDetail.getDataList(), windowSize);
        return Math.max(redRange.getMinimum(),
                Math.min(redRange.getMaximum(), (int) Math.round(weighted)));
    }

    /**
     * 计算加权平均
     */
    private static double calculateWeightedAverage(List<Integer> dataList, int windowSize) {
        if (dataList.isEmpty()) {
            return 0;
        }

        int startIdx = Math.max(0, dataList.size() - windowSize);
        double sum = 0;
        double totalWeight = 0;

        for (int i = startIdx; i < dataList.size(); i++) {
            double weight = 1.0 + (double) (i - startIdx) / windowSize;
            sum += dataList.get(i) * weight;
            totalWeight += weight;
        }

        return sum / totalWeight;
    }
}
