package cqwang.doubleball.algorithm.detection.single.single;

import cqwang.doubleball.algorithm.detection.single.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * LSTM模拟算法 - 基于长短期记忆的时序预测
 */
public class LSTMSimulationAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByLSTM(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByLSTM(blueBallDataDetail, blueRange);
    }

    private int predictByLSTM(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.size() < 5) {
            return range.getMinimum();
        }

        // 模拟LSTM的细胞状态和隐藏状态
        int seqLength = 5;
        double cellState = 0;
        double hiddenState = 0;

        int startIdx = Math.max(0, dataList.size() - seqLength);
        for (int i = startIdx; i < dataList.size(); i++) {
            double input = dataList.get(i);
            cellState = 0.7 * cellState + 0.3 * input;
            hiddenState = 0.6 * hiddenState + 0.4 * input;
        }

        int prediction = (int) Math.round((cellState + hiddenState) / 2);
        return Math.max(range.getMinimum(), Math.min(range.getMaximum(), prediction));
    }
}
