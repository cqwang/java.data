package cqwang.doubleball.detection.model.option;

import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.result.features.ValueFlag;
import cqwang.doubleball.detection.utils.model.DataScore;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DoublePredictOption {
    /**
     * 黑名单
     */
    private Set<Integer> blueBlocks = new HashSet<>();
    /**
     * 黑名单
     */
    private Set<Integer> redBlocks = new HashSet<>();

    @Getter
    @Setter
    private Integer blueAllow;
    @Getter
    @Setter
    private Integer firstRedAllow;

    public PredictOption toPredictOption(ValueFlag valueFlag) {
        if (valueFlag == ValueFlag.BlUE) {
            return new PredictOption(new HashSet<>(blueBlocks));
        }
        return new PredictOption(new HashSet<>(redBlocks));
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
