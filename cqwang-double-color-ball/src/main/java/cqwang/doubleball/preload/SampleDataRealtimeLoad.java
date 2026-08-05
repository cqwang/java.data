package cqwang.doubleball.preload;

import cqwang.doubleball.common.model.BallDataDetail;
import cqwang.doubleball.common.model.DoubleColorBallItem;
import cqwang.doubleball.common.model.RedBallData;
import cqwang.doubleball.common.model.VirtualDoubleColorBallItem;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
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
     * 虚拟数据，用于特征分析
     */
    @Getter
    private List<VirtualDoubleColorBallItem> virtualItemList;

    /**
     * 基类的数据越多，样本量越大，所以只要是前序数据，都可以作为样本
     *
     * @param preSampleNum 样本数量
     */
    public void execute(int preSampleNum) {
        execute(0, preSampleNum);
    }

    public void execute(int startIndex, int preSampleNum) {
        List<DoubleColorBallItem> preSampleData = DoubleColorBallDataPreload.allData().subList(startIndex, preSampleNum);
        if (CollectionUtils.isEmpty(preSampleData)) {
            return;
        }

        redBallData = new RedBallData();
        blueBallDetail = new BallDataDetail(0);
        virtualItemList = new ArrayList<>(preSampleNum);
        for (DoubleColorBallItem item : preSampleData) {
            for (int j = 0; j < item.getRedValueList().size(); j++) {
                var redValue = item.getRedValueList().get(j);
                redBallData.getRedBallDetail(j).addData(redValue);
            }
            blueBallDetail.addData(item.getBlueValue());
            var virtualItem = new VirtualDoubleColorBallItem(item);
            virtualItemList.add(virtualItem);
        }
    }
}
