package cqwang.doubleball.detection.algorithm.doublecolorball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cqwang.doubleball.detection.algorithm.AlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithm;
import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.batchball.BatchBallAlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmRegistry;
import cqwang.doubleball.detection.cache.SplitBallCacheManager;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SplitBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.PredictResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
     * 红色球预测算法注册表名称列表
     */
    @Getter
    @Setter
    private String redBatchAlgorithm;

    /**
     * 红色球预测算法实列列表
     */
    @JsonIgnore
    @Getter
    private BatchBallAlgorithm redBatchInstance;


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

    public DoubleColorAlgorithmRegistry(SingleBallAlgorithmRegistry blue, BatchBallAlgorithmRegistry red) {
        this.blueInstance = blue.getInstance();
        this.blueAlgorithm = blue.getAlgorithmName();
        this.redBatchInstance = red.getInstance();
        this.redBatchAlgorithm = red.getAlgorithmName();
    }

    public void initInstance(boolean resetHistoryValue) {
        this.blueInstance = SingleBallAlgorithmFactory.getAlgorithm(this.blueAlgorithm).getInstance();

        if (StringUtils.isNotEmpty(this.redAlgorithm)) {
            this.redInstance = SingleBallAlgorithmFactory.getAlgorithm(this.redAlgorithm).getInstance();
        }
        if (StringUtils.isNotEmpty(this.redBatchAlgorithm)) {
            this.redBatchInstance = BatchBallAlgorithmFactory.getAlgorithm(this.redBatchAlgorithm).getInstance();
        }

        if (resetHistoryValue) {
            this.setPredictResult(new PredictResult());
        }
    }


    @Override
    public DoubleColorBall predict(int targetIndex, PredictOption originOption) {
        var option = originOption.clone();

        // 预测结果
        var predictResult = new DoubleColorBall();
        predictResult.getRedValueList().addAll(predictRedList(targetIndex, option));
        predictResult.getRedValueList().sort(Comparator.comparingInt(o -> o));

        var blueValue = predictBlue(targetIndex, option);
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

    private List<Integer> predictRedList(int targetIndex, PredictOption option){
        if(StringUtils.isNotEmpty(this.redAlgorithm)){
            return predictRedOneByOne(targetIndex, option);
        }

        var splitBatchBall = SplitBallCacheManager.computeIfAbsentBatchBall(targetIndex);
        return this.redBatchInstance.predict(splitBatchBall.getRedBall(), option.toBatchOption()).getResultList();
    }

    private int predictBlue(int targetIndex, PredictOption option) {
        // 获取样本数据
        var splitBall = SplitBallCacheManager.computeIfAbsent(targetIndex);

        return blueInstance.predict(splitBall.getBlueBall(), option).getResult();
    }


    private List<Integer> predictRedOneByOne(int targetIndex, PredictOption option) {
        // 获取样本数据
        var splitBall = SplitBallCacheManager.computeIfAbsent(targetIndex);

        var redValueList = new ArrayList<Integer>(6);
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var singleBall = splitBall.getRedBall(redIndex);

            var nextAllow = option.nextRedAllow(redIndex);
            if (nextAllow != null) {
                option.addBlocks(BallType.RED, redIndex, nextAllow, 33);
            }

            var predictRedResult = redInstance.predict(singleBall, option);
            redValueList.add(predictRedResult.getResult());

            for (int j = 0; j <= redIndex; j++) {
                option.addBlock(BallType.RED, redIndex + 1, redValueList.get(j));
                if (option.hasRedAllow(j)) {
                    option.addBlocks(BallType.RED, redIndex + 1, 1, redValueList.get(j));
                }
            }
        }
        return redValueList;
    }

    @Override
    public List<DoubleColorBall> predictList(int targetIndex, PredictOption option) {
        var list = new ArrayList<DoubleColorBall>();

        var origin = predict(targetIndex, option.clone());
        list.add(origin);


        // [{"blueAlgorithm":"BlueRecommend","redAlgorithm":"RedRecommend","predictResult":{"profit":3342,"sumValue":7260,"sumCost":3918,"maxValue":3000,"hitTotalCount":131,"hitBlueTotalCount":125,"hitRedTotalCount":11}}]


//        PointRemover.execute(list, targetIndex, option.clone(), this);

//        BlockAndAllowMaker.execute(list, targetIndex, option.clone(), this);
//
//        PreMover.execute(list, targetIndex, option.clone(), this);
//
//        SecondBest.execute(list, targetIndex, option.clone(), this);
//        ColdBest.execute(list, targetIndex, option, this);


        // 如果大家推荐的雷同，则补全缺失
//        Maintainer.vote(list, option.clone(), targetIndex, this);

        return list;
    }


    @JsonIgnore
    public String getUniqueName() {
        var red = this.redAlgorithm;
        if (StringUtils.isEmpty(red)) {
            red = this.redBatchAlgorithm;
        }
        return red + "_" + blueAlgorithm;
    }
}
