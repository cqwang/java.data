package cqwang.doubleball.v2.model.option;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StrategyOption {
    /**
     * 单周期窗口
     */
    int period;
    /**
     * 周期窗口
     */
    int[] periods;
    /**
     * 每个窗口的权重
     */
    int[] weights;

    /**
     * 权重是否累计
     * 如果不累计，短期、中期、长期的权重不同。短期的权重可以更高
     */
    private boolean cumulativeWeight;

    /**
     * 黑名单
     */
    private List<Integer> balckList;

    public StrategyOption(boolean cumulativeWeight) {
        this.cumulativeWeight = cumulativeWeight;
    }

    public StrategyOption(int period){
        this.period = period;
    }
}
