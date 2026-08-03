package cqwang.doubleball.common.model;

import java.util.ArrayList;
import java.util.List;

public class VirtualDoubleColorBallItem {
    /**
     * 球列表
     * blue抽象为连续序列，用于间隔特征分析
     */
    private List<Integer> ballValueList;

    public VirtualDoubleColorBallItem(DoubleColorBallItem doubleColorBallItem) {
        ballValueList = new ArrayList<>(7);
        ballValueList.addAll(doubleColorBallItem.getRedValueList());

        var maxRed = doubleColorBallItem.getRedValueList().get(doubleColorBallItem.getRedValueList().size() - 1);
        ballValueList.add(doubleColorBallItem.getBlueValue() + 33 - maxRed);
    }

    public List<Integer> getBallValueList() {
        return ballValueList;
    }
}
