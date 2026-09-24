package cqwang.doubleball.detection.cache;

import cqwang.doubleball.detection.model.data.SplitBall;
import cqwang.doubleball.detection.model.data.SplitBatchBall;

import java.util.HashMap;
import java.util.Map;

public class SplitBallCacheManager {
    private static Map<Integer, SplitBall> cache = new HashMap<>();
    private static Map<Integer, SplitBatchBall> batchCache = new HashMap<>();

    public static SplitBall computeIfAbsent(int targetIndex) {
        return cache.computeIfAbsent(targetIndex, integer -> new SplitBall(targetIndex));
    }

    public static SplitBatchBall computeIfAbsentBatchBall(int targetIndex) {
        return batchCache.computeIfAbsent(targetIndex, integer -> new SplitBatchBall(targetIndex));
    }
}
