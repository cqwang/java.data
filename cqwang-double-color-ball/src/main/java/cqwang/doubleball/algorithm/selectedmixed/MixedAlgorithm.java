package cqwang.doubleball.algorithm.selectedmixed;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.SampleDataRealtimeLoad;
import lombok.Data;
import org.apache.commons.lang3.Range;

import java.text.MessageFormat;

@Data
public class MixedAlgorithm {

    private PredictionAlgorithm blueAlgorithm;
    private PredictionAlgorithm redAlgorithm;
    /**
     * 历史预测价值合计
     */
    private int historyPredictValueSum;

    /**
     * 历史命中次数
     */
    private int historyHitCount;

    public MixedAlgorithm(){}

    public MixedAlgorithm(PredictionAlgorithm blueAlgorithm, PredictionAlgorithm redAlgorithm) {
        this.blueAlgorithm = blueAlgorithm;
        this.redAlgorithm = redAlgorithm;
    }

    public DoubleColorBallItem predict(int targetIndex) {
        // 获取样本数据
        var sampleDataRealtimeLoad = new SampleDataRealtimeLoad();
        sampleDataRealtimeLoad.execute(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBallItem(true);
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var redBallDetail = sampleDataRealtimeLoad.getRedBallData().getRedBallDetail(redIndex);
            var redRange = redAlgorithm.getRedRange(predictResult, redBallDetail);
            var predictRed = redAlgorithm.predictRed(redBallDetail, redRange);
            predictResult.getRedValueList().add(predictRed);
        }

        var blueValue = blueAlgorithm.predictBlue(sampleDataRealtimeLoad.getBlueBallDetail(), Range.between(1, 16));
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

    public String toString(){
        return MessageFormat.format("red: {0}, blue:{1}, value:{2}",
                redAlgorithm.getClass().getName(),
                blueAlgorithm.getClass().getName(),
                historyPredictValueSum
                );
    }
}
