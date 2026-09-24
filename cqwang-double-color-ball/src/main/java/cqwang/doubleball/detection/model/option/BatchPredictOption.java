package cqwang.doubleball.detection.model.option;

import cqwang.doubleball.detection.model.data.features.BallType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class BatchPredictOption {

    /**
     * 黑名单
     */
    @Getter
    @Setter
    private Set<Integer> redBlocks = new HashSet<>();

    @Getter
    @Setter
    private Set<Integer> redAllows = new HashSet<>();



    public boolean isBlock(int value) {
        return redBlocks.contains(value);
    }

    public boolean isAllow(int value){
        return redAllows.contains(value);
    }

    public void addBlock(int value){
        redBlocks.add(value);
    }

    public void addBlocks(int minValue, int maxValue) {
        for (int i = minValue; i <= maxValue; i++) {
            redBlocks.add(i);
        }
    }

    public void addAllow(int value) {
        redAllows.add(value);
    }

    public BatchPredictOption clone(){
        var result = new BatchPredictOption();
        result.redAllows = new HashSet<>();
        result.redAllows.addAll(this.redAllows);

        result.redBlocks = new HashSet<>();
        result.redBlocks.addAll(this.redBlocks);
       return result;
    }

}
