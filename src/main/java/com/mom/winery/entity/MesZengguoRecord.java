package com.mom.winery.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Comment;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.NumberFormat;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.Date;

@JmixEntity
@Table(name = "MES_ZENGGUO_RECORD", indexes = {
        @Index(name = "IDX_MES_ZENGTONG_RECORD_JIAOCHI_UP", columnList = "JIAOCHI_UP_ID"),
        @Index(name = "IDX_MES_ZENGTONG_RECORD_ZENGGUO_PHASE", columnList = "ZENGGUO_PHASE_ID"),
        @Index(name = "IDX_MES_ZENGGUO_RECORD_MES_ZENGGUO", columnList = "MES_ZENGGUO_ID"),
        @Index(name = "IDX_MES_ZENGGUO_RECORD_RUNLIANG_DURATION_QUALIFIED_DOWN", columnList = "RUNLIANG_DURATION_QUALIFIED_DOWN_ID"),
        @Index(name = "IDX_MES_ZENGGUO_RECORD_RUNLIANG_DUARATION_QUALIFIED_UP", columnList = "RUNLIANG_DUARATION_QUALIFIED_UP_ID")
})
@Entity
public class MesZengguoRecord {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @JoinColumn(name = "MES_ZENGGUO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguo mesZengguo;

    @Column(name = "ZENG_SEQUENCE")
    private Integer zengSequence;

    @JoinColumn(name = "ZENGGUO_PHASE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZenggouPhaseConfig zengguoPhase;

    @Comment("阶段开始时间")
    @Column(name = "MES_START_TIME_TOTAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date phaseStartTimeTotal;

    @Comment("阶段结束时间")
    @Column(name = "PHASE_END_TIME_TOTAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date phaseEndTimeTotal;

    @Comment("阶段持续时长(min)")
    @NumberFormat(pattern = "#.##")
    @Column(name = "PHASE_DURATION")
    private Float phaseDuration;

    @Column(name = "START_TIME_TOTAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTimeTotal;

    @Column(name = "START_TIME_DEVICE_SHANG_ZENG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTimeDeviceShangZeng;

    @Column(name = "START_TIME_KAGAI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTimeKagai;

    @Temporal(TemporalType.TIMESTAMP)
    @Comment("馏酒结束时间,通常是酒尾结束，切换至蒸煮/大汽冲酸阶段的时间点")
    @Column(name = "END_TIME_LIUJIU")
    private Date endTimeLiujiu;

    @Column(name = "END_TIME_TALL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTimeTall;

    @JoinColumn(name = "JIAOCHI_DOWN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesJiaochi jiaochiDown;

    @Column(name = "JIAOCHI_LAYER_DOWN")
    private Integer jiaochiLayerDown;

    @Column(name = "JIAOCHI_TIME_DOWN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date jiaochiTimeDown;

    @Column(name = "ZAOPEI_TYPE_DOWN")
    private Integer zaopeiTypeDown;

    @Column(name = "RUNLIANG_START_TIME_DOWN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runliangStartTimeDown;

    @Column(name = "RUNLIANG_END_TIME_DOWN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runliangEndTimeDown;

    @NumberFormat(pattern = "###.#")
    @Column(name = "RUNLIANG_ADD_WATER_DOWN")
    private Float runliangAddWaterDown;

    @NumberFormat(pattern = "##.#")
    @Column(name = "RUNLIANG_DURATION_DOWN")
    private Float runliangDurationDown;

    @JoinColumn(name = "RUNLIANG_DURATION_QUALIFIED_DOWN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig runliangDurationQualifiedDown;

    @NumberFormat(pattern = "#.#")
    @Column(name = "ZAOPEI_QTY_DOWN")
    private Float zaopeiQtyDown;

    @NumberFormat(pattern = "#.#")
    @Column(name = "DAOKE_QTY_DOWN")
    private Float daokeQtyDown;

    @NumberFormat(pattern = "#.#")
    @Column(name = "LIANGSHI_QTY_DOWN")
    private Float liangshiQtyDown;

    @Column(name = "LIANGSHI_TYPE_DOWN")
    private Integer liangshiTypeDown;

    @JoinColumn(name = "JIAOCHI_UP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesJiaochi jiaochiUp;

    @Column(name = "JIAOCHI_TIME_UP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date jiaochiTimeUp;

    @Column(name = "JIAOCHI_LAYER_UP")
    private Integer jiaochiLayerUp;

    @Column(name = "ZAOPEI_TYPE_UP")
    private Integer zaopeiTypeUp;

    @Column(name = "RUNLIANG_START_TIME_UP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runliangStartTimeUp;

    @Column(name = "RUNLIANG_END_TIME_UP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runliangEndTimeUp;

    @NumberFormat(pattern = "#.#")
    @Column(name = "RUNLIANG_ADD_WATER_UP")
    private Float runliangAddWaterUp;

    @NumberFormat(pattern = "#.#")
    @Column(name = "RUNLIANG_DURATION_UP")
    private Float runliangDurationUp;

    @JoinColumn(name = "RUNLIANG_DUARATION_QUALIFIED_UP_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig runliangDuarationQualifiedUp;

    @NumberFormat(pattern = "#.#")
    @Column(name = "ZAOPEI_QTY_UP")
    private Float zaopeiQtyUp;

    @NumberFormat(pattern = "#.#")
    @Column(name = "DAOKE_QTY_UP")
    private Float daokeQtyUp;

    @NumberFormat(pattern = "#.#")
    @Column(name = "LIANGSHI_QTY_UP")
    private Float liangshiQtyUp;

    @Column(name = "LIANGSHI_TYP_UP")
    private Integer liangshiTypUp;

    @Column(name = "ZAOPEI_TYPE")
    private Integer zaopeiType;

    @Column(name = "ZHUANGZENG_LAYER")
    private Integer shangzengLayer;

    @NumberFormat(pattern = "##.#")
    @Column(name = "SHANGZENG_DURATION")
    private Float shangzengDuration;

    @NumberFormat(pattern = "###.##")
    @Column(name = "SHANGZENG_HEIGHT")
    private Float shangzengHeight;

    @Column(name = "SHANGZENG_TOTAL_QTY")
    private Float shangzengTotalQty;

    @NumberFormat(pattern = "##.##")
    @Column(name = "JIEJIU_DURATION_FIRST_CLASS")
    private Float jiejiuDurationFirstClass;

    @NumberFormat(pattern = "#.##")
    @Column(name = "JIEJIU_DURATION_SECOND_CLASS")
    private Float jiejiuDurationSecondClass;

    @NumberFormat(pattern = "#.##")
    @Column(name = "JIEJIU_DURATION_THIRD_CLASS")
    private Float jiejiuDurationThirdClass;

    @NumberFormat(pattern = "#.##")
    @Column(name = "JIEJIU_DURATION_FEISHUI")
    private Float jiejiuDurationFeishui;

    @NumberFormat(pattern = "#.##")
    @Column(name = "JIEJIU_DURATION_JIUWEI")
    private Float jiejiuDurationJiuwei;

    @NumberFormat(pattern = "#.##")
    @Column(name = "LIANGSHUI_ADD_QTY")
    private Float liangshuiAddQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "HUISHOUDIGUO_WATER_ADD_QTY")
    private Float huishoudiguoWaterAddQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "HOT_WATER_ADD_QTY")
    private Float hotWaterAddQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "JIUWEI_ADD_QTY")
    private Float jiuweiAddQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "HUANGSHUI_ADD_QTY")
    private Float huangshuiAddQty;

    @NumberFormat(pattern = "##.##")
    @Column(name = "ENERGY_QI_SHANGZENG")
    private Float energyQiShangzeng;

    @NumberFormat(pattern = "#.##")
    @Column(name = "ENERGY_QI_ZHENGLIU")
    private Float energyQiZhengliu;

    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHENGLIU_DURATION")
    private Float zhengliuDuration;

    @NumberFormat(pattern = "#.##")
    @Column(name = "LIUJIU_ADD_ZHENGZHU_DURATION")
    private Float liujiuAddZhengzhuDuration;

    @Comment("启动的wincc id")
    @Column(name = "PHASE_START_WINCC_ID")
    private Integer phaseStartWinccId;

    @Comment("结束的wincc id")
    @Column(name = "PHASE_END_WINCC_ID")
    private Integer phaseEndWinccId;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private OffsetDateTime lastModifiedDate;

    public Float getPhaseDuration() {
        return phaseDuration;
    }

    public void setPhaseDuration(Float phaseDuration) {
        this.phaseDuration = phaseDuration;
    }

    public Integer getPhaseEndWinccId() {
        return phaseEndWinccId;
    }

    public void setPhaseEndWinccId(Integer phaseEndWinccId) {
        this.phaseEndWinccId = phaseEndWinccId;
    }

    public Integer getPhaseStartWinccId() {
        return phaseStartWinccId;
    }

    public void setPhaseStartWinccId(Integer phaseStartWinccId) {
        this.phaseStartWinccId = phaseStartWinccId;
    }

    public Date getPhaseEndTimeTotal() {
        return phaseEndTimeTotal;
    }

    public void setPhaseEndTimeTotal(Date phaseEndTimeTotal) {
        this.phaseEndTimeTotal = phaseEndTimeTotal;
    }

    public Date getPhaseStartTimeTotal() {
        return phaseStartTimeTotal;
    }

    public void setPhaseStartTimeTotal(Date mesStartTimeTotal) {
        this.phaseStartTimeTotal = mesStartTimeTotal;
    }

    public Float getZhengliuDuration() {
        return zhengliuDuration;
    }

    public void setZhengliuDuration(Float zhengliuDuration) {
        this.zhengliuDuration = zhengliuDuration;
    }

    public Date getRunliangEndTimeUp() {
        return runliangEndTimeUp;
    }

    public void setRunliangEndTimeUp(Date runliangEndTimeUp) {
        this.runliangEndTimeUp = runliangEndTimeUp;
    }

    public Date getRunliangStartTimeUp() {
        return runliangStartTimeUp;
    }

    public void setRunliangStartTimeUp(Date runliangStartTimeUp) {
        this.runliangStartTimeUp = runliangStartTimeUp;
    }

    public MesWinccItemConfig getRunliangDuarationQualifiedUp() {
        return runliangDuarationQualifiedUp;
    }

    public void setRunliangDuarationQualifiedUp(MesWinccItemConfig runliangDuarationQualifiedUp) {
        this.runliangDuarationQualifiedUp = runliangDuarationQualifiedUp;
    }

    public MesWinccItemConfig getRunliangDurationQualifiedDown() {
        return runliangDurationQualifiedDown;
    }

    public void setRunliangDurationQualifiedDown(MesWinccItemConfig runliangDurationQualifiedDown) {
        this.runliangDurationQualifiedDown = runliangDurationQualifiedDown;
    }

    public Date getRunliangEndTimeDown() {
        return runliangEndTimeDown;
    }

    public void setRunliangEndTimeDown(Date runliangEndTimeDown) {
        this.runliangEndTimeDown = runliangEndTimeDown;
    }

    public Date getRunliangStartTimeDown() {
        return runliangStartTimeDown;
    }

    public void setRunliangStartTimeDown(Date runliangStartTimeDown) {
        this.runliangStartTimeDown = runliangStartTimeDown;
    }

    public Float getShangzengTotalQty() {
        return shangzengTotalQty;
    }

    public void setShangzengTotalQty(Float shangzengTotalQty) {
        this.shangzengTotalQty = shangzengTotalQty;
    }

    public Date getEndTimeTall() {
        return endTimeTall;
    }

    public void setEndTimeTall(Date endTimeTall) {
        this.endTimeTall = endTimeTall;
    }

    public MesZengguo getMesZengguo() {
        return mesZengguo;
    }

    public void setMesZengguo(MesZengguo mesZengguo) {
        this.mesZengguo = mesZengguo;
    }

    public MesZenggouPhaseConfig getZengguoPhase() {
        return zengguoPhase;
    }

    public void setZengguoPhase(MesZenggouPhaseConfig zengguoPhase) {
        this.zengguoPhase = zengguoPhase;
    }

    public Float getLiujiuAddZhengzhuDuration() {
        return liujiuAddZhengzhuDuration;
    }

    public void setLiujiuAddZhengzhuDuration(Float liujiuAddZhengzhuDuration) {
        this.liujiuAddZhengzhuDuration = liujiuAddZhengzhuDuration;
    }

    public Float getEnergyQiZhengliu() {
        return energyQiZhengliu;
    }

    public void setEnergyQiZhengliu(Float energyQiZhengliu) {
        this.energyQiZhengliu = energyQiZhengliu;
    }

    public Float getEnergyQiShangzeng() {
        return energyQiShangzeng;
    }

    public void setEnergyQiShangzeng(Float energyQiShangzeng) {
        this.energyQiShangzeng = energyQiShangzeng;
    }

    public Integer getZengSequence() {
        return zengSequence;
    }

    public void setZengSequence(Integer zengSequence) {
        this.zengSequence = zengSequence;
    }

    public Float getHuangshuiAddQty() {
        return huangshuiAddQty;
    }

    public void setHuangshuiAddQty(Float huangshuiAddQty) {
        this.huangshuiAddQty = huangshuiAddQty;
    }

    public Float getJiuweiAddQty() {
        return jiuweiAddQty;
    }

    public void setJiuweiAddQty(Float jiuweiAddQty) {
        this.jiuweiAddQty = jiuweiAddQty;
    }

    public Float getHotWaterAddQty() {
        return hotWaterAddQty;
    }

    public void setHotWaterAddQty(Float hotWaterAddQty) {
        this.hotWaterAddQty = hotWaterAddQty;
    }

    public Float getHuishoudiguoWaterAddQty() {
        return huishoudiguoWaterAddQty;
    }

    public void setHuishoudiguoWaterAddQty(Float huishoudiguoWaterAddQty) {
        this.huishoudiguoWaterAddQty = huishoudiguoWaterAddQty;
    }

    public Float getLiangshuiAddQty() {
        return liangshuiAddQty;
    }

    public void setLiangshuiAddQty(Float liangshuiAddQty) {
        this.liangshuiAddQty = liangshuiAddQty;
    }

    public Float getJiejiuDurationJiuwei() {
        return jiejiuDurationJiuwei;
    }

    public void setJiejiuDurationJiuwei(Float jiejiuDurationJiuwei) {
        this.jiejiuDurationJiuwei = jiejiuDurationJiuwei;
    }

    public Float getJiejiuDurationFeishui() {
        return jiejiuDurationFeishui;
    }

    public void setJiejiuDurationFeishui(Float jiejiuDurationFeishui) {
        this.jiejiuDurationFeishui = jiejiuDurationFeishui;
    }

    public Float getJiejiuDurationThirdClass() {
        return jiejiuDurationThirdClass;
    }

    public void setJiejiuDurationThirdClass(Float jiejiuDurationThirdClass) {
        this.jiejiuDurationThirdClass = jiejiuDurationThirdClass;
    }

    public Float getJiejiuDurationSecondClass() {
        return jiejiuDurationSecondClass;
    }

    public void setJiejiuDurationSecondClass(Float jiejiuDurationSecondClass) {
        this.jiejiuDurationSecondClass = jiejiuDurationSecondClass;
    }

    public Float getJiejiuDurationFirstClass() {
        return jiejiuDurationFirstClass;
    }

    public void setJiejiuDurationFirstClass(Float jiejiuDurationFirstClass) {
        this.jiejiuDurationFirstClass = jiejiuDurationFirstClass;
    }

    public Float getShangzengHeight() {
        return shangzengHeight;
    }

    public void setShangzengHeight(Float shangzengHeight) {
        this.shangzengHeight = shangzengHeight;
    }

    public Float getShangzengDuration() {
        return shangzengDuration;
    }

    public void setShangzengDuration(Float shangzengDuration) {
        this.shangzengDuration = shangzengDuration;
    }

    public Integer getShangzengLayer() {
        return shangzengLayer;
    }

    public void setShangzengLayer(Integer zhuangzengLayer) {
        this.shangzengLayer = zhuangzengLayer;
    }

    public EnumZaopeiType getZaopeiType() {
        return zaopeiType == null ? null : EnumZaopeiType.fromId(zaopeiType);
    }

    public void setZaopeiType(EnumZaopeiType zaopeiType) {
        this.zaopeiType = zaopeiType == null ? null : zaopeiType.getId();
    }

    public EnumLiangshiType getLiangshiTypUp() {
        return liangshiTypUp == null ? null : EnumLiangshiType.fromId(liangshiTypUp);
    }

    public void setLiangshiTypUp(EnumLiangshiType liangshiTypUp) {
        this.liangshiTypUp = liangshiTypUp == null ? null : liangshiTypUp.getId();
    }

    public Float getLiangshiQtyUp() {
        return liangshiQtyUp;
    }

    public void setLiangshiQtyUp(Float liangshiQtyUp) {
        this.liangshiQtyUp = liangshiQtyUp;
    }

    public Float getDaokeQtyUp() {
        return daokeQtyUp;
    }

    public void setDaokeQtyUp(Float daokeQtyUp) {
        this.daokeQtyUp = daokeQtyUp;
    }

    public Float getZaopeiQtyUp() {
        return zaopeiQtyUp;
    }

    public void setZaopeiQtyUp(Float zaopeiQtyUp) {
        this.zaopeiQtyUp = zaopeiQtyUp;
    }

    public Float getRunliangDurationUp() {
        return runliangDurationUp;
    }

    public void setRunliangDurationUp(Float runliangDurationUp) {
        this.runliangDurationUp = runliangDurationUp;
    }

    public Float getRunliangAddWaterUp() {
        return runliangAddWaterUp;
    }

    public void setRunliangAddWaterUp(Float runliangWaterAddUp) {
        this.runliangAddWaterUp = runliangWaterAddUp;
    }

    public EnumZaopeiType getZaopeiTypeUp() {
        return zaopeiTypeUp == null ? null : EnumZaopeiType.fromId(zaopeiTypeUp);
    }

    public void setZaopeiTypeUp(EnumZaopeiType zaopeiTypeUp) {
        this.zaopeiTypeUp = zaopeiTypeUp == null ? null : zaopeiTypeUp.getId();
    }

    public Integer getJiaochiLayerUp() {
        return jiaochiLayerUp;
    }

    public void setJiaochiLayerUp(Integer jiaochiLayerUp) {
        this.jiaochiLayerUp = jiaochiLayerUp;
    }

    public Date getJiaochiTimeUp() {
        return jiaochiTimeUp;
    }

    public void setJiaochiTimeUp(Date jiaochiTimeUp) {
        this.jiaochiTimeUp = jiaochiTimeUp;
    }

    public MesJiaochi getJiaochiUp() {
        return jiaochiUp;
    }

    public void setJiaochiUp(MesJiaochi jiaochiUp) {
        this.jiaochiUp = jiaochiUp;
    }

    public EnumLiangshiType getLiangshiTypeDown() {
        return liangshiTypeDown == null ? null : EnumLiangshiType.fromId(liangshiTypeDown);
    }

    public void setLiangshiTypeDown(EnumLiangshiType liangshiTypeDown) {
        this.liangshiTypeDown = liangshiTypeDown == null ? null : liangshiTypeDown.getId();
    }

    public Float getLiangshiQtyDown() {
        return liangshiQtyDown;
    }

    public void setLiangshiQtyDown(Float liangshiQtyDown) {
        this.liangshiQtyDown = liangshiQtyDown;
    }

    public Float getDaokeQtyDown() {
        return daokeQtyDown;
    }

    public void setDaokeQtyDown(Float daokeQtyDown) {
        this.daokeQtyDown = daokeQtyDown;
    }

    public Float getZaopeiQtyDown() {
        return zaopeiQtyDown;
    }

    public void setZaopeiQtyDown(Float zaopeiQtyDown) {
        this.zaopeiQtyDown = zaopeiQtyDown;
    }

    public Float getRunliangDurationDown() {
        return runliangDurationDown;
    }

    public void setRunliangDurationDown(Float runliangDurationDown) {
        this.runliangDurationDown = runliangDurationDown;
    }

    public Date getJiaochiTimeDown() {
        return jiaochiTimeDown;
    }

    public void setJiaochiTimeDown(Date jiaochiTimeDown) {
        this.jiaochiTimeDown = jiaochiTimeDown;
    }

    public Float getRunliangAddWaterDown() {
        return runliangAddWaterDown;
    }

    public void setRunliangAddWaterDown(Float runliangAddWater) {
        this.runliangAddWaterDown = runliangAddWater;
    }

    public EnumZaopeiType getZaopeiTypeDown() {
        return zaopeiTypeDown == null ? null : EnumZaopeiType.fromId(zaopeiTypeDown);
    }

    public void setZaopeiTypeDown(EnumZaopeiType zaopeiType) {
        this.zaopeiTypeDown = zaopeiType == null ? null : zaopeiType.getId();
    }

    public Integer getJiaochiLayerDown() {
        return jiaochiLayerDown;
    }

    public void setJiaochiLayerDown(Integer jiaochiLayer) {
        this.jiaochiLayerDown = jiaochiLayer;
    }

    public void setJiaochiDown(MesJiaochi jichaoDown) {
        this.jiaochiDown = jichaoDown;
    }

    public MesJiaochi getJiaochiDown() {
        return jiaochiDown;
    }

    public void setEndTimeLiujiu(Date endTimeLiujiu) {
        this.endTimeLiujiu = endTimeLiujiu;
    }

    public Date getEndTimeLiujiu() {
        return endTimeLiujiu;
    }

    public Date getStartTimeKagai() {
        return startTimeKagai;
    }

    public void setStartTimeKagai(Date startTimeKagai) {
        this.startTimeKagai = startTimeKagai;
    }

    public Date getStartTimeDeviceShangZeng() {
        return startTimeDeviceShangZeng;
    }

    public void setStartTimeDeviceShangZeng(Date startTimeDeviceShangZeng) {
        this.startTimeDeviceShangZeng = startTimeDeviceShangZeng;
    }

    public Date getStartTimeTotal() {
        return startTimeTotal;
    }

    public void setStartTimeTotal(Date startTimeTotal) {
        this.startTimeTotal = startTimeTotal;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
