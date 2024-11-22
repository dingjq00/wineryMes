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
@Table(name = "MES_RUNLIANGDOU_OPERATION", indexes = {
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_LOCATION", columnList = "LOCATION_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_EMPTY_OR_FULL", columnList = "EMPTY_OR_FULL_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_DIU_ZAO_ORLIANGZAO", columnList = "DIU_ZAO_ORLIANGZAO_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_JIAOCHI", columnList = "JIAOCHI_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_ZAOPEI_TYPE", columnList = "ZAOPEI_TYPE"),
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_DURATION_QUALIFIED", columnList = "DURATION_QUALIFIED_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOUDOU_OPERATION_MES_RUNLIANGDOU", columnList = "MES_RUNLIANGDOU_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOUDOU_OPERATION_PRE_LOCATION", columnList = "PRE_LOCATION_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOUDOU_OPERATION_AFTER_LOCATION", columnList = "AFTER_LOCATION_ID"),
        @Index(name = "IDX_MES_RUNLIANGDOU_OPERATION_SHIFT_TEAM", columnList = "SHIFT_TEAM_ID")
})
@Entity
public class MesRunliangdouOperation {
    @Comment("配润操作执行记录,汇总润粮斗的详细数据")
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("润粮斗")
    @JoinColumn(name = "MES_RUNLIANGDOU_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesRunliangdou mesRunliangdou;

    @Column(name = "WINCC_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUpdateTime;

    @Column(name = "WINCC_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccEndTime;

    @NumberFormat(pattern = "#.##")
    @Column(name = "PHASE_DURATION")
    private Float phaseDuration;

    @JoinColumn(name = "PRE_LOCATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig preLocation;

    @JoinColumn(name = "LOCATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig location;

    @JoinColumn(name = "AFTER_LOCATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig afterLocation;

    @JoinColumn(name = "EMPTY_OR_FULL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig emptyOrFull;

    @JoinColumn(name = "DIU_ZAO_ORLIANGZAO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig diuZaoOrliangzao;

    @NumberFormat(pattern = "#.#")
    @Column(name = "WATER_QTY_ADD")
    private Float waterQtyAdd;

    @NumberFormat(pattern = "#.#")
    @Column(name = "RUNLIANG_DURATION")
    private Float runliangDuration;

    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @JoinColumn(name = "JIAOCHI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesJiaochi jiaochi;

    @Column(name = "JIAOCHI_LAYER")
    private Integer jiaochiLayer;

    @Column(name = "JIAOCHI_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date jiaochiTime;

    @Column(name = "ZAOPEI_TYPE")
    private Integer zaopeiType;

    @NumberFormat(pattern = "#.#")
    @Column(name = "DAOKE_ADD_QTY")
    private Float daokeAddQty;

    @NumberFormat(pattern = "#.#")
    @Column(name = "LIANGSHI_ADD_QTY")
    private Float liangshiAddQty;

    @NumberFormat(pattern = "#.#")
    @Column(name = "ZAOPEI_ADD_QTY")
    private Float zaopeiAddQty;

    @JoinColumn(name = "DURATION_QUALIFIED_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig durationQualified;

    @Column(name = "LIANGSHI_TYPE")
    private Integer liangshiType;

    @NumberFormat(pattern = "##############")
    @Column(name = "WINCC_START_ID")
    private Integer winccStartID;

    @NumberFormat(pattern = "#############")
    @Column(name = "WINCC_END_ID")
    private Integer winccEndId;

    @Comment("班组")
    @JoinColumn(name = "SHIFT_TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesShiftTeam shiftTeam;

    @Comment("班次")
    @Column(name = "ENUM_SHIFT", length = 50)
    private String enumShift;

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

    public EnumShiftConfig getEnumShift() {
        return enumShift == null ? null : EnumShiftConfig.fromId(enumShift);
    }

    public void setEnumShift(EnumShiftConfig enumShift) {
        this.enumShift = enumShift == null ? null : enumShift.getId();
    }

    public MesShiftTeam getShiftTeam() {
        return shiftTeam;
    }

    public void setShiftTeam(MesShiftTeam shiftTeam) {
        this.shiftTeam = shiftTeam;
    }

    public Integer getWinccEndId() {
        return winccEndId;
    }

    public void setWinccEndId(Integer winccEndId) {
        this.winccEndId = winccEndId;
    }

    public Integer getWinccStartID() {
        return winccStartID;
    }

    public void setWinccStartID(Integer winccStartID) {
        this.winccStartID = winccStartID;
    }

    public MesWinccItemConfig getAfterLocation() {
        return afterLocation;
    }

    public void setAfterLocation(MesWinccItemConfig afterLocation) {
        this.afterLocation = afterLocation;
    }

    public MesWinccItemConfig getPreLocation() {
        return preLocation;
    }

    public void setPreLocation(MesWinccItemConfig preLocation) {
        this.preLocation = preLocation;
    }

    public Float getPhaseDuration() {
        return phaseDuration;
    }

    public void setPhaseDuration(Float phaseDuration) {
        this.phaseDuration = phaseDuration;
    }

    public Date getWinccEndTime() {
        return winccEndTime;
    }

    public void setWinccEndTime(Date winccEndTime) {
        this.winccEndTime = winccEndTime;
    }

    public MesRunliangdou getMesRunliangdou() {
        return mesRunliangdou;
    }

    public void setMesRunliangdou(MesRunliangdou mesRunliangdou) {
        this.mesRunliangdou = mesRunliangdou;
    }

    public Date getWinccUpdateTime() {
        return winccUpdateTime;
    }

    public void setWinccUpdateTime(Date winccUpdateTime) {
        this.winccUpdateTime = winccUpdateTime;
    }

    public EnumLiangshiType getLiangshiType() {
        return liangshiType == null ? null : EnumLiangshiType.fromId(liangshiType);
    }

    public void setLiangshiType(EnumLiangshiType liangshiType) {
        this.liangshiType = liangshiType == null ? null : liangshiType.getId();
    }

    public MesWinccItemConfig getDurationQualified() {
        return durationQualified;
    }

    public void setDurationQualified(MesWinccItemConfig durationQualified) {
        this.durationQualified = durationQualified;
    }

    public Float getZaopeiAddQty() {
        return zaopeiAddQty;
    }

    public void setZaopeiAddQty(Float zaopeiAddQty) {
        this.zaopeiAddQty = zaopeiAddQty;
    }

    public Float getLiangshiAddQty() {
        return liangshiAddQty;
    }

    public void setLiangshiAddQty(Float liangshiAddQty) {
        this.liangshiAddQty = liangshiAddQty;
    }

    public Float getDaokeAddQty() {
        return daokeAddQty;
    }

    public void setDaokeAddQty(Float daokeAddQty) {
        this.daokeAddQty = daokeAddQty;
    }

    public void setZaopeiType(EnumZaopeiType zaopeiType) {
        this.zaopeiType = zaopeiType == null ? null : zaopeiType.getId();
    }

    public EnumZaopeiType getZaopeiType() {
        return zaopeiType == null ? null : EnumZaopeiType.fromId(zaopeiType);
    }

    public Date getJiaochiTime() {
        return jiaochiTime;
    }

    public void setJiaochiTime(Date jiaochiTime) {
        this.jiaochiTime = jiaochiTime;
    }

    public Integer getJiaochiLayer() {
        return jiaochiLayer;
    }

    public void setJiaochiLayer(Integer jiaochiLayer) {
        this.jiaochiLayer = jiaochiLayer;
    }

    public MesJiaochi getJiaochi() {
        return jiaochi;
    }

    public void setJiaochi(MesJiaochi jiaochi) {
        this.jiaochi = jiaochi;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Float getRunliangDuration() {
        return runliangDuration;
    }

    public void setRunliangDuration(Float runliangDuration) {
        this.runliangDuration = runliangDuration;
    }

    public Float getWaterQtyAdd() {
        return waterQtyAdd;
    }

    public void setWaterQtyAdd(Float waterQtyAdd) {
        this.waterQtyAdd = waterQtyAdd;
    }

    public MesWinccItemConfig getDiuZaoOrliangzao() {
        return diuZaoOrliangzao;
    }

    public void setDiuZaoOrliangzao(MesWinccItemConfig diuZaoOrliangzao) {
        this.diuZaoOrliangzao = diuZaoOrliangzao;
    }

    public MesWinccItemConfig getEmptyOrFull() {
        return emptyOrFull;
    }

    public void setEmptyOrFull(MesWinccItemConfig emptyOrFull) {
        this.emptyOrFull = emptyOrFull;
    }

    public MesWinccItemConfig getLocation() {
        return location;
    }

    public void setLocation(MesWinccItemConfig location) {
        this.location = location;
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
