package cqwang.doubleball.detection.algorithm.doublecolorball;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.option.DoublePredictOption;

public interface DoubleColorAlgorithm {
    DoubleColorBall predict(int targetIndex, DoublePredictOption option);
}
