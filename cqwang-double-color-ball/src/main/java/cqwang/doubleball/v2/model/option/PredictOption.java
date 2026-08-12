package cqwang.doubleball.v2.model.option;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PredictOption {
    private PredictOptionDetail blue;
    private Map<Integer, PredictOptionDetail> redMap = new HashMap<>(6);
    private boolean retry;

    public PredictOptionDetail getRed(int index) {
        return redMap.get(index);
    }

    public PredictOption addRed(int index, int data) {
        var detail = redMap.get(index);
        if (detail == null) {
            detail = new PredictOptionDetail();
            redMap.put(index, detail);
        }
        detail.getBlocks().add(data);
        return this;
    }
}
