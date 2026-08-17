package cqwang.doubleball.detection.model.data;

import cqwang.doubleball.detection.model.option.StrategyOption;
import cqwang.doubleball.detection.model.result.SingleResult;
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

    @Getter
    private int totalFrequency;
    @Getter
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
            if (hasPreSameData(data)) {
                model.setMaxContinuousFrequency(model.getMaxContinuousFrequency() + 1);
            }
        }

        if (data > maxData) {
            maxData = data;
        }
        if (data < minData) {
            minData = data;
        }
    }

    private boolean hasPreSameData(Integer data) {
        if (this.dataList.size() == 1) {
            return false;
        }

        var preData = this.dataList.get(this.dataList.size() - 2);
        return preData == data;
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
    }

    public SingleResult getMaxDataFrequency(Range<Integer> range, StrategyOption option) {
        for (int index = sortedList.size() - 1; index >= 0; index--) {
            var item = sortedList.get(index);
            if (option.isBlocked(item.getData())) {
                continue;
            }

            if (item.getData() >= range.getMinimum() && item.getData() <= range.getMaximum()) {
                return new SingleResult(item.getData(), true);
            }
        }
        return new SingleResult(range.getMinimum(), false);
    }

    public SingleResult getMinDataFrequency(Range<Integer> range, StrategyOption option) {
        for (int index = 0; index < sortedList.size(); index++) {
            var item = sortedList.get(index);
            if (option.isBlocked(item.getData())) {
                continue;
            }

            if (item.getData() >= range.getMinimum() && item.getData() <= range.getMaximum()) {
                return new SingleResult(item.getData(), true);
            }
        }
        return new SingleResult(range.getMinimum(), false);
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


    /**
     * 按照样本量构造单个球对象
     *
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

    public double toAvgFrequencyRatio(Integer data){

        return getFrequency(data) / avgFrequency;
    }

    public int getMaxContinuousFrequency(Integer data) {
        var dataFrequency = dataFrequencyMap.get(data);
        if (dataFrequency == null) {
            return 0;
        }
        return dataFrequency.getMaxContinuousFrequency();
    }


    /**
     * 均值
     * @param range
     * @return
     */
    public int medianData(Range<Integer> range) {
        var median = medianData();
        return Math.max(range.getMinimum(),
                Math.min(range.getMaximum(), (int) Math.round(median)));
    }

    private int medianData(){
        int mid = dataList.size() / 2;
        if (dataList.size() % 2 == 0) {
            return (dataList.get(mid - 1) + dataList.get(mid)) / 2;
        } else {
            return dataList.get(mid);
        }
    }


    /**
     * 加权平均值
     * @return
     */
    public int weightedAverageData(Range<Integer> range){
        var weightedAvg = weightedAverageData();
        return Math.max(range.getMinimum(),
                Math.min(range.getMaximum(), (int) Math.round(weightedAvg)));
    }

    private double weightedAverageData() {
        double sum = 0;
        double totalWeight = 0;

        for (int i = 0; i < dataList.size(); i++) {
            double weight = 1.0 + (double) (i / dataList.size());
            sum += dataList.get(i) * weight;
            totalWeight += weight;
        }

        return sum / totalWeight;
    }
}
