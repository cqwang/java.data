package cqwang.doubleball.algorithm.selectedmixed;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.SampleDataRealtimeLoad;
import lombok.Data;
import org.apache.commons.lang3.Range;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MixedAlgorithm {

    private PredictionAlgorithm redAlgorithm;
    private PredictionAlgorithm blueAlgorithm;
    private PredictionAlgorithm[] algorithmArray;

    /**
     * 历史预测价值合计
     */
    private int historyPredictValueSum;

    /**
     * 历史命中次数
     */
    private int historyHitCount;

    public MixedAlgorithm() {
    }

    public MixedAlgorithm(PredictionAlgorithm... algorithmArray) {
        this.redAlgorithm = algorithmArray[0];
        this.blueAlgorithm = algorithmArray[algorithmArray.length - 1];
        if (algorithmArray.length == 7) {
            this.algorithmArray = algorithmArray;
        }
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
            var selectedRedAlgorithm = algorithmArray == null ? redAlgorithm : algorithmArray[redIndex];
            var predictRed = selectedRedAlgorithm.predictRed(redBallDetail, redRange);
            predictResult.getRedValueList().add(predictRed);
        }

        var blueValue = blueAlgorithm.predictBlue(sampleDataRealtimeLoad.getBlueBallDetail(), Range.between(1, 16));
        predictResult.setBlueValue(blueValue);
        sampleDataRealtimeLoad = null;
        return predictResult;
    }

    public String toString() {
        if (algorithmArray == null) {
            return MessageFormat.format("red: {0}, blue:{1}, value:{2}",
                    redAlgorithm.getClass().getSimpleName(),
                    blueAlgorithm.getClass().getSimpleName(),
                    historyPredictValueSum
            );
        }

        return MessageFormat.format("algorithmArray: {0}, value:{1}, maxAmount:{2}",
                String.join(",", Arrays.stream(algorithmArray).map(t -> t.getClass().getSimpleName()).collect(Collectors.toList())),
                historyPredictValueSum,
                historyHitCount);
    }
}
