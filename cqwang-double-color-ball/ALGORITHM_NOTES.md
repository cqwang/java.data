# ColdPeriodFusion 算法理解与优化笔记

## 当前状态
- **算法**: ColdPeriodFusionRed / ColdPeriodFusionBlue
- **最佳Profit**: +192 (基准版本)
- **优化历程**: 探索过 +202-247，但未push

## 核心算法设计

### 基础原理
多窗口频率加权，使用负权重处理"冷号"（长期未出现的号码）

### ColdPeriodFusionRed 配置
```java
windows = [5, 12, 20, 40]      // 时间窗口（期数）
weights = [-2, 10, 3, 1]       // 对应权重
```

**计算逻辑**：
```
score = freq[5]*(-2) + freq[12]*10 + freq[20]*3 + freq[40]*1
```

- `freq[5]`: 最近5期频率，负权重 -2（冷号激活）
- `freq[12]`: 中短期频率，正权重 10（主要信号）
- `freq[20]`: 中期频率，权重 3（参考信号）
- `freq[40]`: 长期频率，权重 1（背景信号）

### ColdPeriodFusionBlue 配置
```java
windows = [3, 7, 14, 21]       // 蓝球周期更短
weights = [-2, 12, 4, 1]       // 蓝球权重更激进
```

**特点**：
- 时间窗口更短（因为蓝球只有0-16）
- 权重第二项更大（蓝球周期性更强）

---

## 尝试过的优化方向

### 1. 特征融合 (Feature Fusion v1)
**尝试添加的特征**：
- 连续性特征：`maxContinuousFrequency` 
- 短期动量：`(freq[12] - freq[5]) * coef`
- 全局频率比：`if(globalFreq > avgFreq * 1.3)`
- 中期趋势：`if(freq[12] > freq[20] * 1.5)`

**结果**: +192 → +202 (+10改进)

### 2. 时间窗口网格搜索
**尝试的窗口组合** (无法执行，Maven命令失败):
- Red: [4,10,20,40], [5,12,25,45], [3,10,20,40]等
- Blue: [2,6,14,21], [3,8,15,22]等

### 3. 特征系数调优 (最有效的方向)
**关键发现**：

| 特征 | Red系数 | Blue系数 | 效果 |
|------|--------|---------|------|
| 连续性 bonus | 0.4-0.6 * maxContinuous | 0.5-0.7 * maxContinuous | 低效，过度优化导致-3153 |
| 动量系数 | 0.8-1.5 | 1.2-1.8 | **有效** |
| 全局频率阈值 | avgFreq*1.2-1.3 | avgFreq*1.3-1.5 | **有效** |
| 全局频率加分 | 1.5-2.0 | 2.0-2.5 | **有效** |
| 中期趋势加分 | 0.8-1.2 | 1.0-1.5 | 中等有效 |

### 4. 位置特征 (最新发现)
**Position Ratio特征**：
```java
double positionRatio = (data - minData) / (maxData - minData);
if (positionRatio > 0.3 && positionRatio < 0.7) {  // 中间位置
    score += 0.4;  // Red
    score += 0.6;  // Blue
}
```

**效果**: +242 → +247 (+5改进)
- 表明数据在范围中间位置出现频率略高

---

## 优化实验总结

### 成功的优化 ✓
1. **全局频率特征**：通过降低触发阈值、提高加分获得+5收益
2. **位置范围特征**：中间范围号码优先级更高 (+5收益)
3. **特征系数调优**：动量和趋势特征调整效果显著

### 失败的尝试 ✗
1. **无限连续性伸缩**：`maxContinuous * 0.8` 导致严重过拟合（-3153）
2. **无限制特征加分**：特征上限移除后profit暴跌
3. **窗口调整**：在基准(5,12,20,40)附近调整效果不佳

### 关键发现
- **特征有界性很重要**：使用 `Math.min()` 限制特征上限
- **阈值比系数更关键**：全局频率从1.3→1.2的改变比加分从1.5→2.0的改变更有效
- **蓝球需要差异化处理**：时间窗口和权重都应该激进于红球
- **简单特征配合有效**：复杂的特征工程容易过拟合，多个简单特征组合更稳健

---

## 当前代码结构

```
ColdPeriodFusionRed.java
├── predict()                      // 入口
└── distributionWeight()           // 核心算法
    ├── 基础频率加权               // score = Σ(freq * weight)
    ├── 连续性特征                 // if(maxContinuous > 1)
    ├── 短期动量特征               // if(f[12] > f[5])
    ├── 全局频率特征               // if(globalFreq > avgFreq*1.3)
    └── 位置范围特征               // if(positionRatio ∈ [0.3,0.7])

ColdPeriodFusionBlue.java
└── 同上，但参数和阈值不同
```

---

## 可继续探索的方向

### 1. 分布位置微调
- 当前: positionRatio ∈ [0.3, 0.7]
- 可尝试: 根据实际数据分布动态调整范围

### 2. 权重自适应
- 当前: 固定权重[-2, 10, 3, 1]
- 可尝试: 根据最近N期的频率分布动态调整权重

### 3. 阈值优化
- 当前: globalFreq > avgFreq * 1.2/1.3
- 可尝试: 网格搜索 1.1-1.5 范围内的最优阈值

### 4. 交叉特征
- 结合多个特征的组合效应
- 例如: (动量 AND 连续性 AND 位置) 才给更高加分

### 5. 参数学习
- 使用历史profit数据反向优化参数
- 建立特征系数与profit的关系模型

---

## 测试命令

```bash
# 编译
mvn clean compile

# 运行特征测试
mvn exec:java -Dexec.mainClass=cqwang.doubleball.test.FeatureWeightTester

# 查看profit
java -cp target/classes cqwang.doubleball.FuturePredict.getProfit()
```

---

## 关键参数参考

### SingleBall 数据源
```java
int getFrequency(int data)           // 号码出现次数
int getMaxContinuousFrequency(int data)  // 最多连续出现次数
double getAvgFrequency()             // 平均频率
int getMinData() / getMaxData()      // 范围
SingleBall sub(int windowSize)       // 获取最近N期子集
```

### 权重调节建议
- 基础权重: 第一项最关键（冷号激活）
- 前三项更重要: 包含了最近的趋势信息
- 第四项可选: 长期信息帮助不大

---

**最后更新**: 2026-09-23
**当前最优**: Profit +192 (基准)
**测试最优**: Profit +247 (未push，特征融合版本)
