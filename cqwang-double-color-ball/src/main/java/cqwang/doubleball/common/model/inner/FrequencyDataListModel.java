package cqwang.doubleball.common.model.inner;

import cqwang.doubleball.common.model.BallDataDetail;
import lombok.Data;

import java.util.*;

@Data
public class FrequencyDataListModel {
    private int totalFrequency;
    private double avgFrequency;
    private int maxFrequency;
    private int maxFrequencyData;


    private Map<Integer, Integer> dataFrequencyMap;
    private Map<Integer, DataLevel> dataLevelMap;


    public FrequencyDataListModel(BallDataDetail ballDataDetail, int sampleSize) {
        this.totalFrequency = 0;

        this.dataFrequencyMap = new HashMap<>();
        var startIndex = Math.max(ballDataDetail.getDataList().size() - sampleSize, 0);
        for (int i = startIndex; i < ballDataDetail.getDataList().size(); i++) {
            var data = ballDataDetail.getDataList().get(i);
            var value = dataFrequencyMap.getOrDefault(data, 0);
            dataFrequencyMap.put(data, (value + 1));
            this.totalFrequency++;
        }

        for (var entry : this.dataFrequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                this.maxFrequency = entry.getValue();
                this.maxFrequencyData = entry.getKey();
            }
        }

        this.avgFrequency = (double) totalFrequency / dataFrequencyMap.size();

        this.dataLevelMap = new HashMap<>(dataFrequencyMap.size());
        var maxCold = DataLevel.COLD.getToAvgFrequencyRatio() * this.avgFrequency;
        var maxStable = DataLevel.STABLE.getToAvgFrequencyRatio() * this.avgFrequency;
        var maxHot = DataLevel.HOT.getToAvgFrequencyRatio() * this.avgFrequency;
        for (var entry : dataFrequencyMap.entrySet()) {
            if (entry.getValue() < maxCold) {
                dataLevelMap.put(entry.getKey(), DataLevel.COLD);
            } else if (entry.getValue() < maxStable) {
                dataLevelMap.put(entry.getKey(), DataLevel.STABLE);
            } else if (entry.getValue() < maxHot) {
                dataLevelMap.put(entry.getKey(), DataLevel.HOT);
            } else {
                dataLevelMap.put(entry.getKey(), DataLevel.SO_HOT);
            }
        }
    }

    public int getFrequency(Integer data) {
        var result = dataFrequencyMap.get(data);
        if (result == null) {
            return 0;
        }
        return result;
    }

    public DataLevel getLevel(Integer data) {
        var result = dataLevelMap.get(data);
        if (result == null) {
            return DataLevel.COLD;
        }
        return result;
    }

}
