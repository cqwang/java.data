package cqwang.doubleball.v2.model.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictOptionDetail {
    /**
     * 黑名单
     */
    private Set<Integer> blocks;
}
