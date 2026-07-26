package cqwang.doubleball.model;

import lombok.Data;

import java.util.List;

@Data
public class DoubleColorBallResponse {
    private List<DoubleColorBallItem> result;

    public List<DoubleColorBallItem> getResult() {
        return result;
    }

    public void setResult(List<DoubleColorBallItem> result) {
        this.result = result;
    }
}
