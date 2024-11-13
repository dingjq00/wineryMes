package com.mom.winery.view.meszengguounitprocedure;

import com.mom.winery.app.PdfService;
import com.mom.winery.entity.*;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import elemental.json.JsonObject;
import io.jmix.chartsflowui.component.Chart;
import io.jmix.chartsflowui.data.item.EntityDataItem;
import io.jmix.chartsflowui.kit.component.model.DataSet;
import io.jmix.chartsflowui.kit.component.model.Title;
import io.jmix.chartsflowui.kit.component.model.Tooltip;
import io.jmix.chartsflowui.kit.component.model.axis.AbstractCartesianAxis;
import io.jmix.chartsflowui.kit.component.model.axis.XAxis;
import io.jmix.chartsflowui.kit.component.model.axis.YAxis;
import io.jmix.chartsflowui.kit.component.model.legend.Legend;
import io.jmix.chartsflowui.kit.component.model.series.BarSeries;
import io.jmix.chartsflowui.kit.component.model.series.Label;
import io.jmix.chartsflowui.kit.component.model.series.LineSeries;
import io.jmix.chartsflowui.kit.component.model.series.mark.MarkArea;
import io.jmix.chartsflowui.kit.component.model.shared.AbstractAxisPointer;
import io.jmix.chartsflowui.kit.component.model.shared.AbstractTooltip;
import io.jmix.chartsflowui.kit.component.model.shared.Color;
import io.jmix.chartsflowui.kit.component.model.shared.ItemStyle;
import io.jmix.chartsflowui.kit.component.model.visualMap.AbstractVisualMap;
import io.jmix.chartsflowui.kit.component.model.visualMap.ContinuousVisualMap;
import io.jmix.chartsflowui.kit.component.model.visualMap.PiecewiseVisualMap;
import io.jmix.chartsflowui.kit.data.chart.ListChartItems;
import io.jmix.core.DataManager;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.facet.DataLoadCoordinator;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.reportsflowui.view.reportwizard.template.generators.ChartGenerator;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/31 13:32
 */

@Route(value = "mesZengguoMultiAnalysis", layout = MainView.class)
@ViewController("MesZengguoMultiAnalysisi.list")
@ViewDescriptor("mes-zengguo-multi-analysis-view.xml")
@LookupComponent("mesZengguoUnitProceduresDataGrid")
@DialogMode(width = "100%")
public class MesZengguoMultiAnalysisView extends StandardListView<MesZengguoUnitProcedure> {
    @Autowired
    protected UiComponents uiComponents;
    List<Long> unitProcedureIds = new ArrayList<>();
    @ViewComponent
    private CollectionLoader<MesZengguoUnitProcedure> mesZengguoUnitProceduresDl;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private VerticalLayout shangzengRealDataVerticalLayout;
    @Autowired
    private ChartGenerator chartGenerator;
    @Autowired
    private PdfService pdfService;

    public List<Long> getUnitProcedureIds() {
        return unitProcedureIds;
    }
    public void setUnitProcedureIds(List<Long> unitProcedureIds) {
        this.unitProcedureIds = unitProcedureIds;
    }


//    @Subscribe
//    public void onBeforeShow(final BeforeShowEvent event) {
//        Map<String,Object> mesZengguoUnitProceduresDlParams = new HashMap<>();
//        mesZengguoUnitProceduresDlParams.put("unitProcedureIds", unitProcedureIds);
//        mesZengguoUnitProceduresDl.setParameters(mesZengguoUnitProceduresDlParams);
//        mesZengguoUnitProceduresDl.load();
//
//    }

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {
//    public void onReady(final ReadyEvent event) {
        List<String> idsParams = event.getQueryParameters().getParameters().get("ids");
        String ids = idsParams.get(0);
        List<Long> unitProcedureIds =  Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
        setUnitProcedureIds(unitProcedureIds);

        Map<String,Object> mesZengguoUnitProceduresDlParams = new HashMap<>();
        mesZengguoUnitProceduresDlParams.put("unitProcedureIds", unitProcedureIds);
        mesZengguoUnitProceduresDl.setParameters(mesZengguoUnitProceduresDlParams);
        mesZengguoUnitProceduresDl.load();


        List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList = mesZengguoUnitProceduresDl.getContainer().getItems();
        List<MesZengguoRealDataV2> mesZengguoRealDataList = new ArrayList<>();
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            MesArea mesArea = mesZengguoUnitProcedure.getMesZengguo().getMesArea();
            MesZengguo mesZengguo = mesZengguoUnitProcedure.getMesZengguo();
            List<MesZengguoRealDataV2> realDataList = dataManager.load(MesZengguoRealDataV2.class)
                    .query("select e from MesZengguoRealDataV2 e " +
                            "where e.mesArea = :mesArea " +
                            "and e.mesZengguo = :mesZengguo " +
                            "and e.winccUpdateTime >= :winccStartTime " +
                            "and e.winccUpdateTime <= :winccEndTime " +
                            "order by e.winccUpdateId")
                    .parameter("mesArea", mesArea)
                    .parameter("mesZengguo", mesZengguo)
                    .parameter("winccStartTime", mesZengguoUnitProcedure.getPhaseStartTimeTotal())
                    .parameter("winccEndTime", mesZengguoUnitProcedure.getPhaseEndTimeTotal()==null?new Date():mesZengguoUnitProcedure.getPhaseEndTimeTotal())
                    .list();
            realDataList.forEach(e -> {
                e.setMesZengguoUnitProcedure(mesZengguoUnitProcedure);
//                e.setMesZengguo(mesZengguoUnitProcedure.getMesZengguo());
            });

            List<MesZengguoOperation> mesZengguoOperationList = dataManager.load(MesZengguoOperation.class)
                    .query("select e from MesZengguoOperation e " +
                            "where e.mesZengguo = :mesZengguo " +
                            "and e.phaseStartWinccId >= :phaseStartWinccId " +
                            "and e.phaseStartWinccId < :phaseEndWinccId " +
                            "order by e.phaseStartWinccId")
                    .parameter("mesZengguo", mesZengguoUnitProcedure.getMesZengguo())
                    .parameter("phaseStartWinccId", mesZengguoUnitProcedure.getPhaseStartWinccId())
                    .parameter("phaseEndWinccId", mesZengguoUnitProcedure.getPhaseEndWinccId())
                    .list();
            for (MesZengguoOperation mesZengguoOperation : mesZengguoOperationList) {
                List<MesZengguoRealDataV2> operationRealDataList = realDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(mesZengguoOperation.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(mesZengguoOperation.getPhaseEndTimeTotal()) <= 0)
                        .toList();
                operationRealDataList.forEach(e -> {
                    e.setMesZengguoOperation(mesZengguoOperation);
                });
            }

            List<MesZengguoRecord> mesZengguoRecordList = dataManager.load(MesZengguoRecord.class)
                    .query("select e from MesZengguoRecord e " +
                            "where e.mesZengguo = :mesZengguo " +
                            "and e.phaseStartWinccId >= :phaseStartWinccId " +
                            "and e.phaseStartWinccId < :phaseEndWinccId " +
                            "order by e.phaseStartWinccId")
                    .parameter("mesZengguo", mesZengguoUnitProcedure.getMesZengguo())
                    .parameter("phaseStartWinccId", mesZengguoUnitProcedure.getPhaseStartWinccId())
                    .parameter("phaseEndWinccId", mesZengguoUnitProcedure.getPhaseEndWinccId())
                    .list();

            for (MesZengguoRecord mesZengguoRecord : mesZengguoRecordList) {
                List<MesZengguoRealDataV2> recordRealDataList = realDataList.stream()
                        .filter(e ->e.getMesZengguo().equals(mesZengguo)
                                && e.getWinccUpdateTime().compareTo(mesZengguoRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(mesZengguoRecord.getPhaseEndTimeTotal()) <= 0)
                        .toList();
                recordRealDataList.forEach(e -> {
                    e.setMesZengguoRecord(mesZengguoRecord);
                });
            }
            mesZengguoRealDataList.addAll(realDataList);
        }
        // 上甑曲线
        getShangzengQuxian(mesZengguoUnitProcedureList, mesZengguoRealDataList);

        // 溜酒线
        getLiujiuQuxian(mesZengguoUnitProcedureList, mesZengguoRealDataList);

        // 蒸煮线
        getZhengzhuQuxian(mesZengguoUnitProcedureList, mesZengguoRealDataList);

        // 摊晾曲线
        getTanliangQuxian(mesZengguoUnitProcedureList);
    }

    private void getTanliangQuxian(List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList) {
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            Date phaseEndTimeTotal = mesZengguoUnitProcedure.getPhaseEndTimeTotal();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(phaseEndTimeTotal);
            calendar.add(Calendar.HOUR, 4);
            Date phaseMaxEndTime = calendar.getTime();
            List<MesTanliangjiRecord> mesTanliangjiRecordList = dataManager.load(MesTanliangjiRecord.class)
                    .query("select e from MesTanliangjiRecord e " +
                            "where e.resourceZengguo is not null " +
                            "and e.zengSequence = :zengSequence " +
                            "and e.resourceZengguo = :mesZengguo " +
                            "and e.phaseStartTime >= :phaseEndTime " +
                            "and e.phaseStartTime <= :phaseMaxEndTime " +
                            "order by e.phaseStartTime ")
                    .parameter("zengSequence", mesZengguoUnitProcedure.getZengSequence())
                    .parameter("mesZengguo", mesZengguoUnitProcedure.getMesZengguo())
                    .parameter("phaseEndTime", phaseEndTimeTotal)
                    .parameter("phaseMaxEndTime", phaseMaxEndTime)
                    .maxResults(1)
                    .list();
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("40em");
            if(mesTanliangjiRecordList.isEmpty()){
                chart.withTitle(new Title().withText("未找到摊晾机数据"));
                shangzengRealDataVerticalLayout.add(chart);
                continue;
            }
            String titleStr = "摊晾阶段状况：" + "\n "
                    + "摊晾机：" + mesTanliangjiRecordList.getFirst().getMesTanliangji().getTanliangjiCode() + "\n"
                    + "甑锅：" + mesZengguoUnitProcedure.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + mesZengguoUnitProcedure.getZengSequence() + "\n"
                    + "每一个间隔代表30秒中的数据";
            Title title = new Title().withText( titleStr).withShow(true);
            chart.withTitle(title);

            MesTanliangjiRecord mesTanliangjiRecord = mesTanliangjiRecordList.getFirst();
            List<MesTanliangRealDataV2> mesTanliangRealDataList = dataManager.load(MesTanliangRealDataV2.class)
                    .query("select e from MesTanliangRealDataV2 e " +
                            "where e.mesArea = :mesArea " +
                            "and e.mesTanliangji = :mesTanliangji " +
                            "and e.winccUpdateId >= :phaseStartWinccId " +
                            "and e.winccUpdateId <= :phaseEndWinccId " +
                            "order by e.winccUpdateId")
                    .parameter("mesArea", mesZengguoUnitProcedure.getMesZengguo().getMesArea())
                    .parameter("mesTanliangji", mesTanliangjiRecord.getMesTanliangji())
                    .parameter("phaseStartWinccId", mesTanliangjiRecord.getWinccStartId())
                    .parameter("phaseEndWinccId", mesTanliangjiRecord.getWinccEndId())
                    .list();

            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            for(int i=0;i<mesTanliangRealDataList.size();i++){
                MesTanliangRealDataV2 mesTanliangRealData = mesTanliangRealDataList.get(i);
                if(mesTanliangRealData.getTangliangGeiliaojiLiusu() < 0 || mesTanliangRealData.getQufenLiusu() < 0){
                    continue;
                }
                mesTanliangRealData.setTempIndex(i);
                chartItems.addItem(new EntityDataItem(mesTanliangRealData));
            }
            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("tempIndex")
                                    .withValueFields("tangliangGeiliaojiLiusu","qufenLiusu","tanliangZhongbuTemp","tanliangChukouTemp")
                    )
            );
            Tooltip tooltip = new Tooltip();
            tooltip.setTrigger(AbstractTooltip.Trigger.AXIS);
            AbstractTooltip.AxisPointer axisPointer = new AbstractTooltip.AxisPointer();
            axisPointer.setType(AbstractTooltip.AxisPointer.IndicatorType.CROSS);
            tooltip.setAxisPointer(axisPointer);
            chart.setTooltip(tooltip);

            Legend legend = new Legend();
            chart.withLegend(legend);
            XAxis xAxis = new XAxis().withName("每30秒钟");
            chart.withXAxis(xAxis);
            YAxis yAxis = new YAxis().withName("流速").withPosition(AbstractCartesianAxis.Position.LEFT);
            chart.withYAxis(yAxis);
            chart.withYAxis(new YAxis().withName("温度").withPosition(AbstractCartesianAxis.Position.RIGHT));

            LineSeries lineSeries = new LineSeries().withName("摊晾给料机流速").withYAxisIndex(0);
            LineSeries lineSeries1 = new LineSeries().withName("摊晾曲粉流速").withYAxisIndex(0);
            LineSeries lineSeries2 = new LineSeries().withName("摊晾中部温度").withYAxisIndex(1);
            LineSeries lineSeries3 = new LineSeries().withName("摊晾出口温度").withYAxisIndex(1);
            chart.withSeries(lineSeries,lineSeries1,lineSeries2,lineSeries3);
            shangzengRealDataVerticalLayout.add(chart);

        }
    }

    private void getZhengzhuQuxian(List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList, List<MesZengguoRealDataV2> mesZengguoRealDataList) {
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("40em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            List<MesZengguoRealDataV2> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoUnitProcedure().equals(mesZengguoUnitProcedure))
                    .filter(e ->e.getMesZengguoOperation()!=null && e.getMesZengguoOperation().getMainPhase().equals(EnumZengguoMainPhase.ZHENGZHU_CHONGSUAN))
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();
            for(int i=0;i<mesZengguoRealDataForUpList.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataForUpList.get(i);
                mesZengguoRealData.setTempIndex(i);
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }
            String titleStr = "蒸煮阶段状况：" + "\n "
                    + "甑锅：" + mesZengguoUnitProcedure.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + mesZengguoUnitProcedure.getZengSequence() + "\n"
                    + "每一个间隔代表30秒中的数据";
            Title title = new Title().withText( titleStr).withShow(true);
            chart.withTitle(title);

            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("tempIndex")
//                                    .withValueFields("zhengqiKaidu","zhengqiShunshiLiuliang","lengningKaidu","zhengqiYali","guoniWendu","zhenkongYali","diguoShuiwen","huishuiWendu")
                                    .withValueFields("zhengqiYali","guoniWendu","zhenkongYali","diguoShuiwen","huishuiWendu")
                    )
            );
            Tooltip tooltip = new Tooltip();
            tooltip.setTrigger(AbstractTooltip.Trigger.AXIS);
            AbstractTooltip.AxisPointer axisPointer = new AbstractTooltip.AxisPointer();
            axisPointer.setType(AbstractTooltip.AxisPointer.IndicatorType.CROSS);
            tooltip.setAxisPointer(axisPointer);
            chart.setTooltip(tooltip);

            Legend legend = new Legend();
            chart.withLegend(legend);
            XAxis xAxis = new XAxis().withName("每30秒钟");
            chart.withXAxis(xAxis);
            YAxis yAxis = new YAxis().withName("压力").withPosition(AbstractCartesianAxis.Position.LEFT);
            chart.withYAxis(yAxis);
            chart.withYAxis(new YAxis().withName("温度").withPosition(AbstractCartesianAxis.Position.RIGHT));

//            LineSeries lineSeries = new LineSeries().withName("蒸汽开度").withYAxisIndex(1);
//            LineSeries lineSeries1 = new LineSeries().withName("蒸汽瞬时流量").withYAxisIndex(0);
//            LineSeries lineSeries2 = new LineSeries().withName("冷凝开度").withYAxisIndex(1);
            LineSeries lineSeries3 = new LineSeries().withName("蒸汽压力").withYAxisIndex(0);
            LineSeries lineSeries5 = new LineSeries().withName("锅内温度").withYAxisIndex(1);
            LineSeries lineSeries6 = new LineSeries().withName("真空压力").withYAxisIndex(0);
            LineSeries lineSeries7 = new LineSeries().withName("底锅水温").withYAxisIndex(1);
            LineSeries lineSeries8 = new LineSeries().withName("回水温度").withYAxisIndex(1);
            chart.withSeries(lineSeries3,lineSeries5,lineSeries6,lineSeries7,lineSeries8);
            shangzengRealDataVerticalLayout.add(chart);

        }
    }

    private void getLiujiuQuxian(List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList, List<MesZengguoRealDataV2> mesZengguoRealDataList) {
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("40em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
//            List<Integer> liujiuList = Arrays.asList(520,521,522,523,524);
            List<Integer> liujiuList = Arrays.asList(520,521,522,523);
            List<MesZengguoRealDataV2> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoUnitProcedure().equals(mesZengguoUnitProcedure))
                    .filter(e ->e.getMesZengguoOperation() !=null && e.getMesZengguoOperation().getMainPhase().equals(EnumZengguoMainPhase.LIUJIU))
                    .filter(e -> liujiuList.contains(e.getMesZengguoRecord().getZengguoPhase().getPhaseNo()))
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();
            Integer jiutouStart = -1;
            Integer jiutouEnd = -1;
            Integer firstClassStart = -1;
            Integer firstClassEnd = -1;
            Integer secondClassStart = -1;
            Integer secondClassEnd = -1;
            Integer thirdClassStart = -1;
            Integer thirdClassEnd = -1;
            Integer jiuweiStart = -1;
            Integer jiuweiEnd = -1;
            for(int i=0;i<mesZengguoRealDataForUpList.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataForUpList.get(i);
                mesZengguoRealData.setTempIndex(i);
                MesZengguoRecord mesZengguoRecord = mesZengguoRealData.getMesZengguoRecord();
                MesZenggouPhaseConfig mesZenggouPhaseConfig = mesZengguoRecord.getZengguoPhase();
                switch (mesZenggouPhaseConfig.getPhaseNo()){
                    case 520: // 酒头
                        if(jiutouStart == -1){jiutouStart = i;}
                        break;
                    case 521: // 一级
                        if(firstClassStart == -1){
                            firstClassStart = i;
                            if(jiutouEnd == -1){
                                jiutouEnd = i - 1;
                            }
                        }
                        break;
                    case 522: // 二级
                        if(secondClassStart == -1){
                            secondClassStart = i;
                            if(jiutouEnd == -1){
                                jiutouEnd = i - 1;
                            }
                            if(firstClassEnd == -1){
                                firstClassEnd = i - 1;
                            }
                        }
                        break;
                    case 523: // 三级
                        if(thirdClassStart == -1){
                            thirdClassStart = i;
                            if(jiutouEnd == -1){
                                jiutouEnd = i - 1;
                            }
                            if(firstClassEnd == -1){
                                firstClassEnd = i - 1;
                            }
                            if(secondClassEnd == -1){
                                secondClassEnd = i - 1;
                            }
                        }
                        thirdClassEnd = i;
                        break;
//                    case 524: // 酒尾
//                        if(jiuweiStart == -1){
//                            jiuweiStart = i;
//                            if(jiutouEnd == -1){
//                                jiutouEnd = i - 1;
//                            }
//                            if(firstClassEnd == -1){
//                                firstClassEnd = i - 1;
//                            }
//                            if(secondClassEnd == -1){
//                                secondClassEnd = i - 1;
//                            }
//                            if(thirdClassEnd == -1){
//                                thirdClassEnd = i - 1;
//                            }
//                        }
//                        jiuweiEnd = i;
//                        break;
                }
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }
            String chartTitle = "接酒阶段状况：" + "\n "
                    + "甑锅：" + mesZengguoUnitProcedure.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + mesZengguoUnitProcedure.getZengSequence() + "\n";
//            if(jiuweiStart !=  -1) {
//                chartTitle = chartTitle + "酒头：" + jiutouStart + "-" + jiutouEnd + "\n";
//            }
//            if(firstClassStart !=  -1) {
//                chartTitle = chartTitle + "    " + "一级：" + firstClassStart + "-" + firstClassEnd + "\n";
//            }
//            if(secondClassStart !=  -1) {
//                chartTitle = chartTitle + "    " + "二级：" + secondClassStart + "-" + secondClassEnd + "\n";
//            }
//            if(thirdClassStart !=  -1) {
//                chartTitle = chartTitle + "    " + "三级：" + thirdClassStart + "-" + thirdClassEnd + "\n";
//            }
//            if(jiuweiStart !=  -1) {
//                chartTitle = chartTitle + "酒尾：" + jiuweiStart + "-" + jiuweiEnd + "\n";
//            }
//            if(!chartTitle.isEmpty()){
//               chartTitle= "每一个间隔代表30秒中的数据\n" + chartTitle;
//            }
            Title title = new Title().withText(chartTitle).withShow(true);
            chart.setTitle(title);

            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("tempIndex")
//                                    .withValueFields("zhengqiKaidu","zhengqiShunshiLiuliang","lengningKaidu","zhengqiYali","guoniWendu","chujiuWendu","diguoShuiwen")
                                    .withValueFields("zhengqiYali","guoniWendu","chujiuWendu","diguoShuiwen")
                        )
            );
            Tooltip tooltip = new Tooltip();
            tooltip.setTrigger(AbstractTooltip.Trigger.AXIS);
            AbstractTooltip.AxisPointer axisPointer = new AbstractTooltip.AxisPointer();
            axisPointer.setType(AbstractTooltip.AxisPointer.IndicatorType.CROSS);
            tooltip.setAxisPointer(axisPointer);
            chart.setTooltip(tooltip);

            Legend legend = new Legend();
            chart.withLegend(legend);
            XAxis xAxis = new XAxis();
            chart.withXAxis(xAxis);
            YAxis yAxis = new YAxis().withName("蒸汽压力");
            chart.withYAxis(yAxis);
//            chart.withYAxis(new YAxis().withName("蒸汽瞬时流量").withPosition(AbstractCartesianAxis.Position.LEFT));

            chart.withYAxis(new YAxis().withName("温度").withPosition(AbstractCartesianAxis.Position.RIGHT));

//            LineSeries lineSeries = new LineSeries().withName("蒸汽开度");
//            LineSeries lineSeries1 = new LineSeries().withName("蒸汽瞬时流量").withYAxisIndex(1);
//            LineSeries lineSeries2 = new LineSeries().withName("冷凝开度").withYAxisIndex(1);
            LineSeries lineSeries3 = new LineSeries().withName("蒸汽压力").withYAxisIndex(0);
            // Define the mark area for the red section (0-10)
//            lineSeries3.withMarkArea(new MarkArea()
//                            .withItemStyle(new ItemStyle().withColor(Color.RED))
//                    .withData(new MarkArea.PointPair(new MarkArea.Point().withX("0") // withValue(0.0)
//                            ,new MarkArea.Point().withX("10"))  //.withValue(10.0))
//                    )
//            );
            LineSeries lineSeries5 = new LineSeries().withName("锅内温度").withYAxisIndex(1);
            LineSeries lineSeries6 = new LineSeries().withName("出酒温度").withYAxisIndex(1);
            LineSeries lineSeries7 = new LineSeries().withName("底锅水温").withYAxisIndex(1);
            PiecewiseVisualMap piecewiseVisualMap = new PiecewiseVisualMap();
            List<PiecewiseVisualMap.Piece> pieces = new ArrayList<>();
            if(firstClassStart != -1){
                pieces.add(new PiecewiseVisualMap.Piece().withMin(Double.valueOf(firstClassStart)).withMax(Double.valueOf(firstClassEnd)).withColor(Color.RED));
            }
            if(secondClassStart != -1){
                pieces.add(new PiecewiseVisualMap.Piece().withMin(Double.valueOf(secondClassStart)).withMax(Double.valueOf(secondClassEnd)).withColor(Color.GREEN));
            }
            if(thirdClassStart != -1){
                pieces.add(new PiecewiseVisualMap.Piece().withMin(Double.valueOf(thirdClassStart)).withMax(Double.valueOf(thirdClassEnd)).withColor(Color.BLUE));
            }
            if(!pieces.isEmpty()){
                piecewiseVisualMap.setPieces(pieces);
                chart.withVisualMap(piecewiseVisualMap);
            }
            chart.withSeries(lineSeries3,lineSeries5,lineSeries6,lineSeries7);
            shangzengRealDataVerticalLayout.add(chart);
            }
    }

    private void getShangzengQuxian(List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList, List<MesZengguoRealDataV2> mesZengguoRealDataList) {
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            // 为每一个mesZengguoUnitProcedure增加一个echart的折线图
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("40em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            List<MesZengguoRealDataV2> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoUnitProcedure().equals(mesZengguoUnitProcedure))
                    .filter(e ->e.getMesZengguoOperation() != null && e.getMesZengguoOperation().getMainPhase().equals(EnumZengguoMainPhase.SHANGZENG))
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();
            for(int i=0;i<mesZengguoRealDataForUpList.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataForUpList.get(i);
                mesZengguoRealData.setTempIndex(i);
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }
            String titleStr = "上甑阶段状况：" + "\n "
                    + "甑锅：" + mesZengguoUnitProcedure.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + mesZengguoUnitProcedure.getZengSequence() + "\n"
                    + "每一个间隔代表30秒中的数据";
            Title title = new Title().withText( titleStr).withShow(true);
            chart.withTitle(title);

            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("tempIndex")
//                                    .withValueFields("zhengqiKaidu","zhengqiShunshiLiuliang","zhengqiYali","guoniWendu","diguoShuiwen","mesZengguoRecord.shangzengLayer")
                                    .withValueFields("zhengqiYali","guoniWendu","diguoShuiwen","mesZengguoRecord.shangzengLayer")
                    )
            );
            Tooltip tooltip = new Tooltip();
            tooltip.setTrigger(AbstractTooltip.Trigger.AXIS);
            AbstractTooltip.AxisPointer axisPointer = new AbstractTooltip.AxisPointer();
            axisPointer.setType(AbstractTooltip.AxisPointer.IndicatorType.CROSS);
            tooltip.setAxisPointer(axisPointer);
            chart.setTooltip(tooltip);

            Legend legend = new Legend();
            chart.withLegend(legend);
            XAxis xAxis = new XAxis();
            chart.withXAxis(xAxis);
            YAxis yAxis = new YAxis().withName("蒸汽压力");
            chart.withYAxis(yAxis);
//            chart.withYAxis(new YAxis().withName("蒸汽瞬时流量").withPosition(AbstractCartesianAxis.Position.LEFT));

            chart.withYAxis(new YAxis().withName("温度及层高").withPosition(AbstractCartesianAxis.Position.RIGHT));

            LineSeries lineSeries = new LineSeries();
//            lineSeries.setName("蒸汽开度");
//            lineSeries.setLabel(new Label().withFormatter("{c}").withPosition(Label.Position.PositionType.INSIDE_TOP_LEFT).withShow(true));
//            LineSeries lineSeries1 = new LineSeries().withName("蒸汽瞬时流量")
//                    .withYAxisIndex(1);
            LineSeries lineSeries2 = new LineSeries().withName("蒸汽压力").withYAxisIndex(0);
            LineSeries lineSeries3 = new LineSeries().withName("锅内温度").withYAxisIndex(1);
            LineSeries lineSeries5 = new LineSeries().withName("底锅水温").withYAxisIndex(1);
            BarSeries barSeries = new BarSeries().withName("上甑层数").withYAxisIndex(1);
            chart.withSeries(lineSeries2,lineSeries3,lineSeries5,barSeries);
            shangzengRealDataVerticalLayout.add(chart);
        }
    }

}

