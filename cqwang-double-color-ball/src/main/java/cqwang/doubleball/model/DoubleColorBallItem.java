package cqwang.doubleball.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class DoubleColorBallItem {
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

    public DoubleColorBallItem() {

    }

    public DoubleColorBallItem(boolean init) {
        this();
        if (init) {
            this.redValueList = new ArrayList<>(6);
        }
    }

    public void formatData() {
        try {
            this.date = this.date.substring(0, 10);

            String[] redValues = this.red.split(",", 6);
            this.redValueList = new ArrayList<>();
            for (String value : redValues) {
                this.redValueList.add(NumberUtils.toInt(value));
            }

            this.blueValue = NumberUtils.toInt(this.blue);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSimpleInfo() {
        return StringUtils.join(this.redValueList, ", ") + " | " + this.blueValue;
    }
}
