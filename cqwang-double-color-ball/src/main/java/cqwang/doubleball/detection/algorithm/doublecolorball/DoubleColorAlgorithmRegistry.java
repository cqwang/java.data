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

import java.util.*;


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

            var nextAllow = option.nextRedAllow(redIndex);
            if (nextAllow != null) {
                option.addBlocks(BallType.RED, redIndex, nextAllow, 33);
            }


            var predictRedResult = redInstance.predict(singleBall, option);
            predictResult.getRedValueList().add(predictRedResult.getResult());

            for (int j = 0; j <= redIndex; j++) {
                option.addBlock(BallType.RED, redIndex + 1, predictResult.getRedValueList().get(j));
                if(option.hasRedAllow(j)){
                    option.addBlocks(BallType.RED, redIndex + 1, 1, predictResult.getRedValueList().get(j));
                }
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


        // 使用冷red
        int index = 0;
        var coldRedList = AlgorithmUtils.findColdList(BallType.RED, index, 30);
        for (var red : coldRedList) {
            var result = predict(targetIndex, option.cloneAndSetAllow(BallType.RED, index, red.getData()));
            list.add(result);
        }

        // 如果大家推荐的雷同，则补全缺失
        Maintainer.vote(list, option, targetIndex, this);

        // 移位算法
        if (origin.getRedValueList().get(0) <= 6) {
            // 第二个red作为第一个red的黑名单
            var secondRedOption = option.cloneAndAddBlock(BallType.RED, 0, origin.getRedValueList().get(1));
            list.add(predict(targetIndex, secondRedOption));

            // 第二个red前移
            secondRedOption = option.cloneAndSetAllow(BallType.RED, 0, origin.getRedValueList().get(1));
            list.add(predict(targetIndex, secondRedOption));
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

    private void replaceBlue() {
//        if(origin.getBlueValue()<4){
//            // 替换blue
//            var blueOption = option.cloneAndAddBlock(BallType.BLUE, 0, origin.getBlueValue());
//            for (int i = 0; i < 1; i++) {
//                var blueBlock = predict(targetIndex, blueOption);
//                list.add(blueBlock);
//                blueOption.addBlock(BallType.BLUE, 0, blueBlock.getBlueValue());
//            }
//        }
    }


    @JsonIgnore
    public String getUniqueName() {
        return redAlgorithm + "_" + blueAlgorithm;
    }
}
