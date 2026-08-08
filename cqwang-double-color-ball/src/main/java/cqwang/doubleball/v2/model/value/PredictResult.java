package cqwang.doubleball.v2.model.value;

import cqwang.doubleball.v2.utils.ValueCalculator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史预测总价值汇总
 */
@Data
public class PredictResult {
    /**
     * 预测价值合计
     */
    private int sumValue = 0;

    /**
     * 命中次数
     */
    private int hitTotalCount = 0;

    /**
     * 最大价值
     */
    private int maxValue;

    /**
     * 预测价值曲线，按照索引正序
     */
    private List<PredictPointValue> predictPointList;

    public PredictResult() {
        this.predictPointList = new ArrayList<>();
    }

    public void add(int predictIndex, PredictValueModel predictValueModel) {
        if (ValueCalculator.hasNoValue(predictValueModel.getPredictValue())) {
            return;
        }

        this.sumValue += predictValueModel.getPredictValue();
        this.hitTotalCount++;
        if (predictValueModel.getPredictValue() > maxValue) {
            maxValue = predictValueModel.getPredictValue();
        }
        this.predictPointList.add(new PredictPointValue(predictIndex, predictValueModel));
    }
}
