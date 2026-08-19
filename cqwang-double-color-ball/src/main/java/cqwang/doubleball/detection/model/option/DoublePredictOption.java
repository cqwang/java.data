package cqwang.doubleball.detection.model.option;

import cqwang.doubleball.detection.model.result.features.ValueFlag;

import java.util.HashSet;
import java.util.Set;

public class DoublePredictOption {
    /**
     * 黑名单
     */
    private Set<Integer> blueBlocks = new HashSet<>();
    /**
     * 黑名单
     */
    private Set<Integer> redBlocks = new HashSet<>();

    public PredictOption toPredictOption(ValueFlag valueFlag) {
        if (valueFlag == ValueFlag.BlUE) {
            return new PredictOption(blueBlocks);
        }
        return new PredictOption(redBlocks);
    }

    public DoublePredictOption addRedBlock(int data) {
        this.redBlocks.add(data);
        return this;
    }

    public DoublePredictOption removeRedBlock(int data) {
        this.redBlocks.remove(data);
        return this;
    }

    public DoublePredictOption addBlueBlock(int data) {
        this.blueBlocks.add(data);
        return this;
    }

    public DoublePredictOption removeBlueBlock(int data) {
        this.blueBlocks.remove(data);
        return this;
    }
}
