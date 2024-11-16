package com.mom.winery.view.meszengguorecord;

import com.mom.winery.app.WinccDataDealCommons;
import com.mom.winery.entity.*;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
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
import io.jmix.chartsflowui.kit.component.model.series.LineSeries;
import io.jmix.chartsflowui.kit.component.model.shared.AbstractTooltip;
import io.jmix.chartsflowui.kit.component.model.shared.Color;
import io.jmix.chartsflowui.kit.component.model.visualMap.PiecewiseVisualMap;
import io.jmix.chartsflowui.kit.data.chart.ListChartItems;
import io.jmix.core.DataManager;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/12 10:48
 */

@Route(value = "mesZengguoRecordAnalysiss", layout = MainView.class)
@ViewController(id = "MesZengguoRecordAnalysisi.list")
@ViewDescriptor(path = "mes-zengguo-record-analysis-list-view.xml")
@LookupComponent("mesZengguoRecordsDataGrid")
@DialogMode(width = "64em")
public class MesZengguoRecordAnalysisiListView extends StandardListView<MesZengguoRecord> {

    private List<Long> recordIds;
    @ViewComponent
    private CollectionLoader<MesZengguoRecord> mesZengguoRecordsDl;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private UiComponents uiComponents;
    @ViewComponent
    private VerticalLayout shangzengRealDataVerticalLayout;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    public List<Long> getRecordIds() {
        return recordIds;
    }

    public void setRecordIds(List<Long> recordIds) {
        this.recordIds = recordIds;
    }

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {
        List<String> idsParams = event.getQueryParameters().getParameters().get("ids");
        String ids = idsParams.get(0);
        List<Long> recordIds = Arrays.stream(ids.split(",")).map(Long::parseLong).toList();
        setRecordIds(recordIds);
        Map<String, Object> mesZengguoRecordsDlParams = new HashMap<>();
        mesZengguoRecordsDlParams.put("ids", recordIds);
        mesZengguoRecordsDl.setParameters(mesZengguoRecordsDlParams);
        mesZengguoRecordsDl.load();

        List<MesZengguoRecord> mesZengguoRecordList = mesZengguoRecordsDl.getContainer().getItems();
        List<MesZengguoRealDataV2> mesZengguoRealDataList = new ArrayList<>();
        List<MesZengguoRecord> totalRecordList = new ArrayList<>();

        for (MesZengguoRecord mesZengguoRecord : mesZengguoRecordList) {
            // 找对应的 sumdata
            MesZengSumData sumData = dataManager.load(MesZengSumData.class)
                    .query("select e from MesZengSumData e " +
                            "where e.zengStartTime = :startTime " +
                            "and e.resourceZengguo = :mesZengguo " +
                            "and e.zengNo = :zengSequence ")
                    .parameter("startTime", mesZengguoRecord.getStartTimeTotal())
                    .parameter("mesZengguo", mesZengguoRecord.getMesZengguo())
                    .parameter("zengSequence", mesZengguoRecord.getZengSequence())
                    .optional()
                    .orElse(null);
            mesZengguoRecord.setSumDataRecord(sumData);
            MesZengguo mesZengguo = mesZengguoRecord.getMesZengguo();
            List<MesZengguoRealDataV2> realDataList = dataManager.load(MesZengguoRealDataV2.class)
                    .query("select e from MesZengguoRealDataV2 e " +
                            "where e.mesZengguo = :mesZengguo " +
                            "and e.winccUpdateTime >= :winccStartTime " +
                            "and e.winccUpdateTime <= :winccEndTime " +
                            "order by e.winccUpdateId")
                    .parameter("mesZengguo", mesZengguo)
                    .parameter("winccStartTime", mesZengguoRecord.getStartTimeTotal())
                    .parameter("winccEndTime", mesZengguoRecord.getEndTimeTall() == null ? new Date() : mesZengguoRecord.getEndTimeTall())
                    .list();
            for (MesZengguoRealDataV2 mesZengguoRealDataV2 : realDataList) {
                mesZengguoRealDataV2.setMesZengguoRecord(mesZengguoRecord);
            }

            List<MesZengguoRecord> allRecordList = dataManager.load(MesZengguoRecord.class)
                    .query("select e from MesZengguoRecord e " +
                            "where e.mesZengguo = :mesZengguo " +
                            " and e.zengSequence = :zengSequence " +
                            " and e.startTimeTotal = :startTime "  )
                    .parameter("mesZengguo", mesZengguo)
                    .parameter("zengSequence", mesZengguoRecord.getZengSequence())
                    .parameter("startTime", mesZengguoRecord.getStartTimeTotal())
                    .list();
            for (MesZengguoRecord record : allRecordList) {
                record.setRelatedRecordId(mesZengguoRecord.getId());
                Integer pahseNo = record.getZengguoPhase().getPhaseNo();
                if (pahseNo < 200) {
                    record.setMainPhase(EnumZengguoMainPhase.OTHERS);
                }
                if (pahseNo >= 200 && pahseNo < 400) {
                    record.setMainPhase(EnumZengguoMainPhase.DIGUOSHUI_ADD);
                }
                if (pahseNo >= 400 && pahseNo < 500) {
                    record.setMainPhase(EnumZengguoMainPhase.SHANGZENG);
                }
                if (pahseNo >= 500 && pahseNo < 525) {
                    record.setMainPhase(EnumZengguoMainPhase.LIUJIU);
                }
                if (pahseNo >= 525 && pahseNo < 530) {
                    record.setMainPhase(EnumZengguoMainPhase.ZHENGZHU_CHONGSUAN);
                }
                if (pahseNo >= 530 && pahseNo <= 700) {
                    record.setMainPhase(EnumZengguoMainPhase.POST_DEAL);
                }

                Date recordStartTime = record.getPhaseStartTimeTotal();
                Date recordEndTime = record.getPhaseEndTimeTotal() == null ? new Date() : record.getPhaseEndTimeTotal();
                List<MesZengguoRealDataV2> realDataV2sForTheMiniRecorde = realDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(recordStartTime) >= 0 && e.getWinccUpdateTime().compareTo(recordEndTime) < 0)
                        .toList();
                for (MesZengguoRealDataV2 mesZengguoRealDataV2 : realDataV2sForTheMiniRecorde) {
                    mesZengguoRealDataV2.setMesZengguoMiniRecord(record);
                }
            }

            totalRecordList.addAll(allRecordList);
            mesZengguoRealDataList.addAll(realDataList);
        }

        // 上甑曲线
        getShangzengQuxian(mesZengguoRecordList, mesZengguoRealDataList,totalRecordList);

        // 溜酒曲线
        getLiujiuQuxian(mesZengguoRecordList, mesZengguoRealDataList,totalRecordList);

        // 按照wincc 实际进行溜酒
//        getLiujiuQuxianV2(mesZengguoRecordList, mesZengguoRealDataList,totalRecordList);

        // 蒸煮曲线
        getZhengzhuQuxian(mesZengguoRecordList, mesZengguoRealDataList,totalRecordList);

        // 摊晾曲线
        getTanliangQuxian(mesZengguoRecordList);

    }

    private void getTanliangQuxian(List<MesZengguoRecord> zengguoRecords) {
        for (MesZengguoRecord zengguoRecord : zengguoRecords) {
            Date phaseEndTimeTotal = zengguoRecord.getEndTimeTall();
            Date minEnd = winccDataDealCommons.convertStringToDate("2022/10/30 00:01:00");
            if(phaseEndTimeTotal.before(minEnd)){
                continue;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(phaseEndTimeTotal);
            calendar.add(Calendar.HOUR, 4);
            Date phaseMaxEndTime = calendar.getTime();
            List<MesTanliangjiRecord> mesTanliangjiRecordList = dataManager.load(MesTanliangjiRecord.class)
                    .query("select e from MesTanliangjiRecord e " +
                            "where e.resourceZengguo is not null " +
                            "and e.zengSequence = :zengSequence " +
                            "and e.resourceZengguo = :mesZengguo " +
                            "and e.phaseStartTime <= :phaseMaxEndTime " +
                            "order by e.phaseStartTime desc ")
                    .parameter("zengSequence", zengguoRecord.getZengSequence())
                    .parameter("mesZengguo", zengguoRecord.getMesZengguo())
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
                    + "甑锅：" + zengguoRecord.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + zengguoRecord.getZengSequence() + "\n"
                    + "每一个间隔代表30秒中的数据";
            Title title = new Title().withText( titleStr).withShow(true);
            chart.withTitle(title);

            MesTanliangjiRecord mesTanliangjiRecord = mesTanliangjiRecordList.getFirst();


            Date tanliangStartTime = mesTanliangjiRecord.getPhaseStartTime();
            Date tanliangEndTime = mesTanliangjiRecord.getPhaseEndTime();
            if(zengguoRecord.getSumDataRecord()!= null ){
                Date minDate = winccDataDealCommons.convertStringToDate("2022/10/30 00:01:00");
                if(zengguoRecord.getSumDataRecord().getTanliangStartTime().compareTo(minDate) > 0 && zengguoRecord.getSumDataRecord().getTanliangEndTime().compareTo(minDate) > 0){
                    tanliangStartTime = zengguoRecord.getSumDataRecord().getTanliangStartTime();
                    tanliangEndTime = zengguoRecord.getSumDataRecord().getTanliangEndTime();
                }
            }

            List<MesTanliangRealDataV2> mesTanliangRealDataList = dataManager.load(MesTanliangRealDataV2.class)
                    .query("select e from MesTanliangRealDataV2 e " +
                            "where e.mesArea = :mesArea " +
                            "and e.mesTanliangji = :mesTanliangji " +
                            "and e.winccUpdateTime >= :phaseStartTime " +
                            "and e.winccUpdateTime <= :phaseEndTime " +
                            "order by e.winccUpdateId")
                    .parameter("mesArea", zengguoRecord.getMesZengguo().getMesArea())
                    .parameter("mesTanliangji", mesTanliangjiRecord.getMesTanliangji())
                    .parameter("phaseStartTime", tanliangStartTime)
                    .parameter("phaseEndTime", tanliangEndTime)
                    .list();
            // mesTanliangRealDataList从第一大于4的开始截取
            int startIndex = 0;
            for(int i=0;i<mesTanliangRealDataList.size();i++){
                MesTanliangRealDataV2 mesTanliangRealData = mesTanliangRealDataList.get(i);
                if(mesTanliangRealData.getTangliangGeiliaojiLiusu() > 5){
                    startIndex = i;
                    break;
                }
            }
            mesTanliangRealDataList = mesTanliangRealDataList.subList(startIndex,mesTanliangRealDataList.size());

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
            YAxis yAxis = new YAxis().withName("糟醅流速").withPosition(AbstractCartesianAxis.Position.LEFT).withOffset(-50);
            chart.withYAxis(yAxis);
            chart.withYAxis(new YAxis().withName("曲粉流速").withPosition(AbstractCartesianAxis.Position.LEFT));
            chart.withYAxis(new YAxis().withName("温度").withMin("8").withMax("28").withPosition(AbstractCartesianAxis.Position.RIGHT));

            LineSeries lineSeries = new LineSeries().withName("摊晾给料机流速").withYAxisIndex(0);
            LineSeries lineSeries1 = new LineSeries().withName("摊晾曲粉流速").withYAxisIndex(1);
            LineSeries lineSeries2 = new LineSeries().withName("摊晾中部温度").withYAxisIndex(2);
            LineSeries lineSeries3 = new LineSeries().withName("摊晾出口温度").withYAxisIndex(2);
            chart.withSeries(lineSeries,lineSeries1,lineSeries2,lineSeries3);
            shangzengRealDataVerticalLayout.add(chart);
        }
    }

    private void getZhengzhuQuxian(List<MesZengguoRecord> zengguoRecords, List<MesZengguoRealDataV2> mesZengguoRealDataList, List<MesZengguoRecord> totalRecords) {
        for (MesZengguoRecord zengguoRecord : zengguoRecords) {
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("30em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            List<MesZengguoRecord> recordZhengzhu = totalRecords.stream()
                    .filter(e -> e.getRelatedRecordId().equals(zengguoRecord.getId()))
                    .filter(e -> e.getMainPhase().equals(EnumZengguoMainPhase.ZHENGZHU_CHONGSUAN))
                    .sorted(Comparator.comparing(MesZengguoRecord::getPhaseStartTimeTotal))
                    .toList();
            Date minStartTime = recordZhengzhu.stream().
                    map(MesZengguoRecord::getPhaseStartTimeTotal)
                    .min(Date::compareTo).orElse(new Date());
            Date maxEndTime = recordZhengzhu.stream().
                    map(MesZengguoRecord::getPhaseEndTimeTotal)
                    .max(Date::compareTo).orElse(new Date());

            List<MesZengguoRealDataV2> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoRecord().equals(zengguoRecord))
                    .filter(e ->e.getWinccUpdateTime().compareTo(minStartTime) >= 0 &&
                            e.getWinccUpdateTime().compareTo(maxEndTime) <= 0)
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();

            for(int i=0;i<mesZengguoRealDataForUpList.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataForUpList.get(i);
                mesZengguoRealData.setTempIndex(i);
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }
            String titleStr = "蒸煮阶段状况：" + "\n "
                    + "甑锅：" + zengguoRecord.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + zengguoRecord.getZengSequence() + "\n"
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
            YAxis yAxis = new YAxis().withName("压力").withMin("-0.01").withPosition(AbstractCartesianAxis.Position.LEFT);
            chart.withYAxis(yAxis);
            chart.withYAxis(new YAxis().withName("温度").withMin("40").withPosition(AbstractCartesianAxis.Position.RIGHT));

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

    private void getLiujiuQuxian(List<MesZengguoRecord> mesZengguoRecords, List<MesZengguoRealDataV2> mesZengguoRealDataList,List<MesZengguoRecord> totalRecords) {
        for (MesZengguoRecord zengguoRecord : mesZengguoRecords) {
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("40em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            List<Integer> liujiuList = Arrays.asList(520,521,522,523,524);

            List<MesZengguoRecord> recordLiujiu = totalRecords.stream()
                    .filter(e -> e.getRelatedRecordId().equals(zengguoRecord.getId()))
                    .filter(e -> e.getMainPhase().equals(EnumZengguoMainPhase.LIUJIU))
                    .sorted(Comparator.comparing(MesZengguoRecord::getPhaseStartTimeTotal))
                    .toList();
            Date minStartTime = recordLiujiu.stream().
                    map(MesZengguoRecord::getPhaseStartTimeTotal)
                    .min(Date::compareTo).orElse(new Date());
            Date maxEndTime = recordLiujiu.stream().
                    map(MesZengguoRecord::getPhaseEndTimeTotal)
                    .max(Date::compareTo).orElse(new Date());

            List<MesZengguoRealDataV2> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoRecord().equals(zengguoRecord))
                    .filter(e ->e.getWinccUpdateTime().compareTo(minStartTime) >= 0 &&
                            e.getWinccUpdateTime().compareTo(maxEndTime) < 0)
                    .filter(e -> liujiuList.contains(e.getMesZengguoMiniRecord().getZengguoPhase().getPhaseNo()))
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();

            // 对于mesZengguoRealDataForUpList中的每一条记录中的甑锅温度，如果开始大于100，则其后续的数据remove 出 list
            int startIndex = 0;
            for(int i=0;i<mesZengguoRealDataForUpList.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataForUpList.get(i);
                if(mesZengguoRealData.getGuoniWendu() > 100){
                    startIndex = i;
                    break;
                }
            }
            if(startIndex > 0){
                mesZengguoRealDataForUpList = mesZengguoRealDataForUpList.subList(0,startIndex);
            }
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
                MesZengguoRecord mesZengguoRecord = mesZengguoRealData.getMesZengguoMiniRecord();
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
//                        thirdClassEnd = i;
                        break;
                    case 524: // 酒尾
                        if(jiuweiStart == -1){
                            jiuweiStart = i;
                            if(jiutouEnd == -1){
                                jiutouEnd = i - 1;
                            }
                            if(firstClassEnd == -1){
                                firstClassEnd = i - 1;
                            }
                            if(secondClassEnd == -1){
                                secondClassEnd = i - 1;
                            }
                            if(thirdClassEnd == -1){
                                thirdClassEnd = i - 1;
                            }
                        }
                        jiuweiEnd = i;
                        break;
                }
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }

            int initialSize = chartItems.getItems().size();
            if(initialSize <= 120){
                int dev = 120 - initialSize;
                for(int i = 0; i < dev; i++){
                    MesZengguoRealDataV2 mesZengguoRealData = dataManager.create(MesZengguoRealDataV2.class);
                    mesZengguoRealData.setTempIndex(initialSize + i);
                    chartItems.addItem(new EntityDataItem(mesZengguoRealData));
                }
            }

            // Create a SimpleDateFormat instance with the desired format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // Format the date
            String formattedDate = dateFormat.format(zengguoRecord.getStartTimeKagai());

            String chartTitle = "接酒阶段状况：" + "\n "
                    +"甑锅：" + zengguoRecord.getMesZengguo().getZengguoName() + "\n"
                    +"甑序：" + zengguoRecord.getZengSequence() + "   " + formattedDate +"\n " +
                    "装甑效率：" + String.format("%.3f", zengguoRecord.getShangzengXiaolv()) + "\n" +
                    "一二级斜率：" + String.format("%.3f", zengguoRecord.getShangzengXielv()) + "\n" +
                    "二级斜率：" + String.format("%.3f", zengguoRecord.getShangzengSecondClassXielv()) + "\n" +
                    "二级三级斜率：" + String.format("%.3f", zengguoRecord.getShangzengSecondThirdXielv()) + "\n" +
                    "三级斜率：" + String.format("%.3f", zengguoRecord.getShangzengThirdClassXielv()) + "\n" +
                    "一至三级斜率：" + String.format("%.3f", zengguoRecord.getShangzengfirstThirdXielv()) + "\n" +
                    "各级酒级时长：" + String.format("%.3f", zengguoRecord.getJiejiuDurationFirstClass()) + " - " + String.format("%.3f", zengguoRecord.getJiejiuDurationSecondClass())+ " - "
                    + String.format("%.3f", zengguoRecord.getJiejiuDurationThirdClass()) + "\n" ;

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

            chart.withYAxis(new YAxis().withMax("30").withMin("15").withName("出酒温度").withPosition(AbstractCartesianAxis.Position.RIGHT));

            chart.withYAxis(new YAxis().withMax("102").withMin("80").withName("锅内温度").withPosition(AbstractCartesianAxis.Position.RIGHT).withOffset(50));

            LineSeries lineSeries3 = new LineSeries().withName("蒸汽压力").withYAxisIndex(0);

            LineSeries lineSeries5 = new LineSeries().withName("锅内温度").withYAxisIndex(2);
            LineSeries lineSeries6 = new LineSeries().withName("出酒温度").withYAxisIndex(1);
            LineSeries lineSeries7 = new LineSeries().withName("底锅水温").withYAxisIndex(2);
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
            chart.withSeries(lineSeries3,lineSeries5,lineSeries6);

            shangzengRealDataVerticalLayout.add(chart);
        }
    }


    private void getLiujiuQuxianV2(List<MesZengguoRecord> mesZengguoRecords, List<MesZengguoRealDataV2> mesZengguoRealDataList,List<MesZengguoRecord> totalRecords) {
        for (MesZengguoRecord zengguoRecord : mesZengguoRecords) {
            MesZengguoRecord zhengzhuRecord = totalRecords.stream()
                    .filter(e -> e.getRelatedRecordId().equals(zengguoRecord.getId()))
//                    .filter(e -> e.getMainPhase().equals(EnumZengguoMainPhase.ZHENGZHU_CHONGSUAN))
                    .filter(e -> e.getZengguoPhase().getPhaseNo().equals(525))
                    .min(Comparator.comparing(MesZengguoRecord::getPhaseStartTimeTotal))
                    .orElse(null);
            if(zhengzhuRecord == null){
                return;
            }
            // 卡盘溜酒开始时间
            Date kapanLiujiuStartTime = zhengzhuRecord.getStartTimeKagai();
            // 溜酒结束时间
            Date liujiuEndTime = zhengzhuRecord.getEndTimeLiujiu();
            // 计算开始结束总时长
            float liujiuTotalDuration = (float) ((liujiuEndTime.getTime() - kapanLiujiuStartTime.getTime()) / 1000) / 60;
            // 酒头溜酒时长(分钟为单位)
            float jiutouDuration = liujiuTotalDuration - (zhengzhuRecord.getJiejiuDurationFirstClass() + zhengzhuRecord.getJiejiuDurationSecondClass()
                    + zhengzhuRecord.getJiejiuDurationThirdClass() + zhengzhuRecord.getJiejiuDurationFeishui()) ;
            // 一级酒开始时间 = 卡盘溜酒开始时间 +  酒头溜酒时长
            Date firstClassStartTime = new Date(kapanLiujiuStartTime.getTime() + (long) (jiutouDuration * 60 * 1000));
            Date secondClassStartTime = new Date(firstClassStartTime.getTime() + (long) (zhengzhuRecord.getJiejiuDurationFirstClass() * 60 * 1000));
            Date thirdClassStartTime = new Date(secondClassStartTime.getTime() + (long) (zhengzhuRecord.getJiejiuDurationSecondClass() * 60 * 1000));
            Date jiuweiStartTime = new Date(thirdClassStartTime.getTime() + (long) (zhengzhuRecord.getJiejiuDurationThirdClass() * 60 * 1000));

            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("30em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();

            List<MesZengguoRealDataV2> mesZengguoRealDataV2List = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguo().equals(zengguoRecord.getMesZengguo()))
                    .filter(e -> e.getWinccUpdateTime().compareTo(kapanLiujiuStartTime) >= 0 &&
                            e.getWinccUpdateTime().compareTo(liujiuEndTime) <= 0)
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();

            int jiutouStart = -1;
            int jiutouEnd = -1;
            int firstClassStart = -1;
            int firstClassEnd = -1;
            int secondClassStart = -1;
            int secondClassEnd = -1;
            int thirdClassStart = -1;
            int thirdClassEnd = -1;
            int jiuweiStart = -1;
            int jiuweiEnd = -1;
            for(int i=0;i<mesZengguoRealDataV2List.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataV2List.get(i);
                mesZengguoRealData.setTempIndex2(i);
                Date winccUpdateTime = mesZengguoRealData.getWinccUpdateTime();
                int jiuGrade = -1;
                if(winccUpdateTime.compareTo(kapanLiujiuStartTime) >= 0 && winccUpdateTime.compareTo(firstClassStartTime) < 0){
                    jiuGrade = 0;
                }
                if(winccUpdateTime.compareTo(firstClassStartTime) >= 0 && winccUpdateTime.compareTo(secondClassStartTime) < 0){
                    jiuGrade = 1;
                }
                if(winccUpdateTime.compareTo(secondClassStartTime) >= 0 && winccUpdateTime.compareTo(thirdClassStartTime) < 0){
                    jiuGrade = 2;
                }
                if(winccUpdateTime.compareTo(thirdClassStartTime) >= 0 && winccUpdateTime.compareTo(jiuweiStartTime) < 0){
                    jiuGrade = 3;
                }
                if (winccUpdateTime.compareTo(jiuweiStartTime) >= 0 && winccUpdateTime.compareTo(liujiuEndTime) <= 0){
                    jiuGrade = 4;
                }
                switch (jiuGrade){
                    case 0: // 酒头
                        if(jiutouStart == -1){jiutouStart = i;}
                        break;
                    case 1: // 一级
                        if(firstClassStart == -1){
                            firstClassStart = i;
                            if(jiutouEnd == -1){
                                jiutouEnd = i - 1;
                            }
                        }
                        break;
                    case 2: // 二级
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
                    case 3: // 三级
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
//                        thirdClassEnd = i;
                        break;
                    case 4: // 酒尾
                        if(jiuweiStart == -1){
                            jiuweiStart = i;
                            if(jiutouEnd == -1){
                                jiutouEnd = i - 1;
                            }
                            if(firstClassEnd == -1){
                                firstClassEnd = i - 1;
                            }
                            if(secondClassEnd == -1){
                                secondClassEnd = i - 1;
                            }
                            if(thirdClassEnd == -1){
                                thirdClassEnd = i - 1;
                            }
                        }
                        jiuweiEnd = i;
                        break;
                }
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }

            int initialSize = chartItems.getItems().size();
            if(initialSize <= 120){
                int dev = 120 - initialSize;
                for(int i = 0; i < dev; i++){
                    MesZengguoRealDataV2 mesZengguoRealData = dataManager.create(MesZengguoRealDataV2.class);
                    mesZengguoRealData.setTempIndex2(initialSize + i);
                    chartItems.addItem(new EntityDataItem(mesZengguoRealData));
                }
            }
            // Create a SimpleDateFormat instance with the desired format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            // Format the date
            String formattedDate = dateFormat.format(zengguoRecord.getStartTimeKagai());

            String chartTitle = "接酒阶段状况：" + "\n "
                    + "甑锅：" + zengguoRecord.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + zengguoRecord.getZengSequence() + "   " + formattedDate +"\n " +
                    " 装甑效率：" + String.format("%.3f", zengguoRecord.getShangzengXiaolv()) + "\n" +
                    " 一二级斜率：" + String.format("%.3f", zengguoRecord.getShangzengXielv()) + "\n" +
                    "各级酒级时长：" + String.format("%.3f", zengguoRecord.getJiejiuDurationFirstClass()) + " - " + String.format("%.3f", zengguoRecord.getJiejiuDurationSecondClass())+ " - "
                    + String.format("%.3f", zengguoRecord.getJiejiuDurationThirdClass()) ;

            Title title = new Title().withText(chartTitle).withShow(true);
            chart.setTitle(title);

            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("tempIndex2")
//                                    .withValueFields("zhengqiKaidu","zhengqiShunshiLiuliang","lengningKaidu","zhengqiYali","guoniWendu","chujiuWendu","diguoShuiwen")
                                    .withValueFields("zhengqiYali","guoniWendu","diguoShuiwen")
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

//            chart.withYAxis(new YAxis().withMax("30").withMin("15").withName("出酒温度").withPosition(AbstractCartesianAxis.Position.RIGHT));

            chart.withYAxis(new YAxis().withMax("102").withMin("80").withName("锅内温度").withPosition(AbstractCartesianAxis.Position.RIGHT).withOffset(50));

            LineSeries lineSeries3 = new LineSeries().withName("蒸汽压力").withYAxisIndex(0);

            LineSeries lineSeries5 = new LineSeries().withName("锅内温度").withYAxisIndex(1);
//            LineSeries lineSeries6 = new LineSeries().withName("出酒温度").withYAxisIndex(1);
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
            chart.withSeries(lineSeries3,lineSeries5,lineSeries7);

            shangzengRealDataVerticalLayout.add(chart);
        }
    }

    private void getShangzengQuxian(List<MesZengguoRecord> mesZengguoRecords, List<MesZengguoRealDataV2> mesZengguoRealDataList,List<MesZengguoRecord> totalRecords) {
        for (MesZengguoRecord mesZengguoRecord : mesZengguoRecords) {
            // 为每一个mesZengguoUnitProcedure增加一个echart的折线图
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("30em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            List<MesZengguoRecord> recordShangzeng = totalRecords.stream()
                    .filter(e -> e.getRelatedRecordId().equals(mesZengguoRecord.getId()))
                    .filter(e -> e.getMainPhase().equals(EnumZengguoMainPhase.SHANGZENG))
                    .sorted(Comparator.comparing(MesZengguoRecord::getPhaseStartTimeTotal))
                    .toList();
            Date minStartTime = recordShangzeng.stream().
                    map(MesZengguoRecord::getPhaseStartTimeTotal)
                    .min(Date::compareTo).orElse(new Date());
            Date maxEndTime = recordShangzeng.stream().
                    map(MesZengguoRecord::getPhaseEndTimeTotal)
                    .max(Date::compareTo).orElse(new Date());

            List<MesZengguoRealDataV2> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoRecord().equals(mesZengguoRecord))
                    .filter(e ->e.getWinccUpdateTime().compareTo(minStartTime) >= 0 &&
                            e.getWinccUpdateTime().compareTo(maxEndTime) <= 0)
                    .sorted(Comparator.comparing(MesZengguoRealDataV2::getWinccUpdateTime))
                    .toList();
            for(int i=0;i<mesZengguoRealDataForUpList.size();i++){
                MesZengguoRealDataV2 mesZengguoRealData = mesZengguoRealDataForUpList.get(i);
                mesZengguoRealData.setTempIndex(i);
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }
            String titleStr = "上甑阶段状况：" + "\n "
                    + "甑锅：" + mesZengguoRecord.getMesZengguo().getZengguoName() + "\n"
                    + "甑序：" + mesZengguoRecord.getZengSequence() + "\n"
                    + "每一个间隔代表30秒中的数据";
            Title title = new Title().withText( titleStr).withShow(true);
            chart.withTitle(title);

            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("tempIndex")
//                                    .withValueFields("zhengqiKaidu","zhengqiShunshiLiuliang","zhengqiYali","guoniWendu","diguoShuiwen","mesZengguoRecord.shangzengLayer")
                                    .withValueFields("zhengqiYali","guoniWendu","diguoShuiwen","mesZengguoMiniRecord.shangzengLayer")
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
