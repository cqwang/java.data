package cqwang.doubleball.v2.model.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StrategyOption {
    /**
     *
     */
    private PredictOptionDetail predictOption;


    /**
     * 权重是否累计
     * 如果不累计，短期、中期、长期的权重不同。短期的权重可以更高
     */
    private boolean cumulativeWeight;

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

    public StrategyOption(PredictOptionDetail predictOption) {
        this.predictOption = predictOption;
    }


    public boolean isBlocked(int data) {
        if (predictOption == null || predictOption.getBlocks() == null) {
            return false;
        }
        return predictOption.getBlocks().contains(data);
    }
}
