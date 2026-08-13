//package cqwang.doubleball.v2.utils;
//
//import cqwang.doubleball.v2.algorithm.doublecolorball.DoubleColorPredictionAlgorithmRegistry;
//import cqwang.doubleball.v2.algorithm.doublecolorball.DoubleColorPredictionAlgorithmSelector;
//import cqwang.doubleball.v2.algorithm.singleball.SingleBallAlgorithmRegistryFactory;
//import cqwang.doubleball.v2.algorithm.singleball.SingleBallPredictAlgorithmRegistry;
//import cqwang.doubleball.v2.model.option.PredictOption;
//import cqwang.doubleball.v2.preload.DoubleColorBallPreload;
//import org.apache.commons.lang3.Range;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.*;
//
//public class CacheManager {
//    private static Map<String,Map<Boolean, Map<Integer, Map<String, Integer>>>> map = new ConcurrentHashMap<>();
//
//    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20,
//            60L, TimeUnit.SECONDS,
//            new LinkedBlockingQueue<Runnable>());
//
//    /**
//     * 预加载
//     */
//    private static void preLoad(){
//        var selector = new DoubleColorPredictionAlgorithmSelector();
//        var algorithmList = SingleBallAlgorithmRegistryFactory.getAlgorithmPool();
//        var countdown = new CountDownLatch(algorithmList.size());
//        for(var algorithm: algorithmList) {
//            var advancedAlgorithm = new DoubleColorPredictionAlgorithmRegistry(algorithm, algorithm);
//            threadPool.execute(() -> {
//                for (int targetIndex = 100; targetIndex < DoubleColorBallPreload.getAllData().size(); targetIndex++) {
//                    advancedAlgorithm.predict(targetIndex, new PredictOption());
//                }
//            });
//        }
//
//        try {
//            countdown.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public static int put(SingleBallPredictAlgorithmRegistry algorithmRegistry,
//                          boolean isBlue, int ballIndex, int targetIndex, Range<Integer> range, boolean retry){
//
//    }
//
//
//    public static int predictWithCache(
//            SingleBallPredictAlgorithmRegistry algorithmRegistry,
//            boolean isBlue, int ballIndex, int targetIndex, Range<Integer> range, boolean retry) {
//        var algorithmMap = map.get(algorithmRegistry.getAlgorithmName());
//        if(algorithmMap == null) {
//            algorithmMap = new HashMap<>();
//            map.put(algorithmRegistry.getAlgorithmName(), algorithmMap);
//        }
//
//
//
//    }
//
//}
