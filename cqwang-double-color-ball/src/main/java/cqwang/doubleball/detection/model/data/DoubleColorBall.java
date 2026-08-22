package cqwang.doubleball.detection.model.data;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class DoubleColorBall {
    /**
     * 期数，唯一标识
     */
    private String code;

    /**
     * 日期字段，也是唯一标识
     */
    private String date;

    /**
     * 红色
     */
    private String red;

    /**
     * 蓝色
     */
    private String blue;

    /**
     * 红色球列表
     */
    private List<Integer> redValueList;

    /**
     * 蓝色球
     */
    private Integer blueValue;

    /**
     * 球列表
     * blue抽象为连续序列，用于整体间隔特征分析等
     */
    private List<Integer> virtualBallValueList;


    public DoubleColorBall(){
        this.redValueList = new ArrayList<>(6);
    }


    /**
     * 填充数值
     */
    public void fillBallIntegerValue() {
        try {
            this.date = this.date.substring(0, 10);

            // 红球
            String[] redValues = this.red.split(",", 6);
            this.redValueList = new ArrayList<>(6);
            for (String value : redValues) {
                this.redValueList.add(NumberUtils.toInt(value));
            }

            // 篮球
            this.blueValue = NumberUtils.toInt(this.blue);

            // 虚拟连续序列
            virtualBallValueList = new ArrayList<>(7);
            virtualBallValueList.addAll(this.redValueList);
            virtualBallValueList.add(this.blueValue + 33);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSimpleInfo() {
        return StringUtils.join(this.redValueList, ", ") + " | " + this.blueValue;
    }


    public DoubleColorBall clone(Integer blue, Integer firstRed) {
        var target = new DoubleColorBall();
        target.blueValue = this.blueValue;
        if (blue != null) {
            target.blueValue = blue;
        }
        target.redValueList.addAll(this.redValueList);
        if (firstRed != null) {
            target.redValueList.set(0, firstRed);
        }
        return target;
    }

    public DoubleColorBall cloneRed(int index, Integer red) {
        var result = clone(null, null);
        result.getRedValueList().set(index, red);
        return result;
    }

    public String getSimilarInfo(int excludeRedIndex) {
        var sb = new StringBuilder();
        for (int i = 0; i < redValueList.size(); i++) {
            if (i == excludeRedIndex) {
                continue;
            }
            sb.append(redValueList.get(i));
            sb.append(" ");
        }
        sb.append("|");
        sb.append(blueValue);
        return sb.toString();
    }


}
