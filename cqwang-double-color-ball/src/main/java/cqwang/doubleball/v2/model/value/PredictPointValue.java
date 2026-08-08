package cqwang.doubleball.v2.model.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在某个点的预测价值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictPointValue {

    /**
     * 预测位置索引
     */
    private int predictIndex;

    /**
     * 预测价值
     */
    private PredictValueModel predictValue;
}
