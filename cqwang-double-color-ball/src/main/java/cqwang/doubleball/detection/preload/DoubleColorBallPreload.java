package cqwang.doubleball.detection.preload;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.SplitBall;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 全局数据
 */
public class DoubleColorBallPreload {
    /**
     * 原始数据，正序
     */
    @Getter
    private static List<DoubleColorBall> allData;


    public static void execute() {
        allData = FileProvider.readFile("/v2/DoubleColorBall.json", new TypeReference<List<DoubleColorBall>>() { });
        for (var data : allData) {
            data.fillBallIntegerValue();
        }

        Collections.reverse(allData);
    }
}
