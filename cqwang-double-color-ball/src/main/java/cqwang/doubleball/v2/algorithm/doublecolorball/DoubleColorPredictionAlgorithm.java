package cqwang.doubleball.v2.algorithm.doublecolorball;

import cqwang.doubleball.v2.model.data.DoubleColorBall;

public interface DoubleColorPredictionAlgorithm {
    DoubleColorBall predict(int targetIndex);
}
