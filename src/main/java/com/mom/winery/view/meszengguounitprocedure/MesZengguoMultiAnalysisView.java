package com.mom.winery.view.meszengguounitprocedure;

import com.mom.winery.entity.*;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.chartsflowui.component.Chart;
import io.jmix.chartsflowui.data.item.EntityDataItem;
import io.jmix.chartsflowui.kit.component.model.DataSet;
import io.jmix.chartsflowui.kit.component.model.Tooltip;
import io.jmix.chartsflowui.kit.component.model.axis.AbstractCartesianAxis;
import io.jmix.chartsflowui.kit.component.model.axis.XAxis;
import io.jmix.chartsflowui.kit.component.model.axis.YAxis;
import io.jmix.chartsflowui.kit.component.model.legend.Legend;
import io.jmix.chartsflowui.kit.component.model.series.BarSeries;
import io.jmix.chartsflowui.kit.component.model.series.Label;
import io.jmix.chartsflowui.kit.component.model.series.LineSeries;
import io.jmix.chartsflowui.kit.component.model.shared.AbstractAxisPointer;
import io.jmix.chartsflowui.kit.component.model.shared.AbstractTooltip;
import io.jmix.chartsflowui.kit.data.chart.ListChartItems;
import io.jmix.core.DataManager;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.facet.DataLoadCoordinator;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import io.jmix.reportsflowui.view.reportwizard.template.generators.ChartGenerator;
import org.springframework.beans.factory.annotation.Autowired;

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

    public List<Long> getUnitProcedureIds() {
        return unitProcedureIds;
    }
    public void setUnitProcedureIds(List<Long> unitProcedureIds) {
        this.unitProcedureIds = unitProcedureIds;
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Map<String,Object> mesZengguoUnitProceduresDlParams = new HashMap<>();
        mesZengguoUnitProceduresDlParams.put("unitProcedureIds", unitProcedureIds);
        mesZengguoUnitProceduresDl.setParameters(mesZengguoUnitProceduresDlParams);
        mesZengguoUnitProceduresDl.load();


    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList = mesZengguoUnitProceduresDl.getContainer().getItems();
        List<MesZengguoRealData> mesZengguoRealDataList = new ArrayList<>();
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            MesArea mesArea = mesZengguoUnitProcedure.getMesZengguo().getMesArea();
            List<MesZengguoRealData> realDataList = dataManager.load(MesZengguoRealData.class)
                    .query("select e from MesZengguoRealData e " +
                            "where e.mesArea = :mesArea " +
                            "and e.winccUpdateTime >= :winccStartTime " +
                            "and e.winccUpdateTime <= :winccEndTime " +
                            "order by e.winccUpdateId")
                    .parameter("mesArea", mesArea)
                    .parameter("winccStartTime", mesZengguoUnitProcedure.getPhaseStartTimeTotal())
                    .parameter("winccEndTime", mesZengguoUnitProcedure.getPhaseEndTimeTotal())
                    .list();
            realDataList.forEach(e -> {
                e.setMesZengguoUnitProcedure(mesZengguoUnitProcedure);
                e.setMesZengguo(mesZengguoUnitProcedure.getMesZengguo());
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
                List<MesZengguoRealData> operationRealDataList = realDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(mesZengguoOperation.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(mesZengguoOperation.getPhaseEndTimeTotal()) <= 0)
                        .toList();
                operationRealDataList.forEach(e -> {
                    e.setMesZenguoOperation(mesZengguoOperation);
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
                List<MesZengguoRealData> recordRealDataList = realDataList.stream()
                        .filter(e -> e.getWinccUpdateTime().compareTo(mesZengguoRecord.getPhaseStartTimeTotal()) >= 0
                                && e.getWinccUpdateTime().compareTo(mesZengguoRecord.getPhaseEndTimeTotal()) <= 0)
                        .toList();
                recordRealDataList.forEach(e -> {
                    e.setMesZenguoRecord(mesZengguoRecord);
                });
            }
            mesZengguoRealDataList.addAll(realDataList);
        }
        for (MesZengguoUnitProcedure mesZengguoUnitProcedure : mesZengguoUnitProcedureList) {
            // 为每一个mesZengguoUnitProcedure增加一个echart的折线图
            Chart chart = uiComponents.create(Chart.class);
            chart.setMinHeight("20em");
            ListChartItems<EntityDataItem> chartItems = new ListChartItems<>();
            List<MesZengguoRealData> mesZengguoRealDataForUpList = mesZengguoRealDataList.stream()
                    .filter(e -> e.getMesZengguoUnitProcedure().equals(mesZengguoUnitProcedure))
                    .filter(e ->e.getMesZenguoOperation().getMainPhase().equals(EnumZengguoMainPhase.SHANGZENG))
                    .sorted(Comparator.comparing(MesZengguoRealData::getWinccUpdateTime))
                    .toList();
            for (MesZengguoRealData mesZengguoRealData : mesZengguoRealDataForUpList) {
                chartItems.addItem(new EntityDataItem(mesZengguoRealData));
            }

            chart.withDataSet(
                    new DataSet().withSource(
                            new DataSet.Source<EntityDataItem>()
                                    .withDataProvider(chartItems)
                                    .withCategoryField("winccUpdateTime")
                                    .withValueFields("zhengqiKaidu12","lengningKaidu2","mesZenguoRecord.shangzengLayer")
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
            YAxis yAxis = new YAxis().withName("蒸汽开度11");
            chart.withYAxis(yAxis);
            chart.withYAxis(new YAxis().withName("蒸汽开度12"));

            chart.withYAxis(new YAxis().withName("上甑层数").withPosition(AbstractCartesianAxis.Position.RIGHT));

            LineSeries lineSeries = new LineSeries();
            lineSeries.setName("蒸汽开度");
//            lineSeries.setLabel(new Label().withFormatter("{c}").withPosition(Label.Position.PositionType.INSIDE_TOP_LEFT).withShow(true));
            LineSeries lineSeries1 = new LineSeries().withName("冷凝开度")
                    .withYAxisIndex(1);
            BarSeries barSeries = new BarSeries().withName("上甑层数").withYAxisIndex(2);
            chart.withSeries(lineSeries,lineSeries1,barSeries);
            shangzengRealDataVerticalLayout.add(chart);
        }
    }



}
