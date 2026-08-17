package cqwang.doubleball.detection.algorithm;

import cqwang.doubleball.detection.model.value.PredictResult;
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

}
