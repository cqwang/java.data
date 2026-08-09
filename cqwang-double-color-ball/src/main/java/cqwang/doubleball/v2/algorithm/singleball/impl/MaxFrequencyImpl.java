package cqwang.doubleball.v2.algorithm.singleball.impl;

import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithm;
import cqwang.doubleball.v2.model.data.SingleBall;
import cqwang.doubleball.v2.model.option.StrategyOption;
import cqwang.doubleball.v2.utils.AlgorithmUtils;
import org.apache.commons.lang3.Range;

public class MaxFrequencyImpl implements SingleBallPredictAlgorithm {
    @Override
    public int predict(SingleBall singleBall, Range<Integer> range, StrategyOption option) {
        if (option.isRecent()) {

            if(option.isWeight()){
                // 取最近出现权重最高的数值，时间越近权重越高
                var sub = singleBall.sub(80);
                return AlgorithmUtils.squareWeight(sub, range, option);
            }

            // 爆发检测，取最近出现频次最高的数值
            var sub = singleBall.sub(15);
            return sub.getMaxDataFrequency().getData();
        }

        // 从全量样本中，选取出现频次最高的数值
        return singleBall.getMaxDataFrequency().getData();
    }
}
