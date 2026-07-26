package cqwang.doubleball.preload;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;
import cqwang.doubleball.model.DoubleColorBallItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoubleColorBallPreload {

    /**
     * 原始数据，正序
     */
    private static List<DoubleColorBallItem> allData;

    private static List<DoubleColorBallItem> verifyData;
    private static final int VERIFY_DATA_SIZE = 200;

    private static List<DoubleColorBallItem> predictData;
    private static final int PREDICT_DATA_SIZE = 100;


    public static void execute() {
        allData = FileProvider.readFile("/DoubleColorBallData.json",
                new TypeReference<List<DoubleColorBallItem>>() {});

        verifyData = new ArrayList<>(VERIFY_DATA_SIZE);
        for (int i = 0; i < VERIFY_DATA_SIZE; i++) {
            verifyData.add(allData.get(i));
        }

        predictData = new ArrayList<>(PREDICT_DATA_SIZE);
        for (int i = 0; i < PREDICT_DATA_SIZE; i++) {
            predictData.add(allData.get(i));
        }

        Collections.reverse(allData);
        Collections.reverse(verifyData);
        Collections.reverse(predictData);
    }

    public static List<DoubleColorBallItem> allData() {
        return allData;
    }

    public static List<DoubleColorBallItem> verifyData() {
        return verifyData;
    }

    public static List<DoubleColorBallItem> predictData() {
        return predictData;
    }
}
