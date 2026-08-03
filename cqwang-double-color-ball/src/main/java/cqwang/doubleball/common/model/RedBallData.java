package cqwang.doubleball.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RedBallData {
    private Map<Integer, BallDataDetail> redBallMap;

    public RedBallData() {
        this.redBallMap = new HashMap<>(6);
        for (int i = 0; i < 6; i++) {
            this.redBallMap.put(i, new BallDataDetail(i));
        }
    }

    public BallDataDetail getRedBallDetail(int index) {
        return redBallMap.get(index);
    }
}
