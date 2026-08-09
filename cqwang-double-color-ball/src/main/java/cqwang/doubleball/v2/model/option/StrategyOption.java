package cqwang.doubleball.v2.model.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StrategyOption {
    /**
     * 周期窗口
     */
    @JsonIgnore
    int[] periods;
    /**
     * 每个窗口的权重
     */
    @JsonIgnore
    int[] weights;



    /**
     * 权重是否累计
     * 如果不累计，短期、中期、长期的权重不同。短期的权重可以更高
     */
    private boolean cumulativeWeight;

    /**
     * 是否最近窗口取样
     */
    private boolean recent;


    /**
     * 黑名单
     */
    private List<Integer> balckList;

    public StrategyOption cumulativeWeight(boolean cumulativeWeight) {
        this.cumulativeWeight = cumulativeWeight;
        return this;
    }

    public StrategyOption recent(boolean recent){
        this.recent = recent;
        return this;
    }
}
