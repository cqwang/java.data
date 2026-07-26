package cqwang.doubleball.model;

import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class DoubleColorBallItem {
    private String code;
    private String date;
    /**
     * 红色
     */
    private String red;

    /**
     * 蓝色
     */
    private String blue;
    private List<Integer> redValueList;
    private Integer blueValue;

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
}
