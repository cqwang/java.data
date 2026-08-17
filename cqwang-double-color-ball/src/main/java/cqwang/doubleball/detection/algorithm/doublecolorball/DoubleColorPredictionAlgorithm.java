package cqwang.doubleball.detection.algorithm.doublecolorball;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.option.PredictOption;

public interface DoubleColorPredictionAlgorithm {
    DoubleColorBall predict(int targetIndex, PredictOption predictOption);
}
