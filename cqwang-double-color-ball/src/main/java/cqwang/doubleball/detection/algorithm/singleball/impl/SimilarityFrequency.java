package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.StrategyOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class SimilarityFrequency implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        // 支持向量机模拟算法 - 基于边界优化的预测
        option.setPeriod(40);
        return AlgorithmUtils.similarity(singleBall, range, option);
    }
}