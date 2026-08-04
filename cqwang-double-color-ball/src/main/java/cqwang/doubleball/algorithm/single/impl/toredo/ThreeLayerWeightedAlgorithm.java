package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 红球三层加权算法 - 三个不同时间窗口的加权组合
 */
public class ThreeLayerWeightedAlgorithm implements SingleAlgorithm {
    private static final int LAYER1 = 18;   // 极近期
    private static final int LAYER2 = 40;   // 近期
    private static final int LAYER3 = 75;   // 中期

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictValue(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictValue(blueBallDataDetail, blueRange);
    }

    private int predictValue(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        int layer1Start = Math.max(0, dataList.size() - LAYER1);
        int layer2Start = Math.max(0, dataList.size() - LAYER2);
        int layer3Start = Math.max(0, dataList.size() - LAYER3);

        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int freq1 = 0, freq2 = 0, freq3 = 0;

            for (int j = layer1Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq1++;
            }

            for (int j = layer2Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq2++;
            }

            for (int j = layer3Start; j < dataList.size(); j++) {
                if (dataList.get(j) == i) freq3++;
            }

            // 三层权重：9:4:1
            double score = freq1 * 9.0 + freq2 * 4.0 + freq3 * 1.0;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
