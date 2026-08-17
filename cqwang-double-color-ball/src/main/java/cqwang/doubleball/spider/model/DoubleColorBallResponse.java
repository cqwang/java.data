package cqwang.doubleball.spider.model;

import cqwang.doubleball.detection.model.data.DoubleColorBall;
import lombok.Data;

import java.util.List;

@Data
public class DoubleColorBallResponse {
    private List<DoubleColorBall> result;

    public List<DoubleColorBall> getResult() {
        return result;
    }

    public void setResult(List<DoubleColorBall> result) {
        this.result = result;
    }
}
