package cqwang.doubleball.model;

import lombok.Data;

import java.util.List;

@Data
public class DoubleColorBallItem {
    private String code;
    private String date;
    private List<Integer> redValueList;
    private Integer blueValue;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Integer> getRedValueList() {
        return redValueList;
    }

    public void setRedValueList(List<Integer> redValueList) {
        this.redValueList = redValueList;
    }

    public Integer getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(Integer blueValue) {
        this.blueValue = blueValue;
    }

    public void formatData() {
    }
}
