package cqwang.doubleball.detection.model.option;

import cqwang.doubleball.detection.model.data.features.BallType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class PredictOption {
    private Map<Integer, Set<Integer>> redBlocks = new HashMap<>();

    /**
     * 黑名单
     */
    private Set<Integer> blueBlocks = new HashSet<>();


    @Getter
    @Setter
    private Integer blueAllow;
    @Getter
    @Setter
    private Map<Integer, Integer> redAllows = new HashMap<>();


    public void addBlock(BallType ballType, int index, int value){
        if(ballType == BallType.BLUE){
            blueBlocks.add(value);
        } else if(ballType ==BallType.RED){
            var set = redBlocks.computeIfAbsent(index, k -> new HashSet<>());
            set.add(value);
        }
    }

    public boolean isBlock(BallType ballType, int index ,int value) {
        if (ballType == BallType.BLUE) {
            return blueBlocks.contains(value);
        } else if (ballType == BallType.RED) {
            var set = redBlocks.get(index);
            if (set == null) {
                return false;
            }
            return set.contains(value);
        }
        return false;
    }

    public void removeBlock(BallType ballType, int index ,int value) {
        if (ballType == BallType.BLUE) {
            blueBlocks.remove(value);
        } else if (ballType == BallType.RED) {
            var set = redBlocks.get(index);
            if (set == null) {
                return;
            }
            set.remove(value);
        }
    }

    public void setAllow(BallType ballType, int index ,int value) {
        if (ballType == BallType.BLUE) {
            blueAllow = value;
        } else if (ballType == BallType.RED) {
            redAllows.put(index, value);
        }
    }

    public boolean isAllow(BallType ballType, int index ,int value) {
        if (ballType == BallType.BLUE) {
            return blueAllow != null && blueAllow == value;
        } else if (ballType == BallType.RED) {
            var result = redAllows.get(index);
            return result != null && result == value;
        }
        return false;
    }


    public void clearAllow(BallType ballType, int index) {
        if (ballType == BallType.BLUE) {
            blueAllow = null;
        } else if (ballType == BallType.RED) {
            redAllows.remove(index);
        }
    }

    public PredictOption clone(){
        var result = new PredictOption();
        for(var entry : this.redBlocks.entrySet()){
            var key = entry.getKey();
            var values = new HashSet<>(entry.getValue());
            result.redBlocks.put(key, values);
        }
        result.blueBlocks.addAll(this.blueBlocks);
        result.blueAllow = this.blueAllow;
        result.redAllows.putAll(this.redAllows);
        return result;
    }

    public PredictOption cloneAndAddBlock(BallType ballType, int index, int value) {
        var result = clone();
        result.addBlock(ballType, index, value);
        return result;
    }

    public PredictOption cloneAndSetAllow(BallType ballType, int index, int value) {
        var result = clone();
        result.setAllow(ballType, index, value);
        return result;
    }

}
