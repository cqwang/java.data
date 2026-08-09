package cqwang.doubleball.v2.model.data;

import cqwang.doubleball.common.model.inner.DataLevel;
import cqwang.doubleball.v2.model.data.features.FrequencyLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指定位序的球
 */
@NoArgsConstructor
public class SingleBall {
    /**
     * 位序
     */
    private Integer index;
    /**
     * 历史数据按序记录
     */
    @Getter
    private List<Integer> dataList;
    /**
     * 历史数据和出现的频次
     */
    private Map<Integer, DataFrequency> dataFrequencyMap;

    /**
     * 历史数据按照频次、数值排序
     */
    private List<DataFrequency> sortedList;


    // 全局约束 初始值
    @Getter
    private int maxData = 1;
    @Getter
    private int minData = 33;

    private int totalFrequency;
    private double avgFrequency;

    public SingleBall(int index) {
        this.index = index;
        this.dataList = new ArrayList<>();
        this.dataFrequencyMap = new HashMap<>();
    }

    public void addData(Integer data) {
        this.dataList.add(data);

        var model = dataFrequencyMap.get(data);
        if (model == null) {
            dataFrequencyMap.put(data, new DataFrequency(data));
        } else {
            model.setFrequency(model.getFrequency() + 1);
        }

        if (data > maxData) {
            maxData = data;
        }
        if (data < minData) {
            minData = data;
        }
    }

    /**
     * 完成填充
     */
    public void completeFill() {
        // 按照频率从小到达、 数据从大到小排序
        sortedList = new ArrayList<>(dataFrequencyMap.values());
        sortedList.sort((left, right) -> {
            if (left.getFrequency() == right.getFrequency()) {
                return right.getData() - left.getData();
            }
            return left.getFrequency() - right.getFrequency();
        });

        this.totalFrequency = dataList.size();
        this.avgFrequency = (double) totalFrequency / dataFrequencyMap.size();

        // 计算冷热度
        var maxSoCold = FrequencyLevel.SO_COLD.getToAvgFrequencyRatio() * this.avgFrequency;
        var maxCold = FrequencyLevel.COLD.getToAvgFrequencyRatio() * this.avgFrequency;
        var maxStable = DataLevel.STABLE.getToAvgFrequencyRatio() * this.avgFrequency;
        var maxHot = DataLevel.HOT.getToAvgFrequencyRatio() * this.avgFrequency;
        for (var dataFrequency : sortedList) {
            if (dataFrequency.getFrequency() <= maxSoCold) {
                dataFrequency.setFrequencyLevel(FrequencyLevel.SO_COLD);
            } else if (dataFrequency.getFrequency() < maxCold) {
                dataFrequency.setFrequencyLevel(FrequencyLevel.COLD);
            } else if (dataFrequency.getFrequency() < maxStable) {
                dataFrequency.setFrequencyLevel(FrequencyLevel.STABLE);
            } else if (dataFrequency.getFrequency() < maxHot) {
                dataFrequency.setFrequencyLevel(FrequencyLevel.HOT);
            } else {
                dataFrequency.setFrequencyLevel(FrequencyLevel.SO_HOT);
            }
        }
    }

    public DataFrequency getMaxDataFrequency(Range<Integer> range) {
        for(int index = sortedList.size() -1 ; index>=0;index--){
            var item = sortedList.get(index);
            if(item.getData()>=range.getMinimum() && item.getData()<=range.getMaximum()){
                return item;
            }
        }
        return new DataFrequency(range.getMinimum());
    }

    public DataFrequency get(Integer data) {
        return dataFrequencyMap.get(data);
    }

    public int getFrequency(Integer data) {
        var result = dataFrequencyMap.get(data);
        if (result == null) {
            return 0;
        }
        return result.getFrequency();
    }

    public FrequencyLevel getFrequencyLevel(Integer data) {
        var result = dataFrequencyMap.get(data);
        if (result == null) {
            return FrequencyLevel.SO_COLD;
        }
        return result.getFrequencyLevel();
    }


    /**
     * 按照样本量构造单个球对象
     * @param sampleSize
     * @return
     */
    public SingleBall sub(int sampleSize) {
        if (sampleSize > this.dataList.size()) {
            sampleSize = this.dataList.size();
        }

        var sub = new SingleBall(this.index);
        int startIndex = this.dataList.size() - sampleSize;
        for (var i = startIndex; i < this.dataList.size(); i++) {
            sub.addData(this.dataList.get(i));
        }

        sub.completeFill();
        return sub;
    }
}
