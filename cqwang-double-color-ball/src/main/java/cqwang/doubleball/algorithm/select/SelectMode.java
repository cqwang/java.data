package cqwang.doubleball.algorithm.select;

/**
 * 算法挑选模式
 */
public enum SelectMode {
    /**
     * 直接读取历史数据分析后保存的文件，因为一半短时间内合适的算法不会变动
     */
    FROM_FILE,

    /**
     * 重新计算、挑选算法并保存到文件
     */
    RE_CALCULATE
}
