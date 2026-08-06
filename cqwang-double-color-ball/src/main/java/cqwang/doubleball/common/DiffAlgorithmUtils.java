package cqwang.doubleball.common;

import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 差值算法
 */
public class DiffAlgorithmUtils {

    /**
     * 差分平滑算法 - 基于一阶差分的平滑预测
     * @param ballDataDetail
     * @param range
     * @return
     */
    public static int predictByDifferentialSmoothing(BallDataDetail ballDataDetail, Range<Integer> range, int sampleSize) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.size() < 2) {
            return range.getMinimum();
        }

        // 计算差分序列
        double diffSum = 0;
        var startIndex = ballDataDetail.getDataList().size() - sampleSize;
        if(startIndex< 1){
            startIndex = 1;
        }
        for (int i = startIndex; i < dataList.size(); i++) {
            diffSum += dataList.get(i) - dataList.get(i - 1);
        }

        double avgDiff = diffSum / (dataList.size() - 1);
        int prediction = (int) Math.round(dataList.get(dataList.size() - 1) + avgDiff);

        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }
}
