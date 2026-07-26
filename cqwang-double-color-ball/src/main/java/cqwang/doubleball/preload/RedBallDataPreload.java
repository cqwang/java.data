package cqwang.doubleball.preload;

import cqwang.doubleball.model.DoubleColorBallItem;
import cqwang.doubleball.model.RedBallData;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class RedBallDataPreload {
    private static RedBallData redBallData;

    public static void execute() {
        List<DoubleColorBallItem> allData = DoubleColorBallPreload.allData();
        if (CollectionUtils.isEmpty(allData)) {
            return;
        }

        redBallData = new RedBallData();
        for (DoubleColorBallItem item : allData) {
            for (int j = 0; j < item.getRedValueList().size(); j++) {
                var redValue = item.getRedValueList().get(j);
                redBallData.getRedBallDetail(j).addData(redValue);
            }
        }
    }

    public static RedBallData redBallData() {
        return redBallData;
    }
}
