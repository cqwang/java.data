package cqwang.doubleball.detection.algorithm.doublecolorball;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.option.DoublePredictOption;

import java.util.List;

public interface DoubleColorAlgorithm {
    DoubleColorBall predict(int targetIndex, DoublePredictOption option);
    List<DoubleColorBall> predictList(int targetIndex, DoublePredictOption option);
}
