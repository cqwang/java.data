package cqwang.doubleball.detection.model.option;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class PredictOption {
    /**
     * 黑名单
     */
    private Set<Integer> blocks = new HashSet<>();

    private Set<Integer> allows = new HashSet<>();

    public boolean isBlocked(int data) {
        return blocks.contains(data);
    }

    public boolean isAllowed(int data) {
        return allows.contains(data);
    }

    public void addBlock(int data) {
        blocks.add(data);
    }
}
