package cqwang.doubleball.algorithm.single.impl.milestone;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 贝叶斯网络模拟算法 - 基于概率推理的预测
 */
public class BayesianNetworkDownAlgorithm implements SingleAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByBayesian(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByBayesian(blueBallDataDetail, blueRange);
    }

    private int predictByBayesian(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 计算后验概率：P(next|last)
        int lastValue = dataList.get(dataList.size() - 1);
        int nextCount = 0;
        int nextSum = 0;

        // 计算在lastValue之后出现的值的分布
        for (int i = 0; i < dataList.size() - 1; i++) {
            if (dataList.get(i) == lastValue) {
                nextSum += dataList.get(i + 1);
                nextCount++;
            }
        }

        int prediction = nextCount > 0 ? next(nextSum, nextCount) : lastValue;
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }

    int next(double nextSum, int nextCount) {
        return (int) Math.floor(nextSum / nextCount);
    }
}
