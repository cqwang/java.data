package cqwang.doubleball.advancedalgorithm;

import com.fasterxml.jackson.core.type.TypeReference;
import cqwang.data.serializer.FileProvider;

import java.util.List;

public class AdvancedAlgorithmPoolFactory {
    private static List<AdvancedAlgorithmRegistry> ALGORITHMS;

    static {
        ALGORITHMS = readFromFile();
    }

    private static List<AdvancedAlgorithmRegistry> readFromFile() {
        return FileProvider.readFile("/AdvancedAlgorithm.json", new TypeReference<List<AdvancedAlgorithmRegistry>>() {
        });
    }

    public static List<AdvancedAlgorithmRegistry> getAlgorithmPool() {
        return ALGORITHMS;
    }
}
