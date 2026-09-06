package cqwang.doubleball.test.model;

import java.util.*;

public class ModelManager {
    public static class ModelVersion {
        public String name;
        public String version;
        public double accuracy;
        public long createdTime;
        public boolean isActive;

        public ModelVersion(String name, String version, double accuracy) {
            this.name = name;
            this.version = version;
            this.accuracy = accuracy;
            this.createdTime = System.currentTimeMillis();
            this.isActive = false;
        }

        @Override
        public String toString() {
            return String.format("%s v%s (Accuracy: %.2f%%, Active: %s)",
                    name, version, accuracy * 100, isActive);
        }
    }

    private final Map<String, List<ModelVersion>> modelVersions = new HashMap<>();
    private final Map<String, ModelVersion> activeModels = new HashMap<>();
    private final PerformanceMonitor performanceMonitor;

    public ModelManager(String monitorName) {
        this.performanceMonitor = new PerformanceMonitor(monitorName, 0.1);
    }

    public void registerModel(String modelName, String version, double accuracy) {
        ModelVersion mv = new ModelVersion(modelName, version, accuracy);
        modelVersions.computeIfAbsent(modelName, k -> new ArrayList<>()).add(mv);

        if (activeModels.get(modelName) == null || accuracy > activeModels.get(modelName).accuracy) {
            ModelVersion oldActive = activeModels.get(modelName);
            if (oldActive != null) {
                oldActive.isActive = false;
            }
            mv.isActive = true;
            activeModels.put(modelName, mv);
        }
    }

    public ModelVersion getActiveModel(String modelName) {
        return activeModels.get(modelName);
    }

    public List<ModelVersion> getModelHistory(String modelName) {
        return modelVersions.getOrDefault(modelName, new ArrayList<>());
    }

    public void recordPerformance(double redHitRate, double blueHitRate, int profit) {
        performanceMonitor.recordMetric(redHitRate, blueHitRate, profit);
    }

    public boolean shouldRetrainModel() {
        return performanceMonitor.detectDegradation();
    }

    public void printStatus() {
        System.out.println("\n========== Model Manager Status ==========");
        activeModels.forEach((name, version) -> System.out.println("Active: " + version));
        performanceMonitor.printMetrics();
    }
}
