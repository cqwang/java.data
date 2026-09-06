package cqwang.doubleball.test;

import lombok.Data;
import java.util.*;

@Data
public class FeatureVector {
    // 36维特征向量
    private double[] features;
    private int dimension = 36;

    public FeatureVector() {
        this.features = new double[dimension];
        Arrays.fill(features, 0.0);
    }

    /**
     * 从原始数据构建特征向量
     */
    public static FeatureVector buildFromData(List<Integer> data, int ballNumber) {
        FeatureVector fv = new FeatureVector();
        int idx = 0;

        if (data.isEmpty()) return fv;

        // ===== 历史序列特征 (10维) =====
        List<Integer> recent20 = data.subList(Math.max(0, data.size() - 20), data.size());
        List<Integer> recent50 = data.subList(Math.max(0, data.size() - 50), data.size());

        // 1. 最近20期频率
        long freq20 = recent20.stream().filter(x -> x == ballNumber).count();
        fv.features[idx++] = freq20;

        // 2. 最近50期频率
        long freq50 = recent50.stream().filter(x -> x == ballNumber).count();
        fv.features[idx++] = freq50;

        // 3. 全局频率
        long freqAll = data.stream().filter(x -> x == ballNumber).count();
        fv.features[idx++] = freqAll;

        // 4-5. 出现间隔
        double[] gaps = calculateGaps(data, ballNumber);
        fv.features[idx++] = gaps[0]; // 平均间隔
        fv.features[idx++] = gaps[1]; // 间隔标准差

        // 6. 连续出现次数
        fv.features[idx++] = calculateMaxContinuous(data, ballNumber);

        // ===== 统计特征 (8维) =====
        // 7-8. 全局均值和方差
        double mean = data.stream().mapToDouble(Double::valueOf).average().orElse(0);
        fv.features[idx++] = mean;
        fv.features[idx++] = FeatureEngineer.calculateStandardDeviation(data);

        // 9-10. 最近100期的统计
        List<Integer> recent100 = data.subList(Math.max(0, data.size() - 100), data.size());
        double mean100 = recent100.stream().mapToDouble(Double::valueOf).average().orElse(0);
        fv.features[idx++] = mean100;
        fv.features[idx++] = FeatureEngineer.calculateStandardDeviation(recent100);

        // 11. 排名位置 (1-33)
        fv.features[idx++] = ballNumber;

        // 12. 热冷度评分
        double hotScore = (freq20 / Math.max(1, mean)) - 1;
        fv.features[idx++] = hotScore;

        // 13. 相对频率 (与平均比)
        fv.features[idx++] = freqAll > 0 ? freqAll / (double) data.size() : 0;

        // 14. 偏度
        fv.features[idx++] = FeatureEngineer.calculateSkewness(data);

        // ===== 关系特征 (6维) =====
        // 15. 与上期球的距离
        if (!data.isEmpty()) {
            int lastBall = data.get(data.size() - 1);
            fv.features[idx++] = Math.abs(ballNumber - lastBall);
        } else {
            fv.features[idx++] = 0;
        }

        // 16-18. 与其他3个最热球的距离（需传入其他数据）
        for (int i = 0; i < 3; i++) {
            fv.features[idx++] = 0; // 占位符
        }

        // 19. 组合概率（简化为20期内与其他球的共现率）
        double cooccurrence = calculateCooccurrence(data, ballNumber);
        fv.features[idx++] = cooccurrence;

        // ===== 时间特征 (4维) =====
        // 20. 距上次出现的期数
        int gap = calculateLastGap(data, ballNumber);
        fv.features[idx++] = gap;

        // 21-23. 周期、季节、周度编码（简化处理）
        fv.features[idx++] = (data.size() % 7); // 周期编码
        fv.features[idx++] = (data.size() % 52); // 季节编码
        fv.features[idx++] = (data.size() % 365); // 年度编码

        // ===== 领域特征 (8维) =====
        // 24. 尾数分布
        fv.features[idx++] = ballNumber % 10;

        // 25. 奇偶性
        fv.features[idx++] = ballNumber % 2;

        // 26. 大小球分布
        fv.features[idx++] = ballNumber > 16 ? 1 : 0;

        // 27. 质合性
        fv.features[idx++] = isPrime(ballNumber) ? 1 : 0;

        // 28-30. 邻近号相关性
        double neighborsFreq = 0;
        if (ballNumber > 1) neighborsFreq += data.stream().filter(x -> x == ballNumber - 1).count();
        if (ballNumber < 33) neighborsFreq += data.stream().filter(x -> x == ballNumber + 1).count();
        fv.features[idx++] = neighborsFreq;

        // 31-32. 历史组合频率（简化）
        fv.features[idx++] = freqAll > 100 ? 1 : 0; // 高频球标记
        fv.features[idx++] = freqAll < 30 ? 1 : 0; // 低频球标记

        // 33-36. 保留为未来特征
        for (int i = 0; i < 3; i++) {
            fv.features[idx++] = 0;
        }

        return fv;
    }

    /**
     * 计算出现间隔
     */
    private static double[] calculateGaps(List<Integer> data, int ballNumber) {
        List<Integer> gaps = new ArrayList<>();
        int lastPos = -1;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == ballNumber) {
                if (lastPos >= 0) {
                    gaps.add(i - lastPos);
                }
                lastPos = i;
            }
        }

        if (gaps.isEmpty()) {
            return new double[]{data.size(), 0};
        }

        double avg = gaps.stream().mapToDouble(Double::valueOf).average().orElse(0);
        double stdDev = FeatureEngineer.calculateStandardDeviation(gaps);

        return new double[]{avg, stdDev};
    }

    /**
     * 计算最大连续出现次数
     */
    private static int calculateMaxContinuous(List<Integer> data, int ballNumber) {
        int maxContinuous = 0, current = 0;

        for (Integer num : data) {
            if (num == ballNumber) {
                current++;
                maxContinuous = Math.max(maxContinuous, current);
            } else {
                current = 0;
            }
        }

        return maxContinuous;
    }

    /**
     * 计算最后一次出现的距离
     */
    private static int calculateLastGap(List<Integer> data, int ballNumber) {
        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i) == ballNumber) {
                return data.size() - 1 - i;
            }
        }
        return data.size();
    }

    /**
     * 计算共现率
     */
    private static double calculateCooccurrence(List<Integer> data, int ballNumber) {
        if (data.size() < 2) return 0;

        int cooccur = 0, total = 0;

        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) == ballNumber) {
                total++;
                if (data.get(i + 1) != ballNumber) {
                    cooccur++;
                }
            }
        }

        return total > 0 ? (double) cooccur / total : 0;
    }

    /**
     * 检查是否为质数
     */
    private static boolean isPrime(int num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;

        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) return false;
        }

        return true;
    }

    /**
     * 归一化特征向量
     */
    public void normalize() {
        double min = Arrays.stream(features).min().orElse(0);
        double max = Arrays.stream(features).max().orElse(1);
        double range = max - min;

        if (range == 0) {
            Arrays.fill(features, 0.5);
            return;
        }

        for (int i = 0; i < features.length; i++) {
            features[i] = (features[i] - min) / range;
        }
    }

    /**
     * 标准化特征向量
     */
    public void standardize() {
        double mean = Arrays.stream(features).average().orElse(0);
        double variance = Arrays.stream(features)
            .map(x -> Math.pow(x - mean, 2))
            .average()
            .orElse(0);
        double stdDev = Math.sqrt(variance);

        if (stdDev == 0) {
            Arrays.fill(features, 0);
            return;
        }

        for (int i = 0; i < features.length; i++) {
            features[i] = (features[i] - mean) / stdDev;
        }
    }

    /**
     * 获取特征向量的字符串表示
     */
    @Override
    public String toString() {
        return "FeatureVector{" +
            "dim=" + dimension +
            ", features=" + Arrays.toString(features) +
            '}';
    }
}
