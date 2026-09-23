package cqwang.doubleball.detection.cache;

import cqwang.doubleball.detection.model.data.SplitBall;

import java.util.HashMap;
import java.util.Map;

public class SplitBallCacheManager {
    private static Map<Integer, SplitBall> cache = new HashMap<>();

    public static SplitBall computeIfAbsent(int targetIndex) {
        return cache.computeIfAbsent(targetIndex, integer -> new SplitBall(targetIndex));
    }
}
