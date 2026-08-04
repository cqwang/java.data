package cqwang.doubleball.algorithm.single.impl.toredo;

import cqwang.doubleball.algorithm.optimization.AlgorithmOptimizationUtils;
import cqwang.doubleball.algorithm.single.SingleAlgorithm;
import cqwang.doubleball.common.model.BallDataDetail;
import org.apache.commons.lang3.Range;

/**
 * K近邻算法 - K近邻优化版
 * 优化策略：加权平均 + 相似度预测，多维度分析
 */
public class KNearestNeighbors3Algorithm implements SingleAlgorithm {

    @Override
    public int predictRed(BallDataDetail redBallDataDetail, Range<Integer> redRange) {
        double weighted = AlgorithmOptimizationUtils.calculateWeightedAverage(
                redBallDataDetail.getDataList(), 50);
        return Math.max(redRange.getMinimum(),
                Math.min(redRange.getMaximum(), (int) Math.round(weighted)));
    }

    @Override
    public int predictBlue(BallDataDetail blueBallDataDetail, Range<Integer> blueRange) {
        double weighted = AlgorithmOptimizationUtils.calculateWeightedAverage(
                blueBallDataDetail.getDataList(), 50);
        return Math.max(blueRange.getMinimum(),
                Math.min(blueRange.getMaximum(), (int) Math.round(weighted)));
    }

    protected int getK(){
        return 3;
    }
}
