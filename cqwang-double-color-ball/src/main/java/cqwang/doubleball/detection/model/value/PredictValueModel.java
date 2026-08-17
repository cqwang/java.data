package cqwang.doubleball.detection.model.value;

import cqwang.doubleball.detection.model.value.features.ValueFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictValueModel {
    /**
     * 预测价值
     */
    private int predictValue = 0;

    private boolean firstRed = false;

    /**
     * 价值标签
     */
    private ValueFlag valueFlag = ValueFlag.NONE;
}
