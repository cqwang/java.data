package cqwang.doubleball;

import cqwang.doubleball.algorithm.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import cqwang.doubleball.preload.PreloadManager;
import org.apache.commons.lang3.Range;

/**
 * 双色球预测分析主入口
 */
public class App {
    public static void main(String[] args) {
        PreloadManager.execute();
//        new PredictionAlgorithm() {
//            @Override
//            public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
//                return 0;
//            }
//
//            @Override
//            public int predictBlue(BallDataDetail redBallDataDetail) {
//                return 0;
//            }
//        }.predict(100,100);
    }
}

