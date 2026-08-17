package cqwang.doubleball.detection.algorithm.doublecolorball;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.detection.utils.ValueCalculator;
import cqwang.doubleball.detection.algorithm.AlgorithmSelector;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.model.option.DoublePredictOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DoubleColorAlgorithmSelector  implements AlgorithmSelector<DoubleColorAlgorithmRegistry> {
    int MIN_AMOUNT = 0;


    @Override
    public List<DoubleColorAlgorithmRegistry> reCalculateJustForSingle() {
        var selectedAlgorithmList = new ArrayList<DoubleColorAlgorithmRegistry>();

        var singleBallAlgorithmList = SingleBallAlgorithmFactory.getAlgorithmPool();
        for (var algorithm : singleBallAlgorithmList) {
            var advancedAlgorithm = new DoubleColorAlgorithmRegistry(algorithm, algorithm);
            historyPredict(advancedAlgorithm);
            var sumValue = advancedAlgorithm.getPredictResult().getSumValue();
            if (ValueCalculator.hasNoValue(sumValue) || sumValue < 0) {
                continue;
            }
            selectedAlgorithmList.add(advancedAlgorithm);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getPredictResult().getSumValue() - o1.getPredictResult().getSumValue());
        System.out.println(JSON.toJSONString(selectedAlgorithmList));
        return selectedAlgorithmList;
    }

    @Override
    public List<DoubleColorAlgorithmRegistry> reCalculate() {
        var singleBallAlgorithmList = SingleBallAlgorithmFactory.getAlgorithmPool();
        var resultList = new ArrayList<DoubleColorAlgorithmRegistry>();

        for (var blue : singleBallAlgorithmList) {
            for (var red : singleBallAlgorithmList) {
                var advancedAlgorithm = new DoubleColorAlgorithmRegistry(blue, red);
                historyPredict(advancedAlgorithm);
                var predictResult = advancedAlgorithm.getPredictResult();
                if (ValueCalculator.hasNoValue(predictResult.getSumValue()) || predictResult.getSumValue() < MIN_AMOUNT) {
                    continue;
                }

                resultList.add(advancedAlgorithm);
            }
        }
        return resultList;
    }

//    private @NonNull ArrayList<DoubleColorAlgorithmRegistry> getDoubleColorPredictionAlgorithmRegistries(Map<Integer, List<DoubleColorAlgorithmRegistry>> maxValueAlgorithmMap) {
//        for (var entry : maxValueAlgorithmMap.entrySet()) {
//            var list = entry.getValue();
//            list.sort((left, right) -> {
//                if (left.getPredictResult().getHitBlueTotalCount() == right.getPredictResult().getHitBlueTotalCount()) {
//                    var recent = right.getPredictResult().getRecentSumValue(100) - left.getPredictResult().getRecentSumValue(100);
//                    if (recent == 0) {
//                        return right.getPredictResult().getSumValue() - left.getPredictResult().getSumValue();
//                    }
//                    return recent;
//                }
//                return right.getPredictResult().getHitBlueTotalCount() - left.getPredictResult().getHitBlueTotalCount();
//            });
//        }
//
//        // 相同maxValue下，只取
//        var selectedAlgorithmList = new ArrayList<DoubleColorAlgorithmRegistry>();
//        for (var entry : maxValueAlgorithmMap.entrySet()) {
//            var subList = new ArrayList<>(entry.getValue());
//            selectedAlgorithmList.addAll(subList);
//        }
//
//        return selectedAlgorithmList;
//    }

    @Override
    public List<DoubleColorAlgorithmRegistry> readFromFile(boolean resetHistoryValue) {
        var algorithmList = FileProvider.readFile(getFilePath(), new TypeReference<List<DoubleColorAlgorithmRegistry>>() {
        });

        for (var algorithm : algorithmList) {
            algorithm.initInstance(resetHistoryValue);
        }

        return algorithmList;
    }

    @Override
    public void historyPredict(DoubleColorAlgorithmRegistry registry) {
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallPreload.getAllData().size(); targetIndex++) {
            var predict = registry.predict(targetIndex, new DoublePredictOption());
            var target = DoubleColorBallPreload.getAllData().get(targetIndex);
            var value = ValueCalculator.calculate(predict, target);
            registry.getPredictResult().add(targetIndex, value);
        }
    }

    @Override
    public String getFilePath() {
        return "/doubleball/AlgorithmRegistryList.json";
    }
}
