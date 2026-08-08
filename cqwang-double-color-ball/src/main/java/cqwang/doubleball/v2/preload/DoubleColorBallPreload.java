package cqwang.doubleball.v2.preload;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.doubleball.v2.model.data.DoubleColorBall;
import cqwang.doubleball.v2.model.data.SplitBall;
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
    /**
     * 每个位置的球的数值序列，正序
     */
    @Getter
    private static SplitBall splitBall;

    public static void execute() {
        allData = FileProvider.readFile("/v2/DoubleColorBall.json", new TypeReference<List<DoubleColorBall>>() { });
        for (var data : allData) {
            data.fillBallIntegerValue();
        }

        Collections.reverse(allData);

        //
        splitBall = new SplitBall(allData.size());
    }
}
