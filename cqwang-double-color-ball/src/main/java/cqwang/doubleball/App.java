package cqwang.doubleball;

import cqwang.doubleball.algorithm.impl.ExponentialSmoothing3Algorithm;
import cqwang.doubleball.algorithm.impl.FourierAnalysisAlgorithm;
import cqwang.doubleball.preload.PreloadManager;

/**
 * 双色球预测分析主入口
 */
public class App {
    public static void main(String[] args) {
        PreloadManager.execute();
       var d= new FourierAnalysisAlgorithm().predict(100);
       System.out.println(d);
    }
}

