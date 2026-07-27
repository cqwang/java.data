package cqwang.doubleball.algorithm;

import cqwang.doubleball.model.BallDataDetail;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
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
     *
     * @param preSampleNum               前置样本量
     * @param targetDoubleColorBallIndex 要预测的目标数据
     * @return
     */
    default DoubleColorBallItem predict(int preSampleNum, int targetDoubleColorBallIndex) {

        var sampleStartIndex = targetDoubleColorBallIndex - preSampleNum;
        if (sampleStartIndex < 0) {
            return null; // 样本不足，不预测
        }

        // 前序样本
        var preSampleList = DoubleColorBallDataPreload.allData().subList(sampleStartIndex, preSampleNum);
        // 目标数据
        var target = DoubleColorBallDataPreload.allData().get(targetDoubleColorBallIndex);

        // 预测结果
        var predictResult = new DoubleColorBallItem(true);
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var redBallDetail = RedBallDataPreload.redBallData().getRedBallDetail(redIndex);
            var redRange = getRedRange(predictResult, redBallDetail);
            var predictRed = predictRed(redBallDetail, redRange);
            predictResult.getRedValueList().add(predictRed);
        }
        predictResult.setBlueValue(predictBlue(RedBallDataPreload.blueBallDetail()));
        return predictResult;
    }

    int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange);

    int predictBlue(BallDataDetail redBallDataDetail);


    /**
     * 获取要预测位序红球的范围
     *
     * @param result
     * @param redBallDetail
     * @return
     */
    default Range<Integer> getRedRange(DoubleColorBallItem result, BallDataDetail redBallDetail) {
        if (CollectionUtils.isEmpty(result.getRedValueList())) {
            return Range.between(redBallDetail.getMin(), redBallDetail.getMax());
        }

        var lastRed = result.getRedValueList().get(result.getRedValueList().size() - 1);
        if (lastRed <= redBallDetail.getMin()) {
            return Range.between(redBallDetail.getMin(), redBallDetail.getMax());
        }

        return Range.between(lastRed + 1, redBallDetail.getMax());
    }

    /**
     * 校验生成的红色球数值是否合法
     *
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
        var lastRed = result.getRedValueList().get(result.getRedValueList().size() - 1);
        if (predictValue <= lastRed) {
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
