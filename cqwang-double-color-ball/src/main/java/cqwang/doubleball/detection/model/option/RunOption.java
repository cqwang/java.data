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
     * 仅仅用于篮球算法选取，结果要按照篮球命中次数排序
     */
    RE_CALCULATE_JUST_FOR_BLUE
}
