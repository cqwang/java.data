## 数据定义

1.通过反序列化获取所有数据：
List<DoubleColorBallItem> originalData = FileProvider.readFile("/DoubleColorBallData.json", new TypeReference<List<DoubleColorBallItem>>() { });

2.定义一个对象RedBallRange，包含两个整型字段：min默认为100，max默认为0
3.定义一个列表rangeList:List<RedBallRange>并初始化出6个空对象
4.for(i =0;i<originalData.size();i++){
    var item = originalData[i];
    for(j=0;j<item.redValueList.size();j++){
        var redValue = item.redValueList[j];
        var range = rangeList[j];
        if(redValue>range.max){
            range.max=redValue;
        }
        if(redValue<range.min){
            range.min=redValue;
        }
    }
}
5.将rangeList序列化到单独文件中，保存下来
