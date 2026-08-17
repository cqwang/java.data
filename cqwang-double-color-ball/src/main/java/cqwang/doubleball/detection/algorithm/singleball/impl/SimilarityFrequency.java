package cqwang.doubleball.detection.algorithm.singleball.impl;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.detection.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class SimilarityFrequency implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var range = Range.between(singleBall.getMinData(), singleBall.getMaxData());
        return AlgorithmUtils.similarity(singleBall, range, option);
    }
}