package cqwang.doubleball.detection.model.data;

import cqwang.doubleball.detection.cache.preload.DoubleColorBallPreload;
import lombok.Getter;

public class SplitBatchBall {
    @Getter
    private BatchBall blueBall;
    @Getter
    private BatchBall redBall;

    public SplitBatchBall() {
        blueBall = new BatchBall();
        redBall = new BatchBall();
    }

    public SplitBatchBall(int preSampleSize) {
        this(0, preSampleSize);
    }

    public SplitBatchBall(int startIndex, int preSampleSize) {
        this();

        int count = 0;
        for (int i = startIndex; i < DoubleColorBallPreload.getAllData().size(); i++) {
            var item = DoubleColorBallPreload.getAllData().get(i);

            for (var red : item.getRedValueList()) {
                redBall.addData(red);
            }
            blueBall.addData(item.getBlueValue());

            count++;
            if (count >= preSampleSize) {
                break;
            }
        }

        //
        redBall.completeFill();
        blueBall.completeFill();
    }
}
