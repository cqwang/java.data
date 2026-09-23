package cqwang.doubleball.detection.algorithm.batchball;

import cqwang.doubleball.detection.model.data.BatchBall;
import cqwang.doubleball.detection.model.option.BatchPredictOption;
import cqwang.doubleball.detection.model.result.BatchResult;

public interface BatchBallAlgorithm {
    BatchResult predict(BatchBall batchBall, BatchPredictOption option);
}
