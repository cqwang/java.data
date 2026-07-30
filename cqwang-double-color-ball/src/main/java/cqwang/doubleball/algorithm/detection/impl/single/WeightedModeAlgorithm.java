package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球加权众数算法 - 最近出现的频率最高值权重更高
 */
public class WeightedModeAlgorithm implements PredictionAlgorithm {
    private static final int SAMPLE_SIZE = 80;

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

        int startIdx = Math.max(0, dataList.size() - SAMPLE_SIZE);

        // 计算每个值的加权频率
        double maxWeightedFreq = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            double weightedFreq = 0;
            int occurrenceIndex = 0;

            for (int j = startIdx; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    // 距离越近权重越高
                    double weight = 1.0 + (double) (j - startIdx) / (dataList.size() - startIdx);
                    weightedFreq += weight;
                    occurrenceIndex++;
                }
            }

            // 平滑处理：防止单一高权重项过度影响
            if (occurrenceIndex > 0) {
                weightedFreq /= occurrenceIndex;
                weightedFreq *= occurrenceIndex;
            }

            if (weightedFreq > maxWeightedFreq) {
                maxWeightedFreq = weightedFreq;
                result = i;
            }
        }

        return result;
    }
}
