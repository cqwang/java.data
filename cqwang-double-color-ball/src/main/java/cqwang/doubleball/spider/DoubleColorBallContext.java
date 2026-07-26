package cqwang.doubleball.spider;


import cqwang.data.serializer.JSON;
import cqwang.doubleball.model.DoubleColorBallItem;

import java.util.ArrayList;
import java.util.List;

public class DoubleColorBallContext {
    public static List<DoubleColorBallItem> allData = new ArrayList<>();

    public static void write(){
        String json = JSON.toJSONString(allData);

        // 写入文件，落地




        System.out.println();
    }
}
