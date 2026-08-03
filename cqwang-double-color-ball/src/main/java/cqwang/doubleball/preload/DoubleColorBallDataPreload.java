package cqwang.doubleball.preload;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.doubleball.common.model.DoubleColorBallItem;

import java.util.Collections;
import java.util.List;

/**
 * 全局静态
 */
public class DoubleColorBallDataPreload {

    /**
     * 原始数据，正序
     */
    private static List<DoubleColorBallItem> allData;

    public static void execute() {
        allData = FileProvider.readFile("/DoubleColorBallData.json", new TypeReference<List<DoubleColorBallItem>>() {});
        Collections.reverse(allData);
    }

    public static List<DoubleColorBallItem> allData() {
        return allData;
    }
}
