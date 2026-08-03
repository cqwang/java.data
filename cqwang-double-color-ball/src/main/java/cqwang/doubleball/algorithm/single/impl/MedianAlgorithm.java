package cqwang.doubleball.algorithm.single.impl;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.helper.ListUtils;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 中位数算法 - 基于历史数据的中位数进行预测
 */
public class MedianAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return calculateMedian(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return calculateMedian(blueBallDataDetail, blueRange);
    }

    private int calculateMedian(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }
        var sortedCopy = ListUtils.getSortedList(dataList);
        int median = sortedCopy.get(sortedCopy.size() / 2);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), median));
    }
}
