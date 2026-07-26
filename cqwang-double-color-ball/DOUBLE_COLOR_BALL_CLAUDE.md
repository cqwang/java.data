## 数据定义

### 全量数据，注意：这个数据序列的排序问题是对的，不要瞎搞
1.通过反序列化获取所有数据：
List<DoubleColorBallItem> originalData = FileProvider.readFile("/DoubleColorBallData.json", new TypeReference<List<DoubleColorBallItem>>() { });

2.取originalData位序为[0,99]的所有元素，逐个添加到latestOriginalData列表中
3.定义allData = originalData.reversed(), 元素按照DoubleColorBallItem.date日期字段正序
4.定义latestData = latestOriginalData.reversed()，元素按照DoubleColorBallItem.date日期字段正序

5.取originalData位序为[0,199]的所有元素，逐个添加到verifyOriginalData列表中
6.定义verifyData = verifyOriginalData.reversed(),元素按照DoubleColorBallItem.date日期字段正序


### DoubleColorBallItem对象字段说明
1.code字段：唯一标识
2.date字段：日期字段，也是唯一标识
3.redValueList字段：红色球列表
4.blueValue字段：蓝色球


### 可选算法池
1.可选算法池中，包含多个可选算法对象。
2.每个可选算法对象，包含3个字段： 算法本身，红球的的调整因子redValueFactorList和蓝球的调整因子blueValueFactor。
3.算法本身：一种基于数值特征分析的算法，从“算法本身池”中选取一种
4.红球的的调整因子redValueFactorList：包含6个整型元素，默认都是0
5.蓝球的调整因子blueValueFactor：整型，默认为0
5.约束：可选算法池中，不能存在重复的“算法本身”


### 算法本身池，要求为每种算法实现不同的预测逻辑
极端梯度提升树
轻量梯度提升树
随机森林
逻辑回归
长短期记忆网络
样本数据的区间分布
概率分布
相似度加权预测
多周期频率预测
其他AI推荐的适合随机数的算法

### 每位红球的历史边界
从文件加载数据：List<RedBallRange> redRangeData = FileProvider.readFile("/RedBallRange.json", new TypeReference<List<RedBallRange>>() { });



### 算法生成预测数据（注意，需要严格执行）
执行算法，生成一个DoubleColorBallItem对象，命名为result。
设置退出机制，如果尝试20次都无法生成满足约束的红球数值，则返回null

红球的约束：
1.redValueList中的数值不会重复，数值只能按照位序变大，最大数值为33
2.result.redValueList包含6个数值，每个数值都是整数，每个数值的区间都是[1,33]。
3.进一步细化，result.redValueList[i]的区间是[redRangeData[i].min, redRangeData[i].max]
4.再进一步动态调整，如果已经生成了result.redValueList[i]，生成result.redValueList[i+1]时的区间最小值调整为max{redRangeData[i].min, result.redValueList[i]}

蓝球的约束：
result.blueValue是整数，数值区间是[1,16]

### 单个算法的价值计算公式 （传入预测数据result:DoubleColorBallItem和目标数据target:DoubleColorBallItem）.注意：严禁修改价值计算公式
定义红球匹配个数 = result.redValueList和target.redValueList的交集

1.若红球匹配个数=6 且 result.blueValue=target.blueValue，则返回价值10000000
2.若红球匹配个数=6 且 result.blueValue!=target.blueValue，则返回价值100000
3.若红球匹配个数=5 且 result.blueValue=target.blueValue，则返回价值3000
4.其他场景，返回价值0



### 单个算法的复合价值计算公式（传入预测数据result:DoubleColorBallItem、目标数据target:DoubleColorBallItem和算法对象）.注意：严禁修改价值计算公式
先执行“单个算法的价值计算公式”，传入result和target，计算出初始价值originalValue
1.若originalValue > 0，则直接返回originalValue
2.若originalValue == 0，且如下"是否可调整方法"返回true ，则执行如下“调整方法”， 再执行“单个算法的价值计算公式”（传入调整后的预测数据result和target），计算出调整价值updatedValue，并返回updatedValue

2.1 定义是否可调整方法，逻辑如下
{
var lastChangedValue= -10;
for(i=0;i<6;i++){
var changedValue = result.redValueList[i] + 当前算法对象.redValueFactorList[i];
若changeValue超出红球的限制区间，则return false;
若changeValue <= lastChangedValue，则return false;
lastChangedValue = changeValue;
}

    var changedBlueValue = result.blueValue + 当前算法对象.blueValueFactor;
    若changedBlueValue超出蓝球的限制区间，则return false;

    默认返回true;
}

2.2 定义调整方法，逻辑如下
{
for(i=0;i<6;i++){
result.redValueList[i] += 当前算法对象.redValueFactorList[i];
}

    result.blueValue += 当前算法对象.blueValueFactor;
}


### 变量定义
定义样本量Num=100


## 预处理步骤

### 第一步：预加载全量数据

通过反序列化获取所有有序数据allData

### 第二步：计算“可选算法池”中，每种算法的调整因子
1.遍历“可选算法池”中的每一种算法对象，执行“计算算法的调整因子”
2.将算法对象池持久化到本地json文件，来固化每个算法对象的调整因子，用于后续生成预测数据时使用
3.提供方法getAlgorithmPoolFromJson，从本地json文件读取可选算法池，获取每个算法对象，包括其调整因子。

### 计算算法的调整因子
定义列表的列表redGapList:List<List<Integer>>。初始化时先创建6个空子列表
定义列表blueGapList:List<Integer>。初始化为空列表

对allData，从位序i=1开始for(i=Num;i<allData.size();i++){
1.allData，取位序max(0,i-Num)到i-1的前序数，执行算法生成预测数据result。如果result为null则continue处理
2.定义目标数据target = allData[i]。
2.for(x=0;x<6;x++){ 计算gap = target.redValueList[x] - result.redValueList[x]，将gap添加到redGapList[x]子列表中
3.计算blueGap = target.blueValue - result.blueValue，将blueGap添加到blueGapList列表中
}

4.帮我打印详细的进度

求均值
1.对red1GapList求均值，结果四舍五入，放入算法对象.redValueFactorList[0]
2.对red2GapList求均值，结果四舍五入，放入算法对象.redValueFactorList[1]
3.对red3GapList求均值，结果四舍五入，放入算法对象.redValueFactorList[2]
4.对red4GapList求均值，结果四舍五入，放入算法对象.redValueFactorList[3]
5.对red5GapList求均值，结果四舍五入，放入算法对象.redValueFactorList[4]
6.对red6GapList求均值，结果四舍五入，放入算法对象.redValueFactorList[5]
7.对blueGapList求均值，结果四舍五入，放入算法对象.blueValueFactor字段



### 第三步：挑选算法
1.创建”验证成功算法列表“
2.调用getAlgorithmPoolFromJson，获取可选算法池的所有算法对象，包括其调整因子。
遍历每一种算法对象，执行“算法验证”，若验证成功，则将算法对象添加到”验证成功算法列表“中
3.如果”验证成功算法列表“.size=8，则完成挑选算法，返回”验证成功算法列表“
4.如果”验证成功算法列表“.size<8，则最多再尝试AI推荐的其他20中算法对象，分别执行“算法验证”，若验证成功，则将算法对象添加到”验证成功算法列表“中


### 算法验证
对verifyData，从位序i=1开始for(i=Num;i<verifyData.size();i++){
1.verifyData，取位序max(0,i-Num)到i-1的前序数，执行“算法生成预测数据”，得到预测数据result，如果result为null则continue处理

2.执行“单个算法的复合价值计算公式”，传入预测数据result、目标数据verifyData[i]和当前算法对象，得到复合价值value
3.若value>0，则算法验证成功，直接返回
}
兜底返回验证失败

### 第四步，预测算法封装
封装一个方法，遍历”验证成功算法列表“，基于样本数据latestData进行预测，返回每种算法预测结果并打印


### 代码优化
优化不同算法，使用不同的随机因子
优化整体算法，减少代码重复率，但不能改变原代码逻辑

完善“可选算法池”和“算法本身池”，解决如下问题日：
20个额外算法都是 RandomForestAlgorithm 的复制品，只改名字不改逻辑，因为要遵守如下两个原则：
1.每种算法实现不同的预测逻辑                                                                                                        
2.违违反不能存在重重复算法本身

检查各种随机数生成方法，避免死循环，修复方案是调整随机数的生成方式，而不是增加超时

