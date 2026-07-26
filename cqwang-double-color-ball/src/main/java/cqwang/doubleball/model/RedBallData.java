package cqwang.doubleball.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RedBallData {
    private Map<Integer, RedBallDetail> redBallMap;

    public RedBallData() {
        this.redBallMap = new HashMap<>(6);
        for (int i = 0; i < 6; i++) {
            this.redBallMap.put(i, new RedBallDetail(i));
        }
    }

    public RedBallDetail getRedBallDetail(int index) {
        return redBallMap.get(index);
    }
}
