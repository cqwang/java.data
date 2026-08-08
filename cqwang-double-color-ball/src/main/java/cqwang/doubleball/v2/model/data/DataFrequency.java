package cqwang.doubleball.v2.model.data;

import cqwang.doubleball.v2.model.data.features.FrequencyLevel;
import lombok.Data;

@Data
public class DataFrequency {
    private int data;
    private int frequency;

    private FrequencyLevel frequencyLevel = FrequencyLevel.NONE;

    public DataFrequency(int data) {
        this.data = data;
        this.frequency = 1;
    }
}
