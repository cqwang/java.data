package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * 蓝球缺失补偿修正算法 - 结合缺失值补偿和最近频率
 */
public class AbsenceCompensationAlgorithm implements PredictionAlgorithm {
    private static final int WINDOW_SIZE = 40;

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

        // 第一步：计算每个值在窗口内的出现频率
        int windowStart = Math.max(0, dataList.size() - WINDOW_SIZE);
        double maxScore = 0;
        int result = range.getMinimum();

        for (int i = range.getMinimum(); i <= range.getMaximum(); i++) {
            int windowFreq = 0;
            int totalFreq = 0;

            // 计算窗口内频率和总频率
            for (int j = windowStart; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    windowFreq++;
                }
            }

            for (int j = 0; j < dataList.size(); j++) {
                if (dataList.get(j) == i) {
                    totalFreq++;
                }
            }

            // 计算缺失惩罚（长时间未出现的值应该被考虑）
            int lastAppearIndex = -1;
            for (int j = dataList.size() - 1; j >= 0; j--) {
                if (dataList.get(j) == i) {
                    lastAppearIndex = j;
                    break;
                }
            }

            double absencePenalty = 1.0;
            if (lastAppearIndex >= 0) {
                int absentCount = dataList.size() - lastAppearIndex - 1;
                // 未出现超过窗口大小则降低评分
                if (absentCount > WINDOW_SIZE / 2) {
                    absencePenalty = 0.7;
                }
            } else {
                // 从未出现过
                absencePenalty = 0.3;
            }

            // 综合评分：窗口频率权重较高，长期频率权重次之，缺失补偿
            double score = windowFreq * 3.0 + totalFreq * 0.5;
            score *= absencePenalty;

            if (score > maxScore) {
                maxScore = score;
                result = i;
            }
        }

        return result;
    }
}
