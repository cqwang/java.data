# 双色球预测系统 - 代码架构与算法理解文档

**最后扫描时间**: 2026-09-23  
**当前commit**: 2d3377a (HEAD)  
**当前状态**: 基准实现，无ColdPeriodFusion系列算法

---

## 一、系统架构概览

### 核心模块结构

```
cqwang.doubleball
├── detection
│   ├── algorithm               # 算法实现
│   │   ├── singleball         # 单球预测算法
│   │   │   ├── SingleBallAlgorithm (接口)
│   │   │   └── impl           # 算法实现类 (15+种)
│   │   │       ├── RedRecommend
│   │   │       ├── BlueRecommend
│   │   │       ├── MaxFrequency
│   │   │       ├── SurgeFrequency
│   │   │       ├── ContinuityWeightFrequency
│   │   │       └── 其他...
│   │   └── doublecolorball    # 双球综合算法
│   ├── model                   # 数据模型
│   │   ├── data               # SingleBall, DoubleColorBall 等
│   │   ├── result             # PredictResult, PredictPointValue 等
│   │   └── option             # PredictOption
│   ├── cache                   # 缓存管理
│   └── utils                   # 工具类（AlgorithmUtils等）
├── FuturePredict               # 预测入口
├── App                         # 应用程序入口
└── spider                      # 数据爬虫模块
```

---

## 二、核心数据结构

### SingleBall 数据模型
**位置**: `detection.model.data.SingleBall.java`

#### 数据成员
```java
BallType ballType           // RED 或 BLUE
Integer index               // 位序（1-6）
List<Integer> dataList      // 历史数据序列（时间有序）
Map<Integer, DataFrequency> dataFrequencyMap  // 号码 -> 出现次数
List<DataFrequency> sortedList  // 按频率排序（从低到高）
int maxData                 // 范围上界
int minData                 // 范围下界
int totalFrequency          // 总出现次数
double avgFrequency         // 平均频率
```

#### 关键方法
```java
public int getFrequency(Integer data)           // 获取号码出现次数
public int getMaxContinuousFrequency(Integer data)  // 获取连续出现最多次
public SingleBall sub(int sampleSize)           // 获取最近N期子集（缓存）
public double toAvgFrequencyRatio(Integer data) // 频率与平均值比值
public void completeFill()                      // 完成数据填充
```

#### 数据特征
- **频率**：raw count
- **连续性**：maxContinuousFrequency
- **分布**：sortedList 按频率排序
- **范围**：minData-maxData
- **时间序列**：dataList 保留顺序

---

## 三、现有算法分类

### 1. 多窗口加权频率类

#### RedRecommend
**文件**: `singleball/impl/RedRecommend.java`

```java
// 核心参数
windows = [5, 12, 20, 40]       // 时间窗口
weights = [-2, 10, 3, 1]        // 对应权重

// 计算逻辑
score = freq[5]*(-2) + freq[12]*10 + freq[20]*3 + freq[40]*1

// 解释
- freq[5]:  最近5期，权重-2  → 冷号激活（长期未出，最近出现则高分）
- freq[12]: 中短期，权重10   → 主要信号源
- freq[20]: 中期，权重3      → 参考信号
- freq[40]: 长期，权重1      → 背景
```

**特点**：
- 使用负权重激活冷号（反向思维）
- 多时间尺度融合
- 简洁高效

#### BlueRecommend
**文件**: `singleball/impl/BlueRecommend.java`

```java
// 使用 SurgeFrequency 算法
// 检测频率突跃：最近频率相比整体频率的提升比例

if (sub30.getFrequency(i) == 0) continue;
if (!becomeHot(...)) continue;  // 热度判断

double surgeRatio = subBall.getFrequency(i) * 2.0 / midBall.getFrequency(i);
score = midBall.getFrequency(i) * (1.0 + Math.min(surgeRatio, 2.0) * 0.5);
```

**特点**：
- 检测频率突跃（热度转变）
- 蓝球周期性强，这个算法更契合

### 2. 频率特征类

#### MaxFrequency
简单选择总体频率最高的号码

#### ContinuityWeightFrequency
```java
score = frequency * 1.0 + maxContinuousFrequency * 3.0
// 连续性权重为3倍
```

#### SurgeFrequency
检测短期频率相比中期频率的提升

### 3. 分布特征类

#### DistributionFrequency
多维度条件过滤（全局热、长期热、中期稳、短期冷）

#### NeighborhoodCluster
计算邻域强度：周围号码的聚集效应

#### SimilarityFrequency
结合频率、邻近性和全局频率

### 4. 其他算法

- **RecentMaxFrequency**: 短期频率最高
- **RecentMaxWeightFrequency**: 短期加权频率
- **SvmSimulation**: SVM模拟
- **MaxDistributionCumulativeWeightFrequency**: 累积加权
- **MaxDistributionSplitWeightFrequency**: 差分加权

---

## 四、AlgorithmUtils 工具类详解

**文件**: `utils/AlgorithmUtils.java`

### 核心方法

#### 1. 多窗口加权计算
```java
public static SingleResult distributionWeight(
    SingleBall singleBall,
    Range<Integer> range,
    boolean isCumulativeWeight,
    PredictOption option)

// isCumulativeWeight=true: 累积频率
//   score = freq[12]*7 + freq[20]*3 + freq[40]*1
// isCumulativeWeight=false: 差分频率（new entries only）
//   freq[12] 直接 * 10
//   (freq[20] - freq[12]) * 4  
//   (freq[40] - freq[20]) * 1
```

#### 2. 热冷转变检测
```java
becomeHot(longBall, recentBall, data, factor)
// recentBall频率(按factor扩展后) > longBall频率*factor

becomeCold(longBall, recentBall, data)
// recentBall频率(扩展后) < longBall频率
```

#### 3. 统计方法
```java
median(List<Integer> dataList)      // 中位数
weightedAverage(List<Integer> dataList)  // 加权平均
standardDeviationWeighted()         // 标准差加权
```

---

## 五、评估机制

### Profit 计算方式

**文件**: `model/result/PredictResult.java`

```java
public int getProfit() {
    return sumValue - sumCost;
}

// sumValue: 所有预测命中的奖金总和
// sumCost: 每次预测成本 = 预测次数 × 2

// 例如：
// 预测100次，其中50次命中，平均奖金10
// sumValue = 50 × 10 = 500
// sumCost = 100 × 2 = 200
// profit = 500 - 200 = 300
```

### 评估指标

```
hitTotalCount      - 总命中次数
hitRedTotalCount   - 红球命中次数
hitBlueTotalCount  - 蓝球命中次数
maxValue           - 单次最高奖金
recentProfit(N)    - 最近N期的profit
```

### 性能分析工具

**AlgorithmPerformanceAnalyzer**: 对所有算法进行交叉测试
```
测试范围: 最近1000期 (或全部数据最近部分)
计算: 红球命中率、蓝球命中率、总profit
排序: 按profit降序
```

---

## 六、算法选择机制

### SingleBallAlgorithmFactory

管理所有单球算法的注册和查询：
```java
SingleBallAlgorithmFactory.getAlgorithmPool()  // 获取所有算法
SingleBallAlgorithmFactory.getAlgorithm("RedRecommend")  // 获取特定算法
```

### DoubleColorAlgorithmRegistry

组合红球和蓝球算法：
```java
new DoubleColorAlgorithmRegistry(blueAlgorithm, redAlgorithm)
// 为单次预测组合两个算法
```

---

## 七、关键设计模式

### 1. 时间窗口模式
```java
SingleBall sub(int sampleSize)
// 创建最近N期数据的视图（缓存）
// 避免重复计算，O(1) 查询
```

### 2. 权重组合模式
```java
// 不同权重方案对应不同算法特性
weights = [-2, 10, 3, 1]      // 冷号激活型
weights = [7, 3, 1]           // 累积型
```

### 3. 特征融合模式
```java
// 多个判断条件组合，提高精度
if (condition1) continue;      // 过滤
if (condition2) continue;
score = base_score + bonus1 + bonus2
```

---

## 八、系统运行流程

```
1. 初始化数据
   ↓
   DoubleColorBallPreload.execute()
   加载历史数据，初始化所有SingleBall实例

2. 单球预测
   ↓
   对每个位置(1-6红球 + 1个蓝球)调用算法
   SingleBallAlgorithm.predict(singleBall, option)
   返回 SingleResult(value, success)

3. 综合预测
   ↓
   DoubleColorAlgorithmRegistry.predict(targetIndex)
   返回 PredictResult

4. 性能评估
   ↓
   计算 profit = sumValue - sumCost
   排序、比较算法性能
```

---

## 九、数据流示例

### 红球位置1的预测过程

```java
// 原始数据（最近40期）
dataList = [5, 12, 8, 15, 3, ..., 22]

// 创建时间窗口
sub5 = 最近5期 [8, 15, 3, 20, 22]
sub12 = 最近12期 [...]
sub20 = 最近20期 [...]
sub40 = 全部40期

// 对候选号码（1-33）计算分数
data=1: freq[5]=0, freq[12]=1, freq[20]=3, freq[40]=5
        score = 0*(-2) + 1*10 + 3*3 + 5*1 = 24

data=8: freq[5]=2, freq[12]=4, freq[20]=6, freq[40]=8
        score = 2*(-2) + 4*10 + 6*3 + 8*1 = 58

// 选择最高分的号码
max_score = 58, result = 8
```

---

## 十、优化方向

### 已验证的有效方向
1. **时间窗口调整** - 根据球的周期性调整窗口
2. **权重系数优化** - 通过网格搜索找到最佳权重比
3. **特征融合** - 多维度特征组合提高准确度
4. **阈值调整** - 全局频率、连续性等的判断阈值

### 潜在优化方向
1. **自适应权重** - 根据最近的热冷转变动态调整
2. **集成算法** - 多算法投票或加权组合
3. **ML方法** - 使用RandomForest、SVM等
4. **交叉验证** - 时间序列分割验证模型稳定性

---

## 十一、文件快速导航

| 功能 | 文件位置 | 说明 |
|------|--------|------|
| 单球算法接口 | `algorithm/singleball/SingleBallAlgorithm.java` | 定义接口 |
| 红球推荐 | `algorithm/singleball/impl/RedRecommend.java` | 当前最优(Profit+3342) |
| 蓝球推荐 | `algorithm/singleball/impl/BlueRecommend.java` | 突跃检测算法 |
| 数据模型 | `model/data/SingleBall.java` | 核心数据结构 |
| 工具函数 | `utils/AlgorithmUtils.java` | 通用算法工具 |
| 结果评估 | `model/result/PredictResult.java` | Profit计算 |
| 性能分析 | `test/AlgorithmPerformanceAnalyzer.java` | 交叉测试 |
| 入口函数 | `FuturePredict.java` | 预测启动 |

---

## 十二、当前基准性能

### RedRecommend 算法
```
窗口: [5, 12, 20, 40]
权重: [-2, 10, 3, 1]
Profit: +3342 (基准)
特点: 多窗口冷号激活
```

### BlueRecommend 算法
```
使用: SurgeFrequency
检测: 频率突跃
Profit: 与红球组合后的总profit
特点: 蓝球周期规律强
```

---

## 十三、创建新算法的步骤

1. **实现接口**
```java
public class MyAlgorithm implements SingleBallAlgorithm {
    @Override
    public SingleResult predict(SingleBall singleBall, PredictOption option) {
        // 实现预测逻辑
    }
}
```

2. **注册到工厂**
```java
// 在 SingleBallAlgorithmFactory 中添加
algorithmList.add(new SingleBallAlgorithmRegistry("MyAlgorithm", MyAlgorithm.class));
```

3. **测试评估**
```java
// 使用 AlgorithmPerformanceAnalyzer 进行交叉测试
```

---

## 附录：核心代码片段

### RedRecommend 核心逻辑
```java
var subList = new SingleBall[]{
    singleBall.sub(5),   // 最近5期
    singleBall.sub(12),  // 最近12期
    singleBall.sub(20),  // 最近20期
    singleBall.sub(40)   // 最近40期
};
var weightList = new double[]{-2, 10, 3, 1};

double maxScore = 0;
int result = range.getMinimum();

for (int data = range.getMinimum(); data <= range.getMaximum(); data++) {
    var score = 0.0;
    for (var index = 0; index < subList.length; index++) {
        score += subList[index].getFrequency(data) * weightList[index];
    }
    if (score > maxScore) {
        maxScore = score;
        result = data;
    }
}
return new SingleResult(result, success);
```

### Profit 计算逻辑
```java
// PredictResult.java
public int getProfit() {
    int sumValue = 0;     // 命中的奖金总和
    int sumCost = 0;      // 预测成本（每次2元）
    
    for (PredictPointValue point : predictPointList) {
        sumValue += point.getValue();
    }
    sumCost = totalTests * 2;
    
    return sumValue - sumCost;
}
```

---

**文档完成度**: 100%
**涵盖范围**: 架构、数据模型、算法、评估、优化方向
**适用对象**: 开发者、优化工程师
