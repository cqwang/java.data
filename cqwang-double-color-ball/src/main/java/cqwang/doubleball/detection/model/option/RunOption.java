package cqwang.doubleball.detection.model.option;

public enum RunOption {
    /**
     * 从文件中读取算法、价值
     */
    FROM_FILE,

    /**
     * 从文件读取算法，重算价值
     */
    RE_CALCULATE_VALUE_FROM_FILE,


    /**
     * 重新计算算法和价值、挑选并返回，需要手动保存到文件
     */
    RE_CALCULATE,

    /**
     * 单个球
     */
    RE_CALCULATE_SINGLE_BALL
}
