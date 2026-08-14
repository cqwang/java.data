package cqwang.doubleball.v2.algorithm.singleball.imp;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.model.result.SingleResult;
import cqwang.doubleball.v2.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class RedPredict implements SingleBallPredictAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        return AlgorithmUtils.predictPriority(singleBall, range, option,
                "DistributionFrequency",
                "SurgeFrequency",
                "MaxFrequency"
        );
    }

//    @Override
//    public SingleResult predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
//        return AlgorithmUtils.predictPriority(singleBall, range, option,
//                "MaxDistributionCumulativeWeightFrequency", // 结合多个时间窗口的加权频次，权重加在数值区间上累计 "sumValue":4305,"hitTotalCount":139,"hitBlueTotalCount":133,"maxValue":3000
//                "MaxDistributionSplitWeightFrequency", // 结合多个时间窗口的加权频次，权重加在每段独立的数值区间上 "sumValue":4120,"hitTotalCount":141,"hitBlueTotalCount":135,"maxValue":3000
//                "SurgeFrequency", // 频率突跃算法 - 检测并偏好频率的突跃点 "sumValue":4105,"hitTotalCount":133,"hitBlueTotalCount":122,"maxValue":3000
//                "RecentMaxWeightFrequency", // 取最近出现权重最高的数值，时间越近权重越高 "sumValue":3940,"hitTotalCount":144,"hitBlueTotalCount":137,"maxValue":3000
//                "RecentMaxFrequency", // 爆发检测，取最近出现频次最高的数值 "sumValue":3760,"hitTotalCount":144,"hitBlueTotalCount":135,"maxValue":3000
//                "ContinuityWeightFrequency", // 基于连续性的预测 - 连续出现的频次和权重 "sumValue":3660,"hitTotalCount":128,"hitBlueTotalCount":123,"maxValue":3000
//                "DistributionFrequency" // 结合长期、中期、短期的冷热特征推荐 "sumValue":1220,"hitTotalCount":155,"hitBlueTotalCount":144,"maxValue":200
//        );
//    }
}
