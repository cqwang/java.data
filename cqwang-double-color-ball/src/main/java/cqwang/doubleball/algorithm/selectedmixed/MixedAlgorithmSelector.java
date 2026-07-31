package cqwang.doubleball.algorithm.selectedmixed;

import cqwang.data.serializer.JSON;
import cqwang.doubleball.algorithm.select.SelectMode;
import cqwang.doubleball.algorithm.select.ValueCalculator;
import cqwang.doubleball.algorithm.select.impl.SingleAlgorithmSelector;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;

import java.util.ArrayList;
import java.util.List;

/**
 * 预测，写一遍selector算法
 */
public class MixedAlgorithmSelector {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;

    public List<MixedAlgorithm> execute() {
        var selectedAlgorithmList = reCalculate();
        for (var algorithm : selectedAlgorithmList) {
            System.out.println(algorithm.toString());
        }
        return selectedAlgorithmList;
    }

    public List<MixedAlgorithm> reCalculate() {
        ArrayList<MixedAlgorithm> selectedAlgorithmList = new ArrayList<>();
        var list =new ArrayList<String>();

        var algorithmRegistryList = new SingleAlgorithmSelector().execute(SelectMode.FROM_FILE);
        for (var red0 : algorithmRegistryList) {
            for (var red1 : algorithmRegistryList) {
                for (var red2 : algorithmRegistryList) {
                    for (var red3 : algorithmRegistryList) {
                        for (var red4 : algorithmRegistryList) {
                            for (var red5 : algorithmRegistryList) {
                                for (var blue : algorithmRegistryList) {
                                    if (red0.getName().equals(red1.getName()) && red0.getName().equals(red2.getName()) && red0.getName().equals(red3.getName()) && red0.getName().equals(red4.getName()) && red0.getName().equals(red5.getName()) && red0.getName().equals(blue.getName())) {
                                        continue;
                                    }
                                    var mixedAlgorithm = new MixedAlgorithm(red0.getInstance(), red1.getInstance(), red2.getInstance(), red3.getInstance(), red4.getInstance(), red5.getInstance(), blue.getInstance());
                                    var sumValue = calculateHistoryPredictValueSum(mixedAlgorithm);
                                    System.out.println("value: "+ sumValue);
                                    if (ValueCalculator.hasNoValue(sumValue) || sumValue < 4500) {
                                        continue;
                                    }
                                    mixedAlgorithm.setHistoryPredictValueSum(sumValue);
                                    selectedAlgorithmList.add(mixedAlgorithm);
                                    var str = red0.getName() + "， "+red1.getName()+", "+ red2.getName()+", "+red3.getName()+", "+red4.getName() +", "+red5.getName()+", "+blue.getName()+ ": "+ sumValue;
                                    System.out.println(str);
                                    list.add(str);
                                }
                            }
                        }
                    }
                }
            }
        }

        for(var str : list){
            System.out.println(str);
        }

        selectedAlgorithmList.sort((o1, o2) -> o2.getHistoryPredictValueSum() - o1.getHistoryPredictValueSum());
        var actualCount = Math.min(50, selectedAlgorithmList.size());
        return selectedAlgorithmList.subList(0, actualCount);
    }


    /**
     * 计算价值
     *
     * @param algorithm
     * @return
     */
    private int calculateHistoryPredictValueSum(MixedAlgorithm algorithm) {
        int sumValue = 0;
        for (int targetIndex = MIN_SAMPLE_COUNT; targetIndex < DoubleColorBallDataPreload.allData().size(); targetIndex++) {
            var predict = algorithm.predict(targetIndex);
            var target = DoubleColorBallDataPreload.allData().get(targetIndex);
            var value = ValueCalculator.calculate(predict, target);
            sumValue += value;
        }
        return sumValue;
    }
}
