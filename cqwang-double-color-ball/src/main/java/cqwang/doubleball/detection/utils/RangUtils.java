package cqwang.doubleball.detection.utils;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SingleBall;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Range;

public class RangUtils {
    /**
     * 获取要预测位序红球的范围
     * @param result 前序预测的结果
     * @param singleBall 当前序列
     * @return
     */
    public static Range<Integer> getRedRange(DoubleColorBall result, SingleBall singleBall) {
        if (CollectionUtils.isEmpty(result.getRedValueList())) {
            return Range.between(singleBall.getMinData(), singleBall.getMaxData());
        }

        var lastRed = result.getRedValueList().get(result.getRedValueList().size() - 1);
        if (lastRed < singleBall.getMinData()) {
            return Range.between(singleBall.getMinData(), singleBall.getMaxData());
        }

        return Range.between(lastRed + 1, singleBall.getMaxData());
    }
}
