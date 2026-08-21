package cqwang.doubleball.detection.model.result;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.result.features.ValueFlag;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PredictValueModel {
    /**
     * 预测价值
     */
    private int predictValue = 0;

    private String predictResult;

    private String actualResult;

    /**
     * 价值标签
     */
    private ValueFlag valueFlag = ValueFlag.NONE;

    public PredictValueModel(int predictValue, boolean equalsBlue, boolean hasRedEquals, String predictResult, String actualResult) {
        this.predictValue = predictValue;
        this.predictResult = predictResult;
        this.actualResult = actualResult;

        if (equalsBlue && hasRedEquals) {
            valueFlag = ValueFlag.BLUE_RED;
        } else if (equalsBlue) {
            valueFlag = ValueFlag.BlUE;
        } else if (hasRedEquals) {
            valueFlag = ValueFlag.RED;
        }
    }
}
