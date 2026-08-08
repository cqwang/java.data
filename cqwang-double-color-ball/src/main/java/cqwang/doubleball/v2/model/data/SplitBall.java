package cqwang.doubleball.v2.model.data;

import cqwang.doubleball.v2.preload.DoubleColorBallPreload;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每个位序的球，单独拆分
 */
public class SplitBall {
    private Map<Integer, SingleBall> redBallMap;

    @Getter
    private SingleBall blueBall;


    public SplitBall() {
        this.redBallMap = new HashMap<>(6);
        for (int i = 0; i < 6; i++) {
            this.redBallMap.put(i, new SingleBall(i));
        }

        blueBall = new SingleBall(0);
    }

    public SplitBall(int preSampleSize) {
        this(0, preSampleSize);
    }

    public SplitBall(int startIndex, int preSampleSize) {
        this();
        List<DoubleColorBall> preSampleData = DoubleColorBallPreload.getAllData().subList(startIndex, preSampleSize);
        if (CollectionUtils.isEmpty(preSampleData)) {
            return;
        }

        for (var item : preSampleData) {
            for (int j = 0; j < item.getRedValueList().size(); j++) {
                var redValue = item.getRedValueList().get(j);
                redBallMap.get(j).addData(redValue);
            }
            blueBall.addData(item.getBlueValue());
        }

        //
        for (var redBall : redBallMap.values()) {
            redBall.completeFill();
        }
        blueBall.completeFill();
    }

    public SingleBall getRedBall(int index) {
        return redBallMap.get(index);
    }
}
