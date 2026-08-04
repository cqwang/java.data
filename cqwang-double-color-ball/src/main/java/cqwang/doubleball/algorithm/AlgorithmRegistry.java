package cqwang.doubleball.algorithm;

import lombok.Data;

@Data
public class AlgorithmRegistry {
    private String name;

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

    /**
     * 最近50次命中次数
     */
    private int latest50HitCount = 0;

    /**
     * 最近50次最大金额
     */
    private int latest50MaxAmount = 0;
}
