
## 算法探测

### 算法池
极端梯度提升树
轻量梯度提升树
随机森林
长短期记忆网络
样本数据的区间分布
概率分布
相似度加权预测
多周期频率预测
其他AI推荐的适合随机数的算法


### 提供PredictionAlgorithm的实现类
从“算法池”中选取一种算法，实现接口PredictionAlgorithm

1.predictRed方法：
功能描述：基于时间有序的历史样本数据redBallDataDetail.dataList和每个数据出现的频率redBallDataDetail.dataFrequencyMap，以及数值区间限制redRange（包含左边界、包含右边界），来生成一个预测数值。
每个实现类的算法，都要在严格遵守此“功能描述”的约束前提下，实现自己差异化的预测数值生成策略。

2.predictBlue方法：
功能描述：基于时间有序的历史样本数据blueBallDataDetail.dataList和每个数据出现的频率blueBallDataDetail.dataFrequencyMap，以及数值区间限制redRange（包含左边界、包含右边界），来生成一个预测数值。
每个实现类的算法，都要在严格遵守此“功能描述”的约束前提下，实现自己差异化的预测数值生成策略。

3.算法不要改变入参redBallDataDetail.dataList和blueBallDataDetail.dataList的原始顺序

4.代码目录：algorithm/impl/
5.将实现类注册到AlgorithmPoolFactory类的ALGORITHMS字段中，格式参考new AlgorithmRegistry("XGBoost", XGBoostAlgorithm.class)


### 补充说明
要求提供 25种PredictionAlgorithm的实现类，每种实现类对应“算法池”中的一种算法，不可重复。

## TODO 优化
1.找下篮球匹配最高的算法
1.红蓝不同规则
2.既要分开预测红蓝，又要一起预测。通过历史红蓝规则，提高算法收益
3.价值增长曲线

