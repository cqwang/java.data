package cqwang.doubleball.spider;

import cqwang.doubleball.common.model.DoubleColorBallItem;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

public class DoubleColorBallPipeline implements Pipeline {



    @Override
    public void process(ResultItems resultItems, Task task) {
        List<DoubleColorBallItem> dataList = resultItems.get("result");
        for(DoubleColorBallItem data : dataList){
            data.formatData();
        }

        DoubleColorBallContext.allData.addAll(dataList);
    }
}
