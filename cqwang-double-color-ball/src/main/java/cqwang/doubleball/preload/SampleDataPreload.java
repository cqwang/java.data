package cqwang.doubleball.preload;

import cqwang.doubleball.common.model.BallDataDetail;
import cqwang.doubleball.common.model.RedBallData;

/**
 * 全局静态，用于实时预测
 */
public class SampleDataPreload {
    private static RedBallData redBallData;
    private static BallDataDetail blueBallDetail;

    public static void execute() {
        var sampleDataRealtimeLoad = new SampleDataRealtimeLoad();
        sampleDataRealtimeLoad.execute(DoubleColorBallDataPreload.allData().size());
        redBallData = sampleDataRealtimeLoad.getRedBallData();
        blueBallDetail = sampleDataRealtimeLoad.getBlueBallDetail();
    }

    public static RedBallData redBallData() {
        return redBallData;
    }

    public static BallDataDetail blueBallDetail() {
        return blueBallDetail;
    }
}
