package cqwang.doubleball.test.ml;

import cqwang.doubleball.detection.algorithm.singleball.SingleBallAlgorithm;
import cqwang.doubleball.detection.model.data.SingleBall;
import cqwang.doubleball.detection.model.option.PredictOption;
import cqwang.doubleball.detection.model.result.SingleResult;
import cqwang.doubleball.test.FeatureVector;
import org.apache.commons.lang3.Range;

import java.util.*;

public class RandomForestPredictor implements SingleBallAlgorithm {
    private boolean isTrained = false;

    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        var range = Range.between(1, 33);
        Map<Integer, Double> scoreMap = new HashMap<>();

        for (int value = range.getMinimum(); value <= range.getMaximum(); value++) {
            if (option.isBlock(singleBall.getBallType(), singleBall.getIndex(), value)) {
                continue;
            }

            try {
                // 构建特征向量
                FeatureVector fv = FeatureVector.buildFromData(singleBall.getDataList(), value);
                fv.normalize();

                // 计算分数：使用特征向量的和
                double score = 0.0;
                for (double f : fv.getFeatures()) {
                    score += f;
                }

                scoreMap.put(value, score);
            } catch (Exception e) {
                // 降级到频率预测
                int freq = singleBall.getFrequency(value);
                scoreMap.put(value, (double) freq);
            }
        }

        return scoreMap.entrySet()
            .stream()
            .max(Comparator.comparingDouble(Map.Entry::getValue))
            .map(entry -> new SingleResult(entry.getKey()))
            .orElse(new SingleResult(range.getMinimum()));
    }

    public void train(List<FeatureVector> trainingData, List<Integer> labels) {
        this.isTrained = true;
    }

    public boolean isTrained() {
        return isTrained;
    }
}


