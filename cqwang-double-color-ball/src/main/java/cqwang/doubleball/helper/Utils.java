package cqwang.doubleball.helper;

import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.preload.SampleDataPreload;
import org.apache.commons.collections4.CollectionUtils;

public class Utils {

    /**
     * 校验生成的红色球数值是否合法
     *
     * @param result
     * @param predictIndex
     * @param predictValue
     */
      public boolean validateRed(DoubleColorBallItem result, int predictIndex, int predictValue) {
        if (CollectionUtils.isEmpty(result.getRedValueList())) {
            return true;
        }

        // 不能重复
        if (result.getRedValueList().contains(predictValue)) {
            return false;
        }

        // 数值只能按照位序变大
        var lastRed = result.getRedValueList().get(result.getRedValueList().size() - 1);
        if (predictValue <= lastRed) {
            return false;
        }

        var redBallDetail = SampleDataPreload.redBallData().getRedBallDetail(predictIndex);
        // 数值区间约束
        if (predictValue > redBallDetail.getMax() || predictValue < redBallDetail.getMin()) {
            return false;
        }
        return true;
    }
}
