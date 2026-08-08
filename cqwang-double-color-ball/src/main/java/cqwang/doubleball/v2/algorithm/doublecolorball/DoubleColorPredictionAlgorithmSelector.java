package cqwang.doubleball.v2.algorithm.doublecolorball;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.doubleball.v2.algorithm.AlgorithmSelector;
import cqwang.doubleball.v2.algorithm.singleball.SingleBallAlgorithmRegistryFactory;
import cqwang.doubleball.v2.preload.DoubleColorBallPreload;
import cqwang.doubleball.v2.utils.ValueCalculator;

import java.util.ArrayList;
import java.util.List;

public class DoubleColorPredictionAlgorithmSelector implements AlgorithmSelector<DoubleColorPredictionAlgorithmRegistry> {

    int ADVANCED_MIX_AMOUNT = 0;

    @Override
    public List<DoubleColorPredictionAlgorithmRegistry> reCalculate() {
        var selectedAlgorithmList = new ArrayList<DoubleColorPredictionAlgorithmRegistry>();

        var singleBallAlgorithmList = SingleBallAlgorithmRegistryFactory.getAlgorithmPool();
        for (var red0 : singleBallAlgorithmList) {
            for (var red1 : singleBallAlgorithmList) {
                for (var red2 : singleBallAlgorithmList) {
                    for (var red3 : singleBallAlgorithmList) {
                        for (var red4 : singleBallAlgorithmList) {
                            for (var red5 : singleBallAlgorithmList) {
                                for (var blue : singleBallAlgorithmList) {
                                    var advancedAlgorithm = new DoubleColorPredictionAlgorithmRegistry(blue, red0, red1, red2, red3, red4, red5, blue);
                                    historyPredict(advancedAlgorithm);
                                    var sumValue = advancedAlgorithm.getPredictResult().getSumValue();
                                    if (ValueCalculator.hasNoValue(sumValue) || sumValue < ADVANCED_MIX_AMOUNT) {
                                        continue;
                                    }
                                    selectedAlgorithmList.add(advancedAlgorithm);
                                }
                            }
                        }
                    }
                }
            }
        }       return selectedAlgorithmList;
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
    public List<DoubleColorPredictionAlgorithmRegistry> readFromFile() {
        var algorithmList = FileProvider.readFile(getFilePath(), new TypeReference<List<DoubleColorPredictionAlgorithmRegistry>>() {
        });

        for (var algorithm : algorithmList) {
            algorithm.initInstance();
        }
        return algorithmList;
    }


    @Override
    public String getFilePath() {
        return "/v2/DoubleColorPredictionAlgorithmRegistryList.json";
    }
}
