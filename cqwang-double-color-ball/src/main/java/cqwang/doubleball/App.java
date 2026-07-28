package cqwang.doubleball;

import cqwang.doubleball.algorithm.select.AlgorithmSelector;
import cqwang.doubleball.preload.PreloadManager;

/**
 * 双色球预测分析主入口
 */
public class App {
    public static void main(String[] args) {
        PreloadManager.execute();
       var algorithmList = AlgorithmSelector.execute();
       System.out.println();
    }
}

