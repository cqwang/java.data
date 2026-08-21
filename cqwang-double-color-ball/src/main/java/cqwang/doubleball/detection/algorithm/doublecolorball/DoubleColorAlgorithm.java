package cqwang.doubleball.detection.algorithm.doublecolorball;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.option.PredictOption;

import java.util.List;

public interface DoubleColorAlgorithm {
    DoubleColorBall predict(int targetIndex, PredictOption option);
    List<DoubleColorBall> predictList(int targetIndex, PredictOption option);
}
