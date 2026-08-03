package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.helper.ListUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 四分位数算法 - 基于四分位数的鲁棒预测
 */
public class QuartileAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByQuartile(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByQuartile(blueBallDataDetail, blueRange);
    }

    private int predictByQuartile(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        var reversedDataList = ListUtils.getSortedList(dataList);

        int q1Index = reversedDataList.size() / 4;
        int q3Index = 3 * reversedDataList.size() / 4;

        int q1 = reversedDataList.get(q1Index);
        int q3 = reversedDataList.get(q3Index);

        // 取四分位数范围的中点
        int result = (q1 + q3) / 2;
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
