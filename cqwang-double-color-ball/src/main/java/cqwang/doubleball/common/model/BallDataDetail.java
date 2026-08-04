package cqwang.doubleball.common.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BallDataDetail {
    /**
     * 位序
     */
    private Integer index;
    /**
     * 历史数据按序记录
     */
    private List<Integer> dataList;
    /**
     * 历史数据和出现的频次
     */
    private Map<Integer, Integer> dataFrequencyMap;

    /**
     * 历史数据和出现的频次权重
     */
    private Map<Integer, Integer> dataFrequencyWeightMap;

    // 全局约束 初始值
    private int max = 1;
    private int min = 33;

    public BallDataDetail() {

    }

    public BallDataDetail(int index) {
        this.index = index;
        this.dataList = new ArrayList<>();
        this.dataFrequencyMap = new HashMap<>();
        this.dataFrequencyWeightMap = new HashMap<>();
    }

    public void addData(Integer data) {
        this.dataList.add(data);

        var value = dataFrequencyMap.getOrDefault(data, 0);
        dataFrequencyMap.put(data, (value + 1));

        if (data > max) {
            max = data;
        }
        if (data < min) {
            min = data;
        }
    }

    public void calculateWeight() {
        int maxCount = this.dataFrequencyMap.values().stream().mapToInt(t -> t).max().getAsInt();
        int targetCount = (int) (maxCount * 0.85);
        for (var entry : dataFrequencyMap.entrySet()) {
            var diff = Math.abs(entry.getValue() - targetCount);
            var weight = (diff == 0) ? maxCount: maxCount / diff;
            this.dataFrequencyWeightMap.put(entry.getKey(), weight);
        }
    }
}
