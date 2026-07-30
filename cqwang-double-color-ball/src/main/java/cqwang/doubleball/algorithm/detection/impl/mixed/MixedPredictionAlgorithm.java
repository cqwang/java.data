package cqwang.doubleball.algorithm.detection.impl.mixed;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;

import java.util.List;

/**
 * 混合多种算法
 */
public interface MixedPredictionAlgorithm extends PredictionAlgorithm {

    List<PredictionAlgorithm> getAlgorithmList();

    int predictRedMix(List<Integer> predictList);

    int predictBlueMix(List<Integer> predictList);
}
