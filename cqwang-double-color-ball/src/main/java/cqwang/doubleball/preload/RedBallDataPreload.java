package cqwang.doubleball.preload;

import cqwang.doubleball.model.BallDataDetail;
import cqwang.doubleball.model.RedBallData;

/**
 * 全局静态，用于实时预测
 */
public class RedBallDataPreload {
    private static RedBallData redBallData;
    private static BallDataDetail blueBallDetail;

    public static void execute() {
        var redBallDataRealtimeLoad = new RedBallDataRealtimeLoad();
        redBallDataRealtimeLoad.execute(DoubleColorBallDataPreload.allData().size());
        redBallData = redBallDataRealtimeLoad.getRedBallData();
        blueBallDetail = redBallDataRealtimeLoad.getBlueBallDetail();
    }

    public static RedBallData redBallData() {
        return redBallData;
    }

    public static BallDataDetail blueBallDetail() {
        return blueBallDetail;
    }
}
