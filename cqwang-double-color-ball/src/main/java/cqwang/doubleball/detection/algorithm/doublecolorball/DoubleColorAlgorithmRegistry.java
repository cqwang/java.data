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


    public DoubleColorAlgorithmRegistry(SingleBallAlgorithmRegistry blue, SingleBallAlgorithmRegistry red) {
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

            if (option.hasFirstRedAllow()) {
                option.addBlocks(BallType.RED, redIndex + 1, predictResult.getRedValueList().get(0));
            }

            for (int j = 0; j <= redIndex; j++) {
                option.addBlock(BallType.RED, redIndex + 1, predictResult.getRedValueList().get(j));
            }
        }
        predictResult.getRedValueList().sort(Comparator.comparingInt(o -> o));

        var blueValue = blueInstance.predict(splitBall.getBlueBall(), option).getResult();
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

    @Override
    public List<DoubleColorBall> predictList(int targetIndex, PredictOption option) {
        var list = new ArrayList<DoubleColorBall>();

        var origin = predict(targetIndex, option.clone());
        list.add(origin);


        // 替换first red
        replaceRed(list, targetIndex, option, origin, 0, 5);


        // 替换last red
        replaceRed(list, targetIndex, option, origin, origin.getRedValueList().size() - 1, 1);


        // 使用冷blue
        var coldBlueList = AlgorithmUtils.findColdList(BallType.BLUE, 0, 15);
        for (var blue : coldBlueList) {
            list.add(origin.clone(blue.getData(), null));
        }
//
        // 使用冷blue red
        int index = 0;
        var coldRedList = AlgorithmUtils.findColdList(BallType.RED, index, 30);
        for (var red : coldRedList) {
            var result = predict(targetIndex, option.cloneAndSetAllow(BallType.RED, index, red.getData()));
            list.add(result);
        }

        return list;
    }

    private void replaceRed(List<DoubleColorBall> resultList, int targetIndex, PredictOption option, DoubleColorBall origin, int index, int tryTimes) {
        var firstRedOption = option.cloneAndAddBlock(BallType.RED, index, origin.getRedValueList().get(index));
        for (int i = 0; i < tryTimes; i++) {
            var firstRedBlock = predict(targetIndex, firstRedOption);
            resultList.add(firstRedBlock);
            firstRedOption.addBlock(BallType.RED, index, firstRedBlock.getRedValueList().get(index));
        }
    }


    @JsonIgnore
    public String getUniqueName() {
        return redAlgorithm + "_" + blueAlgorithm;
    }
}
