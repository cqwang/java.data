package cqwang.doubleball.v2.algorithm.doublecolorball;

import cqwang.doubleball.v2.model.data.DoubleColorBall;
import cqwang.doubleball.v2.model.option.PredictOption;
import lombok.NonNull;

public interface DoubleColorPredictionAlgorithm {
    DoubleColorBall predict(int targetIndex, PredictOption predictOption);
}
