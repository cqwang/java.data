package cqwang.doubleball.common.model;

/**
 * 算法挑选模式
 */
public enum SelectMode {
    /**
     * 直接读取历史数据分析后保存的文件，因为一半短时间内合适的算法不会变动
     */
    FROM_FILE,

    /**
     * 读取文件，并刷新历史预测数值
     */
    RE_CALCULATE_FROM_FILE,


    /**
     * 替换blue
     */
    RE_CALCULATE_FROM_FILE_FOR_RELEVANCE,

    /**
     * 重新计算、挑选算法并保存到文件
     */
    RE_CALCULATE,


}
