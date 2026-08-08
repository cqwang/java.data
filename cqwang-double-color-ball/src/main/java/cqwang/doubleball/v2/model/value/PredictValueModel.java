package cqwang.doubleball.v2.model.value;

import cqwang.doubleball.v2.model.value.features.ValueFlag;
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

    private ValueFlag valueFlag = ValueFlag.NONE;
}
