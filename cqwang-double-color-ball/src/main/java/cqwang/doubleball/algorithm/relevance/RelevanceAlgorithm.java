package cqwang.doubleball.algorithm.relevance;

import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;
import cqwang.doubleball.preload.DoubleColorBallDataPreload;
import cqwang.doubleball.preload.SampleDataRealtimeLoad;

import java.util.List;

public interface RelevanceAlgorithm {

    /**
     * 根据已经预测的redList来预测blue
     * @param targetIndex 要预测的目标数据位序
     * @param predictedRedValueList 已经预测的redValueList结果
     * @return
     */
    default int predictBlue(int targetIndex, List<Integer> predictedRedValueList){
        // 获取样本数据
        var sampleDataRealtimeLoad = new SampleDataRealtimeLoad();
        sampleDataRealtimeLoad.execute(targetIndex);

        var sampleList = sampleDataRealtimeLoad.getVirtualItemList().subList(0, targetIndex);
        return predictBlue(predictedRedValueList, sampleList);
    }

    int predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList);
}
