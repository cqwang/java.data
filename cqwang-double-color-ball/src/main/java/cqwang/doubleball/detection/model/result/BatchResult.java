package cqwang.doubleball.detection.model.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchResult {
    /**
     * 预测结果
     */
    private List<Integer> resultList;

    /**
     * 是否预测成功
     */
    private boolean success;

    public BatchResult(List<Integer> resultList) {
        this.resultList = resultList;
        this.success = true;
    }
}
