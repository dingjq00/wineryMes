package com.mom.winery.view.mesteamarrange;

import com.mom.winery.entity.*;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/20 12:24
 */

@Route(value = "mesTeamArranges", layout = MainView.class)
@ViewController(id = "MesTeamArrange.list")
@ViewDescriptor(path = "mes-team-arrange-list-view.xml")
@LookupComponent("mesTeamArrangesDataGrid")
@DialogMode(width = "64em")
public class MesTeamArrangeListView extends StandardListView<MesTeamArrange> {
    @Autowired
    private DataManager dataManager;

    @Subscribe(id = "set11shopfloorShiftToZengRecord", subject = "clickListener")
    public void onSet11shopfloorShiftToZengRecordClick(final ClickEvent<JmixButton> event) {
        MesShopfloor shopfloor = dataManager.load(MesShopfloor.class)
                .query("select e from MesShopfloor e " +
                        "where e.mesShopfloorName = '11车间'")
                .one();
        List<MesZengguoRecord> zengguoRecordList = dataManager.load(MesZengguoRecord.class)
                .query("select e from MesZengguoRecord e where e.shiftTeam is null " +
                        "and e.mesZengguo.mesArea.mesShopfloor = :shopfloor " )
                .parameter("shopfloor", shopfloor)
                .maxResults(100000)
                .list();
        setZengguoRecordShiftTeam(shopfloor, zengguoRecordList);

    }

    @Subscribe(id = "setShiftToZengRecord", subject = "clickListener")
    public void onSetShiftToZengRecordClick(final ClickEvent<JmixButton> event) {

        MesShopfloor shopfloor = dataManager.load(MesShopfloor.class)
                .query("select e from MesShopfloor e " +
                        "where e.mesShopfloorName = '12车间'")
                .one();
        List<MesZengguoRecord> zengguoRecordList = dataManager.load(MesZengguoRecord.class)
                .query("select e from MesZengguoRecord e where e.shiftTeam is null " +
                        "and e.mesZengguo.mesArea.mesShopfloor = :shopfloor " )
                .parameter("shopfloor", shopfloor)
                .maxResults(100000)
                .list();
        setZengguoRecordShiftTeam(shopfloor, zengguoRecordList);
    }


    @Subscribe(id = "set13shopfloorShiftToZengRecord", subject = "clickListener")
    public void onSet13shopfloorShiftToZengRecordClick(final ClickEvent<JmixButton> event) {
        MesShopfloor shopfloor = dataManager.load(MesShopfloor.class)
                .query("select e from MesShopfloor e " +
                        "where  e.mesShopfloorName = '13车间'")
                .one();
        List<MesZengguoRecord> zengguoRecordList = dataManager.load(MesZengguoRecord.class)
                .query("select e from MesZengguoRecord e where e.shiftTeam is null and " +
                        " e.mesZengguo.mesArea.mesShopfloor = :shopfloor " )
                .parameter("shopfloor", shopfloor)
                .maxResults(100000)
                .list();
        setZengguoRecordShiftTeam(shopfloor, zengguoRecordList);
    }

    private void setZengguoRecordShiftTeam(MesShopfloor shopfloor, List<MesZengguoRecord> zengguoRecordList) {
        List<MesTeamArrange> teamArrangeList = dataManager.load(MesTeamArrange.class)
                .query("select e from MesTeamArrange e " +
                        "where e.isActive = true " +
                        "and e.mesShopfloor = :shopfloor " +
                        "order by e.periodStartDateStr desc ")
                .parameter("shopfloor", shopfloor)
                .list();
        List<MesShiftTime> shiftTimeList = dataManager.load(MesShiftTime.class)
                .query("select e from MesShiftTime e " +
                        "where e.isActive = true " +
                        "and e.mesShopfloor = :shopfloor " +
                        "order by e.startTimeStr desc" )
                .parameter("shopfloor", shopfloor)
                .list();
//        // 将zengguoRecordList按照每组1000个进行分批处理
        for (int i = 0; i < zengguoRecordList.size(); i += 1000) {

            List<MesZengguoRecord> zengguoRecordListBatch = zengguoRecordList.subList(i, Math.min(i + 1000, zengguoRecordList.size()));
            for (MesZengguoRecord zengguoRecord : zengguoRecordListBatch) {
                String recordStartDate = "";
                String recordStartHour = "";
                String recordStartMinute = "";
                Date recordDate = null;
                String recordDateStr = "";
                String recordDateAllstr = "";
                MesTeamArrange teamArrange = null;

                recordDate = zengguoRecord.getPhaseStartTimeTotal();
                // 获取recordDate的日时分
                recordDateStr = recordDate.toString();
                recordStartDate = recordDateStr.substring(8, 10);
                recordStartHour = recordDateStr.substring(11, 13);
                recordStartMinute =recordDateStr.substring(14,16);
                recordDateAllstr = recordStartDate + recordStartHour +  recordStartMinute;
                String finalRecordDateAllstr = recordDateAllstr;
                teamArrange = teamArrangeList.stream()
                        .filter(e -> e.getPeriodStartDateStr().compareTo(finalRecordDateAllstr) <= 0
                                && e.getPeriodEndDateStr().compareTo(finalRecordDateAllstr) >= 0)
                        .findFirst()
                        .orElse(null);
                if(teamArrange == null){
                    teamArrange = teamArrangeList.getFirst();
                }
                String hourMinuteStr = recordStartHour +  recordStartMinute;

                MesShiftTime shiftTime = shiftTimeList.stream()
                        .filter(e -> e.getStartTimeStr().compareTo(hourMinuteStr) <= 0
                                && e.getEndTimeStr().compareTo(hourMinuteStr) >= 0)
                        .findFirst()
                        .orElse(null);
                if(shiftTime == null){
                    shiftTime = shiftTimeList.getFirst();
                }
                MesShiftTeam shiftTeam = switch (shiftTime.getEnumShift()) {
                    case EnumShiftConfig.DAY_SHIFT -> teamArrange.getDayShiftTeam();
                    case EnumShiftConfig.SHORT_NIGHT_SHIFT -> teamArrange.getShortNightTeam();
                    case EnumShiftConfig.LONG_NIGHT_SHIFT -> teamArrange.getLongNightTeam();
                };

                zengguoRecord.setShiftTeam(shiftTeam);
                zengguoRecord.setEnumShift(shiftTime.getEnumShift());
            }
            dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(zengguoRecordListBatch));
        }
    }

    @Subscribe(id = "setTanliangShiftToZengRecord", subject = "clickListener")
    public void onSetTanliangShiftToZengRecordClick(final ClickEvent<JmixButton> event) {
        List<MesShopfloor> shopfloorList = dataManager.load(MesShopfloor.class)
                .query("select e from MesShopfloor e ")
                .list();
        for (MesShopfloor mesShopfloor : shopfloorList) {
            List<MesTanliangjiRecord> tanliangjiRecordList = dataManager.load(MesTanliangjiRecord.class)
                    .query("select e from MesTanliangjiRecord e where e.shiftTeam is null " +
                            "and e.mesTanliangji.mesArea.mesShopfloor = :shopfloor " )
                    .parameter("shopfloor", mesShopfloor)
                    .maxResults(10000)
                    .list();

            List<MesTeamArrange> teamArrangeList = dataManager.load(MesTeamArrange.class)
                    .query("select e from MesTeamArrange e " +
                            "where e.isActive = true " +
                            "and e.mesShopfloor = :shopfloor " +
                            "order by e.periodStartDateStr desc ")
                    .parameter("shopfloor", mesShopfloor)
                    .list();
            List<MesShiftTime> shiftTimeList = dataManager.load(MesShiftTime.class)
                    .query("select e from MesShiftTime e " +
                            "where e.isActive = true " +
                            "and e.mesShopfloor = :shopfloor " +
                            "order by e.startTimeStr desc" )
                    .parameter("shopfloor", mesShopfloor)
                    .list();
            setTanliangRecordToShift(tanliangjiRecordList, teamArrangeList, shiftTimeList);
        }
    }

    private void setTanliangRecordToShift(List<MesTanliangjiRecord> tanliangjiRecordList, List<MesTeamArrange> teamArrangeList, List<MesShiftTime> shiftTimeList) {
        //        // 将zengguoRecordList按照每组1000个进行分批处理
        for (int i = 0; i < tanliangjiRecordList.size(); i += 1000) {
            List<MesTanliangjiRecord> tanliangRecordListBatch = tanliangjiRecordList.subList(i, Math.min(i + 1000, tanliangjiRecordList.size()));
            for (MesTanliangjiRecord tanliangjiRecord : tanliangRecordListBatch) {
                String recordStartDate = "";
                String recordStartHour = "";
                String recordStartMinute = "";
                Date recordDate = null;
                String recordDateStr = "";
                String recordDateAllstr = "";
                MesTeamArrange teamArrange = null;

                recordDate = tanliangjiRecord.getPhaseStartTime();
                // 获取recordDate的日时分
                recordDateStr = recordDate.toString();
                recordStartDate = recordDateStr.substring(8, 10);
                recordStartHour = recordDateStr.substring(11, 13);
                recordStartMinute =recordDateStr.substring(14,16);
                recordDateAllstr = recordStartDate + recordStartHour +  recordStartMinute;
                String finalRecordDateAllstr = recordDateAllstr;
                teamArrange = teamArrangeList.stream()
                        .filter(e -> e.getPeriodStartDateStr().compareTo(finalRecordDateAllstr) <= 0
                                && e.getPeriodEndDateStr().compareTo(finalRecordDateAllstr) >= 0)
                        .findFirst()
                        .orElse(null);
                if(teamArrange == null){
                    teamArrange = teamArrangeList.getFirst();
                }
                String hourMinuteStr = recordStartHour +  recordStartMinute;

                MesShiftTime shiftTime = shiftTimeList.stream()
                        .filter(e -> e.getStartTimeStr().compareTo(hourMinuteStr) <= 0
                                && e.getEndTimeStr().compareTo(hourMinuteStr) >= 0)
                        .findFirst()
                        .orElse(null);
                if(shiftTime == null){
                    shiftTime = shiftTimeList.getFirst();
                }
                MesShiftTeam shiftTeam = switch (shiftTime.getEnumShift()) {
                    case EnumShiftConfig.DAY_SHIFT -> teamArrange.getDayShiftTeam();
                    case EnumShiftConfig.SHORT_NIGHT_SHIFT -> teamArrange.getShortNightTeam();
                    case EnumShiftConfig.LONG_NIGHT_SHIFT -> teamArrange.getLongNightTeam();
                };

                tanliangjiRecord.setShiftTeam(shiftTeam);
                tanliangjiRecord.setEnumShift(shiftTime.getEnumShift());
            }
            dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(tanliangjiRecordList));
        }
    }
}
