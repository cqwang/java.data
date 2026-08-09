package cqwang.doubleball.v2.algorithm.doublecolorball;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cqwang.doubleball.v2.algorithm.AlgorithmRegistry;
import cqwang.doubleball.v2.algorithm.singleball.SingleBallAlgorithmRegistryFactory;
import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithmRegistry;
import cqwang.doubleball.v2.model.data.DoubleColorBall;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.data.SplitBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.model.value.PredictResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Range;

import java.util.Arrays;

/**
 * 双色球预测算法注册表
 */
@NoArgsConstructor
@JsonPropertyOrder({"blueAlgorithm", "redAlgorithmList", "predictResult"})
public class DoubleColorPredictionAlgorithmRegistry extends AlgorithmRegistry implements DoubleColorPredictionAlgorithm {

    /**
     * 红色球预测算法注册表名称列表
     */
    @Getter
    @Setter
    private String[] redAlgorithmList;

    /**
     * 红色球预测算法实列列表
     */
    @JsonIgnore
    @Getter
    private SingleBallPredictAlgorithmRegistry[] redInstanceList;


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
    private SingleBallPredictAlgorithmRegistry blueInstance;

    @Getter
    @Setter
    private StrategyOption option;

    public DoubleColorPredictionAlgorithmRegistry(StrategyOption option, SingleBallPredictAlgorithmRegistry blue, SingleBallPredictAlgorithmRegistry... reds) {
        this.option = option;
        this.blueInstance = blue;
        this.blueAlgorithm = blue.getAlgorithmName();
        this.redAlgorithmList = new String[6];

        if (reds.length == 1) {
            this.redInstanceList = new SingleBallPredictAlgorithmRegistry[6];
            Arrays.fill(this.redInstanceList, reds[0]);
            Arrays.fill(this.redAlgorithmList, reds[0].getAlgorithmName());
        } else if (reds.length == 6) {
            this.redInstanceList = reds;
            for (int i = 0; i < reds.length; i++) {
                this.redAlgorithmList[i] = reds[i].getAlgorithmName();
            }
        } else {
            throw new RuntimeException(" error config");
        }
    }

    public void initInstance(boolean resetHistoryValue) {
        if (resetHistoryValue) {
            this.setPredictResult(new PredictResult());
        }
        this.blueInstance = SingleBallAlgorithmRegistryFactory.getAlgorithm(this.blueAlgorithm);
        this.redInstanceList = new SingleBallPredictAlgorithmRegistry[6];

        if (this.redAlgorithmList == null || this.redAlgorithmList.length == 0 || this.redAlgorithmList.length != 1 && this.redAlgorithmList.length != 6) {
            throw new RuntimeException(" error config");
        }

        for (int index = 0; index < this.redInstanceList.length; index++) {
            var actualIndex = this.redAlgorithmList.length > index ? index : 0;
            var actualName = this.redAlgorithmList[actualIndex];
            var instance = SingleBallAlgorithmRegistryFactory.getAlgorithm(actualName);
            this.redInstanceList[index] = instance;
        }
    }


    @Override
    public DoubleColorBall predict(int targetIndex) {
        // 获取样本数据
        var splitBall = new SplitBall(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBall();
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var redAlgorithm = redInstanceList[redIndex].getInstance();

            var singleBall = splitBall.getRedBall(redIndex);
            var redRange = getRedRange(predictResult, singleBall);
            var predictRed = redAlgorithm.predict(singleBall, redRange, option);
            predictResult.getRedValueList().add(predictRed);
        }

        var blueValue = this.blueInstance.getInstance().predict(splitBall.getBlueBall(), Range.between(1, 16), option);
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

    /**
     * 获取要预测位序红球的范围
     *
     * @param result
     * @param
     * @return
     */
    public Range<Integer> getRedRange(DoubleColorBall result, SingleBall singleBall) {
        if (CollectionUtils.isEmpty(result.getRedValueList())) {
            return Range.between(singleBall.getMinData(), singleBall.getMaxData());
        }

        var lastRed = result.getRedValueList().get(result.getRedValueList().size() - 1);
        if (lastRed <= singleBall.getMinData()) {
            return Range.between(singleBall.getMinData(), singleBall.getMaxData());
        }

        return Range.between(lastRed + 1, singleBall.getMaxData());
    }
}
