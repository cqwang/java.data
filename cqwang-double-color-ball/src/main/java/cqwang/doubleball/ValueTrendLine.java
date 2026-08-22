package cqwang.doubleball;

import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorAlgorithmRegistry;
import cqwang.doubleball.detection.algorithm.doublecolorball.DoubleColorListAlgorithmSelector;
import cqwang.doubleball.detection.model.option.RunOption;
import cqwang.doubleball.detection.preload.DoubleColorBallPreload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValueTrendLine {

    public static String buildEchartDom() {
        var echartList = build();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("option = {\n" +
                "   legend:{\n" +
                "        data: [");
        stringBuilder.append("\"");
        stringBuilder.append(String.join("\",\"", echartList.getChartNames()));
        stringBuilder.append("\"]");
        stringBuilder.append(",\n" +
                "    },\n" +
                "  xAxis: {\n" +
                "    type: 'category',\n" +
                "    data: ");
        stringBuilder.append(Arrays.toString(echartList.getIndexList()));
        stringBuilder.append("\n" +
                "  },\n" +
                "  yAxis: {\n" +
                "    type: 'value'\n" +
                "  },\n" +
                "  series: [\n");
        for (int i = 0; i < echartList.getCharts().size(); i++) {
            var echart = echartList.getCharts().get(i);
            stringBuilder.append("   {\n" +
                    "      data: ");
            stringBuilder.append(Arrays.toString(echart.getValueList()));
            stringBuilder.append(",\n" +
                    "      type: 'line'\n" +
                    "    }");
            if (i != echartList.getCharts().size() - 1) {
                stringBuilder.append(",\n");
            } else {
                stringBuilder.append("\n");
            }
        }
        stringBuilder.append(" ]\n" +
                "};");
        return stringBuilder.toString();
    }

    static EChartList build() {
        DoubleColorBallPreload.execute();
        var indexList = new int[DoubleColorBallPreload.getAllData().size()];
        for (int i = 0; i < indexList.length; i++) {
            indexList[i] = i;
        }

        var algorithmList = new DoubleColorListAlgorithmSelector().execute(RunOption.RE_CALCULATE_VALUE_FROM_FILE);
        var chartNames = algorithmList.stream().map(DoubleColorAlgorithmRegistry::getUniqueName).collect(Collectors.toList());

        var list = new ArrayList<EChart>(algorithmList.size());
        for (var algorithm : algorithmList) {
            int[] valueList = new int[indexList.length];
            for (var point : algorithm.getPredictResult().getPredictPointList()) {
                if (point.getPredictValue().getPredictValue() > 500) {
                    valueList[point.getPredictIndex()] = 0; // 降噪，看成长曲线
                } else {
                    valueList[point.getPredictIndex()] += point.getPredictValue().getPredictValue();
                }
            }
            var echart = new EChart(valueList);
            list.add(echart);
        }
        return new EChartList(indexList, chartNames, list);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class EChartList {
    private int[] indexList;
    private List<String> chartNames;
    private List<EChart> charts;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class EChart {
    private int[] valueList;
}
