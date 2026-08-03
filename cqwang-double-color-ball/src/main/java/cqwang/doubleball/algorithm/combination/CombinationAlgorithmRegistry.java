package cqwang.doubleball.algorithm.combination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.algorithm.single.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.single.AlgorithmRegistry;
import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.preload.SampleDataRealtimeLoad;
import lombok.Data;
import org.apache.commons.lang3.Range;

@Data
public class CombinationAlgorithmRegistry {
    /**
     * 注册算法列表
     */
    private String[] redAlgorithmRegistryList;

    @JsonIgnore
    private AlgorithmRegistry[] redAlgorithmRegistryInstanceList;


    private String blueAlgorithmRegistry;

    @JsonIgnore
    private AlgorithmRegistry blueAlgorithmRegistryInstance;


    /**
     * 历史预测价值合计
     */
    private int historyPredictValueSum = 0;

    /**
     * 历史命中次数
     */
    private int historyHitCount = 0;

    /**
     * 最大金额
     */
    private int maxAmount;

    public CombinationAlgorithmRegistry() {
    }

    public void setRedAlgorithmRegistryList(String[] redAlgorithmRegistryList) {
        this.redAlgorithmRegistryList = redAlgorithmRegistryList;
        if (this.redAlgorithmRegistryList == null || this.redAlgorithmRegistryList.length != 6) {
            throw new RuntimeException("invalid red algorithms args");
        }

        this.redAlgorithmRegistryInstanceList = new AlgorithmRegistry[redAlgorithmRegistryList.length];
        for (int i = 0; i < redAlgorithmRegistryList.length; i++) {
            this.redAlgorithmRegistryInstanceList[i] = AlgorithmPoolFactory.getAlgorithm(redAlgorithmRegistryList[i]);
        }
    }


    public void setBlueAlgorithmRegistry(String blueAlgorithmRegistry) {
        this.blueAlgorithmRegistry = blueAlgorithmRegistry;
        this.blueAlgorithmRegistryInstance = AlgorithmPoolFactory.getAlgorithm(this.blueAlgorithmRegistry);
    }


    public CombinationAlgorithmRegistry(AlgorithmRegistry blueAlgorithm, AlgorithmRegistry... redAlgorithms) {
        this.blueAlgorithmRegistryInstance = blueAlgorithm;
        this.redAlgorithmRegistryInstanceList = redAlgorithms;
    }


    public DoubleColorBallItem predict(int targetIndex) {
        // 获取样本数据
        var sampleDataRealtimeLoad = new SampleDataRealtimeLoad();
        sampleDataRealtimeLoad.execute(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBallItem(true);
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var redAlgorithm = getRedAlgorithmRegistryInstanceList()[redIndex].getInstance();

            var redBallDetail = sampleDataRealtimeLoad.getRedBallData().getRedBallDetail(redIndex);
            var redRange = redAlgorithm.getRedRange(predictResult, redBallDetail);
            var predictRed = redAlgorithm.predictRed(redBallDetail, redRange);
            predictResult.getRedValueList().add(predictRed);
        }

        var blueAlgorithm = getBlueAlgorithmRegistryInstance().getInstance();
        var blueValue = blueAlgorithm.predictBlue(sampleDataRealtimeLoad.getBlueBallDetail(), Range.between(1, 16));
        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

}
