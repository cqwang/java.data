package cqwang.doubleball.detection.model.data;

import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
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

    /**
     * 索引-数据-位序
     */
    private Map<Integer, Map<Integer, List<Integer>>> redIndexMap;

    /**
     * 数据-位序
     */
    private Map<Integer, List<Integer>> blueIndexMap;


    public SplitBall() {
        this.redBallMap = new HashMap<>(6);
        for (int i = 0; i < 6; i++) {
            this.redBallMap.put(i, new SingleBall(i, BallType.RED));
        }

        blueBall = new SingleBall(0, BallType.BLUE);

        this.redIndexMap = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            this.redIndexMap.put(i, new HashMap<>());
        }

        this.blueIndexMap = new HashMap<>();
    }

    public SplitBall(int preSampleSize) {
        this(0, preSampleSize);
    }

    public SplitBall(int startIndex, int preSampleSize) {
        this();

        int count = 0;
        for (int i = startIndex; i < DoubleColorBallPreload.getAllData().size(); i++) {
            var item = DoubleColorBallPreload.getAllData().get(i);
            for (int j = 0; j < item.getRedValueList().size(); j++) {
                var redValue = item.getRedValueList().get(j);
                redBallMap.get(j).addData(redValue);
                var list = redIndexMap.get(j).computeIfAbsent(redValue, k -> new ArrayList<>());
                list.add(i);

            }
            blueBall.addData(item.getBlueValue());
            var list = blueIndexMap.computeIfAbsent(item.getBlueValue(), k -> new ArrayList<>());
            list.add(i);

            count++;
            if (count >= preSampleSize) {
                break;
            }
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


    public List<Integer> getIndexList(BallType ballType, int index, int data) {
        if (ballType == BallType.BLUE) {
            return blueIndexMap.get(data);
        }

        return redIndexMap.get(index).get(data);
    }
}
