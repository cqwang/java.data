package cqwang.doubleball.v2.model.option;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PredictOption {
    private PredictOptionDetail blue;
    private Map<Integer, PredictOptionDetail> redMap = new HashMap<>(6);

    public PredictOptionDetail getRed(int index) {
        return redMap.get(index);
    }
}
