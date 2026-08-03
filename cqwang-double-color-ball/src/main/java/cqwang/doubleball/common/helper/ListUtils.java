package cqwang.doubleball.common.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {
    public static <T> List<T> reversed(List<T> dataList) {
        var reversedDataList = new ArrayList<T>(dataList.size());
        for (int i = dataList.size() - 1; i >= 0; i--) {
            reversedDataList.add(dataList.get(i));
        }
        return reversedDataList;
    }

    public static <T extends Comparable<T>> List<T> getSortedList(List<T> dataList) {
        var sortedList = new ArrayList<T>(dataList.size());
        sortedList.addAll(dataList);
        Collections.sort(sortedList);
        return sortedList;
    }
}
