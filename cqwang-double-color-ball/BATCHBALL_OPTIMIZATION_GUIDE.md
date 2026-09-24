# BatchBall 算法优化指南

**最后更新**: 2026-09-24  
**总算法数**: 14 个  
**优化工具**: BatchBallPerformanceAnalyzer

---

## 概览

本指南介绍如何使用性能分析工具来评估和优化 BatchBall 算法的收益。

## 工具使用

### 1. 运行性能分析

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=cqwang.doubleball.test.BatchBallPerformanceAnalyzer
```

**输出内容**：
- 所有算法组合的 Profit 值
- Top 20 最高收益组合
- Bottom 10 最低收益组合
- 按 BatchBall 算法统计的平均收益
- 按蓝球算法统计的平均收益
- 总体统计（平均、最大、最小）

### 2. 性能分析示例输出

```
Top 20 Performance:
1. BatchEnsembleOptimized + BlueRecommend = 3500
2. BatchRedRecommendOptimized + BlueRecommend = 3400
3. BatchHotColdTransition + BlueRecommend = 3300
...

Average Profit by BatchBall Algorithm:
BatchEnsembleOptimized           | Avg:   3300 | Max:   3500 | Min:   2800 | Tests: 3
BatchRedRecommendOptimized      | Avg:   3200 | Max:   3400 | Min:   2700 | Tests: 3
BatchHotColdTransition          | Avg:   3100 | Max:   3300 | Min:   2600 | Tests: 3
...
```

---

## 算法分类

### 基础算法（第一代，11个）

| 类别 | 算法名称 | 特征 |
|------|--------|------|
| **频率类** | BatchMaxFrequency | 简单频率最高 |
| | BatchRecentMaxFrequency | 近期频率最高 |
| | BatchRecentMaxWeightFrequency | 加权众数 |
| **多窗口加权** | BatchRedRecommend | 经典多窗口 [-2,10,3,1] |
| | BatchMaxDistributionCumulativeWeightFrequency | 累积加权 |
| | BatchMaxDistributionSplitWeightFrequency | 差分加权 |
| **特征融合** | BatchContinuityWeight | 连续性加权 |
| | BatchSurgeFrequency | 频率突跃检测 |
| | BatchNeighborhoodCluster | 邻域聚集 |
| **分析类** | BatchSimilarityFrequency | 多维相似度 |
| | BatchDistributionFrequency | 多条件过滤 |

### 优化算法（第二代，3个）

| 算法名称 | 改进点 | 预期收益提升 |
|---------|------|-----------|
| **BatchRedRecommendOptimized** | 权重调整 + 特征融合 | +5-10% |
| **BatchEnsembleOptimized** | 多算法投票（4重） | +10-15% |
| **BatchHotColdTransition** | 热度转变 + 全局热度 | +8-12% |

---

## 优化策略

### 策略1: 调整权重系数

**原理**: 不同时间窗口对预测的影响不同

**调整方向**:
```java
// 原始权重
weights = [-2, 10, 3, 1]

// 优化方向1: 增加中短期权重
weights = [-2, 11, 3.5, 1]

// 优化方向2: 降低长期权重
weights = [-2, 10, 3, 0.5]
```

**如何实现**: 修改算法中的权重列表，运行性能分析器对比结果

### 策略2: 特征融合

**核心思想**: 单一特征容易过拟合，多特征融合提高稳定性

**常用特征**:
- 基础频率
- 连续性 (maxContinuousFrequency)
- 全局热度 (globalFreq vs avgFreq)
- 邻域效应 (周围号码的聚集度)
- 趋势变化 (频率的上升/下降)

**实现方式**: 
```java
score = baseScore 
      + continuityBonus
      + globalHeatBonus
      + trendBonus
```

### 策略3: 时间窗口优化

**关键参数**:
```java
// 多窗口配置
windows = [5, 12, 20, 40]  // 红球基础
windows = [3, 7, 14, 21]   // 蓝球（周期短）

// 调整方向
// 1. 更紧凑: [4, 10, 20, 35]
// 2. 更宽松: [6, 15, 25, 45]
// 3. 延长长期: [5, 12, 20, 50]
```

### 策略4: 集成/投票算法

**优势**: 
- 降低单个算法的过拟合
- 提高鲁棒性
- 平衡不同的评估标准

**实现**:
```java
score = algo1_score * 0.4 
      + algo2_score * 0.3
      + algo3_score * 0.2
      + algo4_score * 0.1
```

---

## 优化流程

### 第1步: 基准测试
```bash
mvn exec:java -Dexec.mainClass=cqwang.doubleball.test.BatchBallPerformanceAnalyzer
# 记录所有算法的 Profit 和平均值
```

### 第2步: 识别问题
- 找出 Profit 最低的算法
- 分析其特征（权重、窗口、特征）
- 比较与表现最好算法的差异

### 第3步: 尝试改进
选择一个表现不佳的算法，创建优化版本：

```java
// 例如：优化 BatchMaxFrequency
public class BatchMaxFrequencyOptimized implements BatchBallAlgorithm {
    // 修改权重系数或特征
    // 添加连续性考虑
    // 添加全局热度考虑
}
```

### 第4步: 验证改进
```bash
# 在 BatchBallAlgorithmFactory 中注册新算法
# 运行性能分析
# 对比新旧算法的 Profit
```

### 第5步: 迭代
- 如果改进效果好（+5% 以上），合并
- 如果效果一般，调整参数后重试
- 如果没有改进，尝试不同的策略

---

## 快速参考

### 快速创建优化算法的模板

```java
public class Batch<YourAlgorithm>Optimized implements BatchBallAlgorithm {
    @Override
    public BatchResult predict(BatchBall batchBall, BatchPredictOption option) {
        Range<Integer> range = Range.between(
            batchBall.getMinData(), 
            batchBall.getMaxData()
        );
        return batchPredict(batchBall, range, option);
    }

    private static BatchResult batchPredict(BatchBall batchBall, 
                                           Range<Integer> range, 
                                           BatchPredictOption option) {
        List<Map.Entry<Integer, Double>> scoreList = new ArrayList<>();

        for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
            if (option.isBlock(data)) continue;

            // 计算评分
            double score = calculateScore(batchBall, data);
            scoreList.add(new AbstractMap.SimpleEntry<>(data, score));
        }

        // 排序并选出前6个
        scoreList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : scoreList) {
            if (result.size() >= 6) break;
            result.add(entry.getKey());
        }

        Collections.sort(result);
        return new BatchResult(result, result.size() == 6);
    }

    private static double calculateScore(BatchBall batchBall, int data) {
        double score = 0.0;
        
        // 添加您的计算逻辑
        score += batchBall.getFrequency(data);
        
        return score;
    }
}
```

### 注册步骤
```java
// 在 BatchBallAlgorithmFactory 中添加
new BatchBallAlgorithmRegistry(
    "BatchYourAlgorithmOptimized", 
    BatchYourAlgorithmOptimized.class
)
```

---

## 当前最优算法

### 实验结果（预计）

基于算法设计，预计排名：
1. **BatchEnsembleOptimized** - 集成4种策略，最稳定
2. **BatchRedRecommendOptimized** - 权重优化+特征融合
3. **BatchHotColdTransition** - 热度转变检测
4. **BatchRedRecommend** - 经典多窗口
5. **BatchSurgeFrequency** - 突跃检测

### 性能指标目标

| 指标 | 目标 | 依据 |
|------|------|------|
| 平均 Profit | > 3000 | SingleBall 基准 |
| 最大 Profit | > 3500 | 历史最高记录 |
| 最小 Profit | > 2000 | 可接受下限 |
| 标准差 | < 500 | 稳定性要求 |

---

## 进阶优化

### 方向1: 自适应权重
根据最近的表现动态调整权重
```java
// 检测最近20期的频率分布
// 如果最近期频率高，增加短期权重
// 如果有上升趋势，增加突跃检测权重
```

### 方向2: 机器学习
使用历史数据训练模型预测号码
```java
// 特征：频率、连续性、热度、趋势等
// 目标：预测能命中的号码
// 模型：RandomForest、SVM、Neural Network
```

### 方向3: 多目标优化
不只优化 Profit，还考虑：
- 命中率 (Hit Rate)
- 单次收益 (Average Win)
- 稳定性 (Profit Variance)
- 风险 (Worst Case)

---

## 常见问题

**Q: 为什么集成算法不一定最好？**
A: 如果单个算法质量不高，集成效果有限。需要先优化基础算法。

**Q: 如何快速找到最优权重？**
A: 使用网格搜索或贝叶斯优化。但对于 4 个权重，搜索空间很大，可能需要优化策略。

**Q: 新算法要测试多久才能确定有效？**
A: 至少 100+ 期数据。最好 500+ 期以验证长期稳定性。

**Q: 能否同时优化时间窗口和权重？**
A: 可以，但搜索空间会爆炸增长。建议先固定一个，优化另一个。

---

**下一步**: 运行 `BatchBallPerformanceAnalyzer`，获取基准数据，然后按上述流程逐步优化。
