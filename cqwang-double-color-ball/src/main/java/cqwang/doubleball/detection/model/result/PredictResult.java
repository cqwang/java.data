package cqwang.doubleball.detection.model.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import cqwang.doubleball.detection.utils.ValueCalculator;
import cqwang.doubleball.detection.model.result.features.ValueFlag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史预测总价值汇总
 */
@Data
@JsonPropertyOrder({"profit", "sumValue", "sumCost", "maxValue","hitTotalCount","hitBlueTotalCount","hitRedTotalCount"})
public class PredictResult {

    private int sumCost = 0;

    /**
     * 预测价值合计
     */
    private int sumValue = 0;

    /**
     * 命中次数
     */
    private int hitTotalCount = 0;

    /**
     * 命中篮球次数
     */
    private int hitBlueTotalCount = 0;

    private int hitRedTotalCount = 0;

    /**
     * 最大价值
     */
    private int maxValue;

    /**
     * 预测价值曲线，按照索引正序
     */
    @JsonIgnore
    private List<PredictPointValue> predictPointList;

    public PredictResult() {
        this.predictPointList = new ArrayList<>();
    }

    public void add(int predictIndex, PredictValueModel predictValueModel) {
        sumCost += 2;
        if (ValueCalculator.hasNoValue(predictValueModel.getPredictValue())) {
            return;
        }

        this.sumValue += predictValueModel.getPredictValue();
        this.hitTotalCount++;
        if (predictValueModel.getValueFlag() == ValueFlag.BlUE || predictValueModel.getValueFlag() == ValueFlag.BLUE_RED) {
            this.hitBlueTotalCount++;
        }
        if(predictValueModel.getValueFlag() == ValueFlag.RED || predictValueModel.getValueFlag() == ValueFlag.BLUE_RED){
            this.hitRedTotalCount++;
        }

        if (predictValueModel.getPredictValue() > maxValue) {
            maxValue = predictValueModel.getPredictValue();
        }
        this.predictPointList.add(new PredictPointValue(predictIndex, predictValueModel));
    }

    public int getProfit() {
        return sumValue - sumCost;
    }

    public int getRecentProfit(int recentSize) {
        int sumValue = 0;
        int sumCost = recentSize * 2;
        var startIndex = DoubleColorBallPreload.getAllData().size() - recentSize;
        for (PredictPointValue point : this.predictPointList) {
            if (point.getPredictIndex() < startIndex) {
                continue;
            }
            sumValue += point.getPredictValue().getPredictValue();
        }
        return sumValue - sumCost;
    }
}
