package cqwang.doubleball.test;

import java.util.*;

public class HyperparameterOptimizer {
    public static class SearchSpace {
        private final String name;
        private final List<Double> values;

        public SearchSpace(String name, List<Double> values) {
            this.name = name;
            this.values = values;
        }

        public String getName() {
            return name;
        }

        public List<Double> getValues() {
            return values;
        }
    }

    public static class HyperparameterConfig {
        private final Map<String, Double> params = new HashMap<>();

        public void set(String name, double value) {
            params.put(name, value);
        }

        public double get(String name, double defaultValue) {
            return params.getOrDefault(name, defaultValue);
        }

        public Map<String, Double> getParams() {
            return new HashMap<>(params);
        }

        @Override
        public String toString() {
            return "Config{" + params + '}';
        }
    }

    public static class OptimizationResult {
        public HyperparameterConfig config;
        public double score;
        public int iteration;

        public OptimizationResult(HyperparameterConfig config, double score, int iteration) {
            this.config = config;
            this.score = score;
            this.iteration = iteration;
        }

        @Override
        public String toString() {
            return String.format("Iteration %d: Score=%.4f", iteration, score);
        }
    }

    public static List<HyperparameterConfig> gridSearch(List<SearchSpace> searchSpaces) {
        List<HyperparameterConfig> configs = new ArrayList<>();

        if (searchSpaces.isEmpty()) {
            configs.add(new HyperparameterConfig());
            return configs;
        }

        generateConfigs(searchSpaces, 0, new HyperparameterConfig(), configs);
        return configs;
    }

    private static void generateConfigs(
            List<SearchSpace> spaces,
            int index,
            HyperparameterConfig current,
            List<HyperparameterConfig> result) {
        if (index == spaces.size()) {
            result.add(new HyperparameterConfig() {{
                current.getParams().forEach(this::set);
            }});
            return;
        }

        SearchSpace space = spaces.get(index);
        for (double value : space.getValues()) {
            HyperparameterConfig next = new HyperparameterConfig() {{
                current.getParams().forEach(this::set);
                set(space.getName(), value);
            }};
            generateConfigs(spaces, index + 1, next, result);
        }
    }
}
