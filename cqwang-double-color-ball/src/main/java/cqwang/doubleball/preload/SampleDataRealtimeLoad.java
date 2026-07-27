package cqwang.doubleball.preload;

import cqwang.doubleball.model.BallDataDetail;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.model.RedBallData;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 实时加载，用于算法挑选时的数据回归验证
 */
public class SampleDataRealtimeLoad {

    @Getter
    private RedBallData redBallData;
    @Getter
    private BallDataDetail blueBallDetail;

    /**
     * 基类的数据越多，样本量越大，所以只要是前序数据，都可以作为样本
     *
     * @param preSampleNum 样本数量
     */
    public void execute(int preSampleNum) {
        List<DoubleColorBallItem> preSampleData = DoubleColorBallDataPreload.allData().subList(0, preSampleNum);
        if (CollectionUtils.isEmpty(preSampleData)) {
            return;
        }

        redBallData = new RedBallData();
        blueBallDetail = new BallDataDetail(0);
        for (DoubleColorBallItem item : preSampleData) {
            for (int j = 0; j < item.getRedValueList().size(); j++) {
                var redValue = item.getRedValueList().get(j);
                redBallData.getRedBallDetail(j).addData(redValue);
            }
            blueBallDetail.addData(item.getBlueValue());
        }
    }
}
