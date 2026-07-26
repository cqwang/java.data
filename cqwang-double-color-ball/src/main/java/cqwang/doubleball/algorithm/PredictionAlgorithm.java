package cqwang.doubleball.algorithm;

import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.RedBallDataPreload;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Range;

import java.util.List;

/**
 * 预测算法接口
 */
public interface PredictionAlgorithm {
    /**
     * 生成预测结果
     * @param historicalData
     * @return
     */
    DoubleColorBallItem predict(List<DoubleColorBallItem> historicalData);

    /**
     * 获取要预测位序红球的范围
     * @param result
     * @param predictIndex
     * @return
     */
    default Range<Integer> getRedRange(DoubleColorBallItem result, int predictIndex) {
        var redBallDetail = RedBallDataPreload.redBallData().getRedBallDetail(predictIndex);
        if (CollectionUtils.isEmpty(result.getRedValueList())) {
            return Range.between(redBallDetail.getMin(), redBallDetail.getMax());
        }

        var lastRed = result.getRedValueList().getLast();
        if (lastRed <= redBallDetail.getMin()) {
            return Range.between(redBallDetail.getMin(), redBallDetail.getMax());
        }

        return Range.between(lastRed + 1, redBallDetail.getMax());
    }

    /**
     * 校验生成的红色球数值是否合法
     * @param result
     * @param predictIndex
     * @param predictValue
     */
    default boolean validateRed(DoubleColorBallItem result, int predictIndex, int predictValue) {
        if (CollectionUtils.isEmpty(result.getRedValueList())) {
            return true;
        }

        // 不能重复
        if (result.getRedValueList().contains(predictValue)) {
            return false;
        }

        // 数值只能按照位序变大
        if (predictValue <= result.getRedValueList().getLast()) {
            return false;
        }

        var redBallDetail = RedBallDataPreload.redBallData().getRedBallDetail(predictIndex);
        // 数值区间约束
        if (predictValue > redBallDetail.getMax() || predictValue < redBallDetail.getMin()) {
            return false;
        }
        return true;
    }
}
