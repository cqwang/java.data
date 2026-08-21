package cqwang.doubleball.detection.algorithm.doublecolorball;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.utils.CompareUtils;
import cqwang.doubleball.detection.utils.ValueCalculator;
import cqwang.doubleball.detection.algorithm.AlgorithmSelector;
import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithmFactory;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DoubleColorAlgorithmSelector implements AlgorithmSelector<DoubleColorAlgorithmRegistry> {
    int MIN_PROFIT = 0;


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

        selectedAlgorithmList.sort(CompareUtils.PREDICT_RESULT_COMPARE);
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
                if (ValueCalculator.hasNoValue(predictResult.getSumValue()) || predictResult.getProfit() < MIN_PROFIT) {
                    continue;
                }

                resultList.add(advancedAlgorithm);
            }
        }
        return resultList;
    }

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
            var predict = registry.predict(targetIndex, new PredictOption());
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
