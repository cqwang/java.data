package cqwang.doubleball.detection.algorithm.doublecolorball;

import cqwang.doubleball.detection.model.option.DoublePredictOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import cqwang.doubleball.detection.utils.ValueCalculator;

public class DoubleColorListAlgorithmSelector extends DoubleColorAlgorithmSelector {
    @Override
    public void historyPredict(DoubleColorAlgorithmRegistry registry) {
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallPreload.getAllData().size(); targetIndex++) {
            var predictList = registry.predictList(targetIndex, new DoublePredictOption());
            var target = DoubleColorBallPreload.getAllData().get(targetIndex);
            for (var predict : predictList) {
                var value = ValueCalculator.calculate(predict, target);
                registry.getPredictResult().add(targetIndex, value);
            }
        }
    }
}
