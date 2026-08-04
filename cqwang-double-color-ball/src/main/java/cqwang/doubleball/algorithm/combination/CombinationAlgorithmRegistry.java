package cqwang.doubleball.algorithm.combination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cqwang.doubleball.algorithm.AlgorithmPoolFactory;
import cqwang.doubleball.algorithm.AlgorithmRegistry;
import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithm;
import cqwang.doubleball.algorithm.relevance.RelevanceAlgorithmRegistry;
import cqwang.doubleball.algorithm.single.SingleAlgorithmRegistry;
import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.preload.SampleDataRealtimeLoad;
import lombok.Data;
import org.apache.commons.lang3.Range;

@Data
public class CombinationAlgorithmRegistry extends AlgorithmRegistry implements PredictionAlgorithm {
    /**
     * 注册算法列表
     */
    private String[] redAlgorithmRegistryList;

    @JsonIgnore
    private SingleAlgorithmRegistry[] redSingleAlgorithmRegistryInstanceList;


    private String blueAlgorithmRegistry;

    @JsonIgnore
    private SingleAlgorithmRegistry blueSingleAlgorithmRegistryInstance;

    private String blueRelevanceAlgorithmRegistry;

    @JsonIgnore
    private RelevanceAlgorithm blueRelevanceAlgorithmRegistryInstance;


    public CombinationAlgorithmRegistry() {
    }

    public CombinationAlgorithmRegistry(SingleAlgorithmRegistry blueAlgorithm, SingleAlgorithmRegistry... redAlgorithms) {
        this.blueSingleAlgorithmRegistryInstance = blueAlgorithm;
        this.redSingleAlgorithmRegistryInstanceList = redAlgorithms;
    }

    public CombinationAlgorithmRegistry(CombinationAlgorithmRegistry source, RelevanceAlgorithmRegistry relevanceAlgorithmRegistry){
        this.redAlgorithmRegistryList = source.getRedAlgorithmRegistryList();
        this.redSingleAlgorithmRegistryInstanceList = source.getRedSingleAlgorithmRegistryInstanceList();
        this.blueAlgorithmRegistry = source.getBlueAlgorithmRegistry();
        this.blueSingleAlgorithmRegistryInstance = source.getBlueSingleAlgorithmRegistryInstance();
        this.blueRelevanceAlgorithmRegistry = relevanceAlgorithmRegistry.getName();
        this.blueRelevanceAlgorithmRegistryInstance = relevanceAlgorithmRegistry.getInstance();
    }


    public void setRedAlgorithmRegistryList(String[] redAlgorithmRegistryList) {
        this.redAlgorithmRegistryList = redAlgorithmRegistryList;
        if (this.redAlgorithmRegistryList == null || this.redAlgorithmRegistryList.length != 6) {
            throw new RuntimeException("invalid red algorithms args");
        }

        this.redSingleAlgorithmRegistryInstanceList = new SingleAlgorithmRegistry[redAlgorithmRegistryList.length];
        for (int i = 0; i < redAlgorithmRegistryList.length; i++) {
            this.redSingleAlgorithmRegistryInstanceList[i] = AlgorithmPoolFactory.getSingleAlgorithm(redAlgorithmRegistryList[i]);
        }
    }


    public void setBlueAlgorithmRegistry(String blueAlgorithmRegistry) {
        this.blueAlgorithmRegistry = blueAlgorithmRegistry;
        this.blueSingleAlgorithmRegistryInstance = AlgorithmPoolFactory.getSingleAlgorithm(this.blueAlgorithmRegistry);
    }


    @Override
    public DoubleColorBallItem predict(int targetIndex) {
        // 获取样本数据
        var sampleDataRealtimeLoad = new SampleDataRealtimeLoad();
        sampleDataRealtimeLoad.execute(targetIndex);

        // 预测结果
        var predictResult = new DoubleColorBallItem(true);
        // 红色
        for (int redIndex = 0; redIndex < 6; redIndex++) {
            var redAlgorithm = getRedSingleAlgorithmRegistryInstanceList()[redIndex].getInstance();

            var redBallDetail = sampleDataRealtimeLoad.getRedBallData().getRedBallDetail(redIndex);
            var redRange = redAlgorithm.getRedRange(predictResult, redBallDetail);
            var predictRed = redAlgorithm.predictRed(redBallDetail, redRange);
            predictResult.getRedValueList().add(predictRed);
        }

        var blueValue = 0;
        if (blueRelevanceAlgorithmRegistryInstance != null) {
            blueValue = blueRelevanceAlgorithmRegistryInstance.predictBlue(targetIndex, predictResult.getRedValueList());
        } else {
            var blueAlgorithm = getBlueSingleAlgorithmRegistryInstance().getInstance();
            blueValue = blueAlgorithm.predictBlue(sampleDataRealtimeLoad.getBlueBallDetail(), Range.between(1, 16));
        }

        predictResult.setBlueValue(blueValue);
        return predictResult;
    }

}
