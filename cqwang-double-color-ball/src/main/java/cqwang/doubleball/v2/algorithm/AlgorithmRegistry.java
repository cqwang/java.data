package cqwang.doubleball.v2.algorithm;

import cqwang.doubleball.v2.model.value.PredictResult;
import lombok.Data;

@Data
public class AlgorithmRegistry {
    /**
     * 预测结果
     */
    private PredictResult predictResult;


    public AlgorithmRegistry(){
        this.predictResult = new PredictResult();
    }

    public void initInstance(){ }

}
