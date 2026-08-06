package cqwang.doubleball.common;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class VoteUtils {
    public static int randomVote(List<Integer> dataList, List<Integer> weightList) {
        if (CollectionUtils.isEmpty(dataList) || CollectionUtils.isEmpty(weightList) || dataList.size() != weightList.size()) {
            return -1;
        }

        var list = new ArrayList<Integer>();
        for (int i = 0; i < dataList.size(); i++) {
            var data = dataList.get(i);
            var weight = weightList.get(i);
            for (int j = 0; j < weight; j++) {
                list.add(data);
            }
        }

        var index = (int) (Math.random() * list.size());
        var result = list.get(index);
        return result;
    }
}
