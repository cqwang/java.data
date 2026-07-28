package cqwang.doubleball.algorithm.detection;

import cqwang.doubleball.model.BallDataDetail;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import cqwang.doubleball.preload.SampleDataRealtimeLoad;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Range;

/**
 * 预测算法接口
 */
public interface PredictionAlgorithm {
    /**
     * 生成预测结果
     *
     * @param targetIndex 要预测的目标数据位序
     * @return
     */
    default DoubleColorBallItem predict(int targetIndex) {
        // 获取样本数据
        var sampleDataRealtimeLoad = new SampleDataRealtimeLoad();
        sampleDataRealtimeLoad.execute(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBallItem(true);
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var redBallDetail = sampleDataRealtimeLoad.getRedBallData().getRedBallDetail(redIndex);
            var redRange = getRedRange(predictResult, redBallDetail);
            var predictRed = predictRed(redBallDetail, redRange);
            predictResult.getRedValueList().add(predictRed);
        }

        var blueValue = predictBlue(sampleDataRealtimeLoad.getBlueBallDetail(), Range.between(1, 16));
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

    int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange);

    int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange);


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
}
