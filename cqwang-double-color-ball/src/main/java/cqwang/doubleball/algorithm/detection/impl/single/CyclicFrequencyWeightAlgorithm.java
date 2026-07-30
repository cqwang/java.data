package cqwang.doubleball.algorithm.detection.impl.single;

import cqwang.doubleball.algorithm.detection.PredictionAlgorithm;
import cqwang.doubleball.model.BallDataDetail;
import org.apache.commons.lang3.Range;

import java.util.*;

/**
 * 周期频率算法 - 基于周期性的频率分析，权重向中间倾斜
 */
public class CyclicFrequencyWeightAlgorithm implements PredictionAlgorithm {
    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        return predictByCycle(redBallDataDetail, redRange);
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        return predictByCycle(blueBallDataDetail, blueRange);
    }

    private int predictByCycle(BallDataDetail ballDataDetail, Range<Integer> range) {
        var dataList = ballDataDetail.getDataList();
        if (dataList.isEmpty()) {
            return range.getMinimum();
        }

        var period = 7 * 22;
        Map<Integer, Double> dataWeightMap = new HashMap<>();
        for (int index = period; index < ballDataDetail.getDataList().size(); index += period) {

            var subFrequencyMap = new HashMap<Integer, Integer>();
            for (int subIndex = index - period; subIndex < index; subIndex++) {
                var data = ballDataDetail.getDataList().get(subIndex);
                var frequency = subFrequencyMap.getOrDefault(data, 0);
                subFrequencyMap.put(data, ++frequency);
            }

            var subWeightMap = getDataWeight(subFrequencyMap);
            for (var subEntry : subWeightMap.entrySet()) {
                var weight = dataWeightMap.getOrDefault(subEntry.getKey(), 0.0);
                dataWeightMap.put(subEntry.getKey(), weight + subEntry.getValue());
            }
        }

        int result = 0;
        double maxWeight = 0;
        for (var data : dataWeightMap.entrySet()) {
            if (data.getValue() > maxWeight) {
                result = data.getKey();
                maxWeight = data.getValue();
            }
        }

        var seed = range.getMaximum() - result;
        if(seed > 0){
            var random = new Random(seed).nextInt(seed);
            return result + random;
        }
        return result;
    }

    /**
     * 数值权重 = 频率 * 频率权重
     * @param dataFrequencyMap
     * @return
     */
    private Map<Integer, Double> getDataWeight(HashMap<Integer, Integer> dataFrequencyMap){
        var dataWeightMap = new HashMap<Integer,Double>();
        var frequencyWeightMap = getFrequencyWeight(dataFrequencyMap);
        for (var entry : dataFrequencyMap.entrySet()) {
            var dataWeight = entry.getValue() * frequencyWeightMap.get(entry.getValue());
            dataWeightMap.put(entry.getKey(), dataWeight);
        }
        return dataWeightMap;
    }

    /**
     * 1/4位置开始，间距n对应权重1/（间距+1），越远预期越小
     * @param dataFrequencyMap
     * @return
     */
    private Map<Integer, Double> getFrequencyWeight(HashMap<Integer, Integer> dataFrequencyMap) {
        ArrayList<Integer> frequencyList = new ArrayList<>();
        for (var entry : dataFrequencyMap.entrySet()) {
            frequencyList.add(entry.getValue());
        }
        frequencyList.sort(Comparator.comparingInt(o -> o));

        var middle = Double.valueOf(Math.ceil(frequencyList.size() * 0.75));
        var frequencyWeightMap = new HashMap<Integer, Double>();
        for (int i = 0; i < frequencyList.size(); i++) {
            frequencyWeightMap.put(frequencyList.get(i), getWeight(middle.intValue(), i));
        }
        return frequencyWeightMap;
    }

    private double getWeight(int targetIndex, int currentIndex) {
        return 1.0 / (Math.abs(currentIndex - targetIndex) + 1);
    }
}

