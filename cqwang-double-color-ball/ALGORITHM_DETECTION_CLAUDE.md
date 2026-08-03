
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

-----------------------------------------------

## 优化

### predictBlue优化
提供PredictionAlgorithm的更多实现类，
目标是：将算法传入BlueAlgorithmSelector.calculateHistoryPredictValueSum方法，尽可能提高方法返回值

### predict新增算法
提供PredictionAlgorithm的更多实现类，对每一个实现类algorithm
1.新增的算法，不能在注册表AlgorithmPoolFactory.ALGORITHMS中已经存在
2.algorithm类的定义放在cqwang.doubleball.algorithm.detection.impl.temp包对应的目录下
3.执行new SingleAlgorithmSelector().calculateHistoryPredictValueSum(algorithm)方法，传入设计的算法实现类
4.目标是：执行后，要求algorithm.historyPredictValueSum>5000，若满足则将算法添加到注册表AlgorithmPoolFactory.ALGORITHMS中

### relevance算法
在cqwang.doubleball.algorithm.relevance.impl包下，提供RelevanceAlgorithm的实现类，要求实现predictBlue(List<Integer> predictedRedValueList, List<VirtualDoubleColorBallItem> sampleList)方法:
1.入参sampleList为样本数据列表，每个样本VirtualDoubleColorBallItem.ballValueList包含7位数值
2.入参predictedRedValueList为已经预测的前6位数值
3.方法的功能：基于样本数据列表中每个ballValueList的前6位数值和第7位数值的特征，预测predictedRedValueList对应的第7位数值，
4.算法不要修改样本数据
5.将算法实现类，注册到AlgorithmPoolFactory.RELEVANCE_ALGORITHMS中
6.VirtualDoubleColorBallItem.ballValueList中，前6为数值的区间都是[1,33],第7位数值的区间是[34,49]
7.方法如果预测失败，返回-1。如果预测成功，返回的数值区间是[34,49]