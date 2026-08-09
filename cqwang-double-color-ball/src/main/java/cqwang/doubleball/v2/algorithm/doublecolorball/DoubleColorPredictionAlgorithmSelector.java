package cqwang.doubleball.v2.algorithm.doublecolorball;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.v2.algorithm.AlgorithmSelector;
import cqwang.doubleball.v2.algorithm.singleball.SingleBallAlgorithmRegistryFactory;
import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithmRegistry;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.preload.DoubleColorBallPreload;
import cqwang.doubleball.v2.utils.ValueCalculator;

import java.util.*;

public class DoubleColorPredictionAlgorithmSelector implements AlgorithmSelector<DoubleColorPredictionAlgorithmRegistry> {

    int MIN_AMOUNT = 6000;

    @Override
    public List<DoubleColorPredictionAlgorithmRegistry> reCalculateJustForBlue() {
        var selectedAlgorithmList = new ArrayList<DoubleColorPredictionAlgorithmRegistry>();

        var singleBallAlgorithmList = SingleBallAlgorithmRegistryFactory.getAlgorithmPool();
        var optionList = getStrategyOptionList();
        for (var blue : singleBallAlgorithmList) {
            for (var option : optionList) {
                var advancedAlgorithm = new DoubleColorPredictionAlgorithmRegistry(option, blue, blue);
                historyPredict(advancedAlgorithm);
                var sumValue = advancedAlgorithm.getPredictResult().getSumValue();
                if (ValueCalculator.hasNoValue(sumValue) || sumValue < MIN_AMOUNT) {
                    continue;
                }
                selectedAlgorithmList.add(advancedAlgorithm);
            }
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getPredictResult().getHitBlueTotalCount() - o1.getPredictResult().getHitBlueTotalCount());
        System.out.println(JSON.toJSONString(selectedAlgorithmList));
        return selectedAlgorithmList;
    }

    @Override
    public List<DoubleColorPredictionAlgorithmRegistry> reCalculate() {
        Map<Integer, List<DoubleColorPredictionAlgorithmRegistry>> maxValueAlgorithmMap = new HashMap<>();

        var singleBallAlgorithmList = SingleBallAlgorithmRegistryFactory.getAlgorithmPool();
        var optionList = getStrategyOptionList();
        for (var red0 : singleBallAlgorithmList) {
            for (var red1 : singleBallAlgorithmList) {
                for (var red2 : singleBallAlgorithmList) {
                    for (var red3 : singleBallAlgorithmList) {
                        for (var red4 : singleBallAlgorithmList) {
                            for (var red5 : singleBallAlgorithmList) {
                                for (var blue : singleBallAlgorithmList) {
                                    for (var option : optionList) {
                                        var advancedAlgorithm = new DoubleColorPredictionAlgorithmRegistry(option, blue, red0, red1, red2, red3, red4, red5);
                                        historyPredict(advancedAlgorithm);
                                        var predictResult = advancedAlgorithm.getPredictResult();
                                        if (ValueCalculator.hasNoValue(predictResult.getSumValue()) || predictResult.getSumValue() < MIN_AMOUNT) {
                                            continue;
                                        }

                                        var list = maxValueAlgorithmMap.computeIfAbsent(predictResult.getMaxValue(), k -> new ArrayList<>());
                                        list.add(advancedAlgorithm);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        for (var entry : maxValueAlgorithmMap.entrySet()) {
            var list = entry.getValue();
            list.sort((left, right) -> {
                if (left.getPredictResult().getHitBlueTotalCount() == right.getPredictResult().getHitBlueTotalCount()) {
                    var recent = right.getPredictResult().getRecentSumValue(100) - left.getPredictResult().getRecentSumValue(100);
                    if (recent == 0) {
                        return right.getPredictResult().getSumValue() - left.getPredictResult().getSumValue();
                    }
                    return recent;
                }
                return right.getPredictResult().getHitBlueTotalCount() - left.getPredictResult().getHitBlueTotalCount();
            });
        }

        var selectedAlgorithmList = new ArrayList<DoubleColorPredictionAlgorithmRegistry>();
        for (var entry : maxValueAlgorithmMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                var algorithm = entry.getValue().get(i);
                var last = selectedAlgorithmList.stream().filter(t -> t.equalsBlueAlgorithm(algorithm)).findFirst().orElse(null);
                if (last != null) {
                    continue;
                }

                selectedAlgorithmList.add(algorithm);

            }
        }

        return selectedAlgorithmList;
    }

    private List<StrategyOption> getStrategyOptionList() {
        var list = new ArrayList<StrategyOption>();
        list.add(new StrategyOption().cumulativeWeight(true));
        list.add(new StrategyOption().recent(true, false));
        list.add(new StrategyOption().recent(true, true));
        return list;
    }

    @Override
    public void historyPredict(DoubleColorPredictionAlgorithmRegistry registry) {
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallPreload.getAllData().size(); targetIndex++) {
            var predict = registry.predict(targetIndex);
            var target = DoubleColorBallPreload.getAllData().get(targetIndex);
            var value = ValueCalculator.calculate(predict, target);
            registry.getPredictResult().add(targetIndex, value);
        }
    }

    @Override
    public List<DoubleColorPredictionAlgorithmRegistry> readFromFile(boolean resetHistoryValue) {
        var algorithmList = FileProvider.readFile(getFilePath(), new TypeReference<List<DoubleColorPredictionAlgorithmRegistry>>() {
        });

        for (var algorithm : algorithmList) {
            algorithm.initInstance(resetHistoryValue);
        }

        return algorithmList;
    }


    @Override
    public String getFilePath() {
        return "/v2/DoubleColorPredictionAlgorithmRegistryList.json";
    }
}
