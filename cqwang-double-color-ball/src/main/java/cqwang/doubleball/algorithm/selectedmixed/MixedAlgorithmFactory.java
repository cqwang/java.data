package cqwang.doubleball.algorithm.selectedmixed;

import cqwang.doubleball.algorithm.select.SelectMode;
import cqwang.doubleball.algorithm.select.impl.BlueAlgorithmSelector;
import cqwang.doubleball.algorithm.select.impl.SingleAlgorithmSelector;

import java.util.ArrayList;
import java.util.List;

public class MixedAlgorithmFactory {
    private static final List<MixedAlgorithm> ALGORITHMS = new ArrayList<>();
    static {
        var redAlgorithmRegistry = new SingleAlgorithmSelector().execute(SelectMode.FROM_FILE);
        var blueAlgorithmRegistry = new BlueAlgorithmSelector().execute(SelectMode.FROM_FILE);
        for (var red : redAlgorithmRegistry) {
            for (var blue : blueAlgorithmRegistry) {
                if (red.getName().equals(blue.getName())) {
                    continue;
                }

                var mixedAlgorithm = new MixedAlgorithm(blue.getInstance(), red.getInstance());
                ALGORITHMS.add(mixedAlgorithm);
            }
        }
    }

    public static List<MixedAlgorithm> getAlgorithmPool() {
        return ALGORITHMS;
    }
}
