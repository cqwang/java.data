package cqwang.doubleball.detection.model.result;

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

    /**
     * 价值标签
     */
    private ValueFlag valueFlag = ValueFlag.NONE;

    public PredictValueModel(int predictValue, boolean equalsBlue, boolean hasRedEquals) {
        this.predictValue = predictValue;

        if (equalsBlue && hasRedEquals) {
            valueFlag = ValueFlag.BLUE_RED;
        } else if (equalsBlue) {
            valueFlag = ValueFlag.BlUE;
        } else if (hasRedEquals) {
            valueFlag = ValueFlag.RED;
        }
    }
}
