package cqwang.doubleball.detection.algorithm.doublecolorball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SplitBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.PredictResult;
import cqwang.doubleball.detection.model.result.features.ValueFlag;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public DoubleColorBall predict(int targetIndex, PredictOption option) {
        // 获取样本数据
        var splitBall = new SplitBall(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBall();
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var singleBall = splitBall.getRedBall(redIndex);

            var predictRedResult = redInstance.predict(singleBall, option);
            predictResult.getRedValueList().add(predictRedResult.getResult());
            option.addBlocks(BallType.RED, redIndex+1, predictRedResult.getResult());

//
//            for (int tryTimes = 0; tryTimes < 6; tryTimes++) {
//                var predictRedResult = redInstance.predict(singleBall, option);
//                if (predictResult.getRedValueList().contains(predictRedResult.getResult())) {
//                    option.addBlock(BallType.RED, redIndex, predictRedResult.getResult());
//                } else {
//                    predictResult.getRedValueList().add(predictRedResult.getResult());
//                    break;
//                }
//            }
        }
        predictResult.getRedValueList().sort(Comparator.comparingInt(o -> o));

        var blueValue = blueInstance.predict(splitBall.getBlueBall(), option).getResult();
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

    @Override
    public List<DoubleColorBall> predictList(int targetIndex, PredictOption option) {
        var list = new ArrayList<DoubleColorBall>();

        var first = predict(targetIndex, option.clone());
        list.add(first);

        var index = 0;
        list.add(predict(targetIndex, option.cloneAndAddBlock(BallType.RED, index, first.getRedValueList().get(index))));


        index = first.getRedValueList().size() - 1;
        list.add(predict(targetIndex, option.cloneAndAddBlock(BallType.RED, index, first.getRedValueList().get(index))));

        var coldBlueList = AlgorithmUtils.findColdList(BallType.BLUE, 0,15);
        for(var blue: coldBlueList){
            list.add(first.clone(blue.getData(), null));
        }

        index = 0;
        var coldRedList = AlgorithmUtils.findColdList(BallType.RED, index,15);
        for(var red: coldRedList) {
            var result = predict(targetIndex, option.cloneAndSetAllow(BallType.RED, index, red.getData()));
            list.add(result);
            for (var blue : coldBlueList) {
                list.add(result.clone(blue.getData(), null));
            }
        }

        return list;
    }


    @JsonIgnore
    public String getUniqueName() {
        return redAlgorithm + "_" + blueAlgorithm;
    }
}
