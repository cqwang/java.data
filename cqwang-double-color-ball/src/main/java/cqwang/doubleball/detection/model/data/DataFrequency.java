package cqwang.doubleball.detection.model.data;

import lombok.Data;

@Data
public class DataFrequency {
    private int data;
    private int frequency;
    private int maxContinuousFrequency;

    public DataFrequency(int data) {
        this.data = data;
        this.frequency = 1;
        this.maxContinuousFrequency = 1;
    }
}
