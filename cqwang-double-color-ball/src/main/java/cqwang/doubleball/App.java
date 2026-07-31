package cqwang.doubleball;

import cqwang.doubleball.algorithm.FuturePredict;
import cqwang.doubleball.algorithm.detection.impl.CosineSimilarityAlgorithm;
import cqwang.doubleball.algorithm.selectedmixed.MixedAlgorithm;

/**
 * 双色球预测分析主入口
 */
public class App {
    public static void main(String[] args) {

        FuturePredict.predictMix();
    }
}

