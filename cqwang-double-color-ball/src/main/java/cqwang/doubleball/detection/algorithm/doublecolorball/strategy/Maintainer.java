package cqwang.doubleball.detection.algorithm.doublecolorball.strategy;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.model.data.DoubleColorBall;
import cqwang.doubleball.detection.model.data.features.BallType;
import cqwang.doubleball.detection.model.option.PredictOption;

import java.util.*;
import java.util.stream.Collectors;

public class Maintainer {
    public static void vote(List<DoubleColorBall> list, PredictOption option, int targetIndex, DoubleColorAlgorithmRegistry generator) {
        // 如果大家推荐的雷同，则补全缺失
        var size = list.size();
        var map = buildSimilarGroupList(list);
        for (var entry : map.entrySet()) {
            var coreIndex = entry.getKey();
            for (var subEntry : entry.getValue().entrySet()) {
                var similarList = subEntry.getValue();
                if (similarList.size() <= 2) {
                    continue;
                }

                var allSameLit = similarList.stream().filter(t -> t.getSimpleInfo().equals(similarList.get(0).getSimpleInfo())).collect(Collectors.toList());
                if (allSameLit.size() > similarList.size() / 2) {
                    continue;
                }

                var talkPredict = similarList.get(0);
//                if (coreIndex == 0) {
//                    if (similarList.size() > size * 0.75) { // 有一半以上推荐
//                        // 1 2 3 red前移, 4 5 不变，3遍历生成
//                        var coreOption = buildOption(option, talkPredict, Set.of(1, 2, 3), Set.of(4, 5));
//                        var corePredict = generator.predict(targetIndex, coreOption);
//                        for (var red = corePredict.getRedValueList().get(2) + 1; red < corePredict.getRedValueList().get(4); red++) {
//                            list.add(corePredict.cloneRed(3, red));
//                        }
//                    }
//                }
//                else if (coreIndex == 1) {
//                    if (similarList.size() >= size * 0.5) {
//                        // 2 3 4 5 前移，0不变，5遍历生成
//                        var coreOption = buildOption(option, talkPredict, Set.of(2, 3, 4, 5), Set.of(0));
//                        var corePredict = generator.predict(targetIndex, coreOption);
//                        for (var red = corePredict.getRedValueList().get(4) + 1; red <= 33; red++) {
//                            list.add(corePredict.cloneRed(5, red));
//                        }
//                    }
//                }
//                else if (similarList.size() > 2) {
//                }

            }
        }
    }

    /**
     * 忽略一位红球差异，按照其他球的数值分组
     *
     * @param ballList
     * @return
     */
    private static Map<Integer, Map<String, List<DoubleColorBall>>> buildSimilarGroupList(List<DoubleColorBall> ballList) {
        Map<Integer, Map<String, List<DoubleColorBall>>> map = new HashMap<>();
        // 分组
        for (int index = 0; index < 6; index++) {
            var subMap = map.computeIfAbsent(index, k -> new HashMap<>());

            for (var ball : ballList) {
                var key = ball.getSimilarInfo(index);
                var list = subMap.computeIfAbsent(key, t -> new ArrayList<>());
                list.add(ball);
            }
        }
        return map;
    }

    private static PredictOption buildOption(
            PredictOption originOption,
            DoubleColorBall originBall,
            Set<Integer> moveToPreIndexs,
            Set<Integer> keepIndexs) {
        var targetOption = originOption.clone();
        for (int i = 0; i < originBall.getRedValueList().size(); i++) {
            if (moveToPreIndexs.contains(i)) {
                targetOption.setAllow(BallType.RED, i - 1, originBall.getRedValueList().get(i));
            }
            if (keepIndexs.contains(i)) {
                targetOption.setAllow(BallType.RED, i, originBall.getRedValueList().get(i));
            }
        }
        return targetOption;
    }
}
