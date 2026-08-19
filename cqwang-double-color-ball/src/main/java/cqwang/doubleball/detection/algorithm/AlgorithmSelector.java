package cqwang.doubleball.detection.algorithm;

import cqwang.data.serializer.JSON;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.utils.CompareUtils;

import java.util.List;

public interface AlgorithmSelector<TRegistry extends AlgorithmRegistry> {
    /**
     * 最小样本量
     */
    int MIN_SAMPLE_COUNT = 100;


    default List<TRegistry> execute(RunOption runOption) {
        List<TRegistry> algorithmList = null;

        if(runOption == RunOption.RE_CALCULATE_SINGLE_BALL){
            return reCalculateJustForSingle();
        }


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
    }

    List<TRegistry> reCalculateJustForSingle();

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
