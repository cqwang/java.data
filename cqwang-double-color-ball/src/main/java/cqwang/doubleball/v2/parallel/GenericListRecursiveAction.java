package cqwang.doubleball.v2.parallel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class GenericListRecursiveAction<T> extends RecursiveAction {

    private final List<T> dataList;
    private final int startIndex;
    private final int endIndex;
    private final int chunkSize;
    private final Consumer<T> consumer;

    /**
     * 构造函数（默认从 0 到 list.size()）
     *
     * @param dataList  要处理的列表
     * @param chunkSize 拆分任务的大小
     * @param consumer  每个元素的处理逻辑（T 为元素，Integer 为索引）
     */
    public GenericListRecursiveAction(List<T> dataList,
                                      int chunkSize,
                                      Consumer<T> consumer) {
        this(dataList,
                0,
                Objects.requireNonNull(dataList, "dataList must not be null").size(),
                chunkSize,
                Objects.requireNonNull(consumer, "consumer must not be null"));
    }

    /**
     * 构造函数（支持自定义起止索引）
     *
     * @param dataList   要处理的列表
     * @param startIndex 起始索引
     * @param endIndex   结束索引（不包含）
     * @param chunkSize  拆分任务的大小
     * @param consumer   每个元素的处理逻辑（T 为元素）
     */
    private GenericListRecursiveAction(
            List<T> dataList,
            int startIndex,
            int endIndex,
            int chunkSize,
            Consumer<T> consumer) {
        this.dataList = dataList;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.chunkSize = Math.max(chunkSize, 1);
        this.consumer = consumer;
    }

    @Override
    protected void compute() {
        if (isCancelled()) {
            return;
        }
        if (endIndex - startIndex <= chunkSize) {
            for (int i = startIndex; i < endIndex; i++) {
                if (isCancelled()) {
                    return;
                }
                // 执行用户提供的逻辑
                consumer.accept(dataList.get(i));
            }
            return;
        }

        int mid = (startIndex + endIndex) >> 1;
        GenericListRecursiveAction<T> leftTask = new GenericListRecursiveAction<>(
                dataList, startIndex, mid, chunkSize, consumer);
        GenericListRecursiveAction<T> rightTask = new GenericListRecursiveAction<>(
                dataList, mid, endIndex, chunkSize, consumer);

        invokeAll(leftTask, rightTask);
    }
}
