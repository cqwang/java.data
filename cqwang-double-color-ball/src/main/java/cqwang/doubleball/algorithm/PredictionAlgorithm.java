package cqwang.doubleball.algorithm;

import cqwang.doubleball.common.model.DoubleColorBallItem;

public interface PredictionAlgorithm {
    DoubleColorBallItem predict(int targetIndex);
}
