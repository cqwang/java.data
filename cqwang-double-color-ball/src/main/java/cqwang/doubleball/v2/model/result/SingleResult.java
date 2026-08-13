package cqwang.doubleball.v2.model.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleResult {
    /**
     * 预测结果
     */
    private int result;
    /**
     * 是否预测成功
     */
    private boolean success;

    public SingleResult(int result) {
        this.result = result;
        this.success = true;
    }
}
