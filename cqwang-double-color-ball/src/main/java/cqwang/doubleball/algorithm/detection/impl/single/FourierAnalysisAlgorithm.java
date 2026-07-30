package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 傅里叶分析算法 - 基于频域分析的周期预测
 */
public class FourierAnalysisAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByFourier(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByFourier(blueBallDataDetail, blueRange);
    }

    private int predictByFourier(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        // 简化的傅里叶分析：计算周期成分
        double dc = 0; // 直流分量
        double ac = 0; // 交流分量

        for (int i = 0; i < dataList.size(); i++) {
            double phase = 2 * Math.PI * i / Math.max(1, dataList.size());
            dc += dataList.get(i);
            ac += dataList.get(i) * Math.cos(phase);
        }

        int result = (int) Math.round((dc + ac) / Math.max(1, 2 * dataList.size()));
        result = Math.abs(result);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), result));
    }
}
