package cqwang.doubleball.detection.algorithm;

import com.google.common.base.Stopwatch;
import cqwang.data.serializer.JSON;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.utils.CompareUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface AlgorithmSelector<TRegistry extends AlgorithmRegistry> {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;


    default List<TRegistry> execute(RunOption runOption) {
        var stopWatch = Stopwatch.createStarted();
        try {

            List<TRegistry> algorithmList = null;

            if (runOption == RunOption.RE_CALCULATE) {
                algorithmList = reCalculate();
            } else if (runOption == RunOption.RE_CALCULATE_VALUE_FROM_FILE) {
                algorithmList = reCalculateValueFromFile();
            } else if (runOption == RunOption.FROM_FILE) {
                algorithmList = readFromFile(false);
            }

            if (algorithmList == null) {
                return algorithmList;
            }

            algorithmList.sort(CompareUtils.PREDICT_RESULT_COMPARE);
            var actualCount = Math.min(getMaxCount(), algorithmList.size());
            algorithmList = algorithmList.subList(0, actualCount);
            System.out.println(JSON.toJSONString(algorithmList)); // 保存到文件  手动保存到resource目录下
            return algorithmList;
        } finally {
            stopWatch.stop();
            System.out.println("history predict time : " + stopWatch.elapsed(TimeUnit.SECONDS));
        }
    }

    List<TRegistry> reCalculate();

    default List<TRegistry> reCalculateValueFromFile() {
        var algorithmList = readFromFile(true);
        for (var algorithm : algorithmList) {
            historyPredict(algorithm);
        }
        return algorithmList;
    }

    List<TRegistry> readFromFile(boolean resetHistoryValue);

    void historyPredict(TRegistry registry);

    /**
     * 算法最大数量
     *
     * @return
     */
    default int getMaxCount() {
        return 100;
    }

    /**
     * 文件路径
     *
     * @return
     */
    String getFilePath();
}
