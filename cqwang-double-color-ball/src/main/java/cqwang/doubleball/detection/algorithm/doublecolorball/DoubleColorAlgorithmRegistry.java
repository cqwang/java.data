package cqwang.doubleball.detection.algorithm.doublecolorball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SplitBall;
import cqwang.doubleball.detection.model.option.DoublePredictOption;
import cqwang.doubleball.detection.model.result.PredictResult;
import cqwang.doubleball.detection.model.result.features.ValueFlag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonPropertyOrder({"blueAlgorithm", "redAlgorithm", "predictResult"})
public class DoubleColorAlgorithmRegistry extends AlgorithmRegistry implements DoubleColorAlgorithm {

    /**
     * 红色球预测算法注册表名称列表
     */
    @Getter
    @Setter
    private String redAlgorithm;

    /**
     * 红色球预测算法实列列表
     */
    @JsonIgnore
    @Getter
    private SingleBallAlgorithm redInstance;


    /**
     * 蓝色球预测算法注册表名称
     */
    @Getter
    @Setter
    private String blueAlgorithm;

    /**
     * 蓝色球预测算法实例
     */
    @JsonIgnore
    @Getter
    private SingleBallAlgorithm blueInstance;


    public DoubleColorAlgorithmRegistry(SingleBallAlgorithmRegistry blue, SingleBallAlgorithmRegistry  red) {
        this.blueInstance = blue.getInstance();
        this.blueAlgorithm = blue.getAlgorithmName();
        this.redInstance = red.getInstance();
        this.redAlgorithm = red.getAlgorithmName();
    }

    public void initInstance(boolean resetHistoryValue) {
        this.blueInstance = SingleBallAlgorithmFactory.getAlgorithm(this.blueAlgorithm).getInstance();
        this.redInstance = SingleBallAlgorithmFactory.getAlgorithm(this.redAlgorithm).getInstance();

        if (resetHistoryValue) {
            this.setPredictResult(new PredictResult());
        }
    }



    @Override
    public DoubleColorBall predict(int targetIndex, DoublePredictOption option) {
        // 获取样本数据
        var splitBall = new SplitBall(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBall();
        // 红色
        var redOption = option.toPredictOption(ValueFlag.RED);
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var singleBall = splitBall.getRedBall(redIndex);

            for (int tryTimes = 0; tryTimes < 6; tryTimes++) {
                var predictRedResult = redInstance.predict(singleBall, redOption);
                if (predictResult.getRedValueList().contains(predictRedResult.getResult())) {
                    redOption.addBlock(predictRedResult.getResult());
                } else {
                    predictResult.getRedValueList().add(predictRedResult.getResult());
                    break;
                }
            }
        }

        var blueValue = blueInstance.predict(splitBall.getBlueBall(), option.toPredictOption(ValueFlag.BlUE)).getResult();
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }
}
