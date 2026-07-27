package cqwang.doubleball.preload;

import cqwang.doubleball.model.BallDataDetail;
import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.model.RedBallData;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class RedBallDataPreload {
    private static RedBallData redBallData;
    private static BallDataDetail blueBallDetail;

    public static void execute() {
        List<DoubleColorBallItem> allData = DoubleColorBallDataPreload.allData();
        if (CollectionUtils.isEmpty(allData)) {
            return;
        }

        redBallData = new RedBallData();
        blueBallDetail = new BallDataDetail(0);
        for (DoubleColorBallItem item : allData) {
            for (int j = 0; j < item.getRedValueList().size(); j++) {
                var redValue = item.getRedValueList().get(j);
                redBallData.getRedBallDetail(j).addData(redValue);
            }
            blueBallDetail.addData(item.getBlueValue());
        }
    }

    public static RedBallData redBallData() {
        return redBallData;
    }

    public static BallDataDetail blueBallDetail(){
        return blueBallDetail;
    }
}
