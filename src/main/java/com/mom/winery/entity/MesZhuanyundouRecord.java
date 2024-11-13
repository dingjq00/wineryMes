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

@Comment("转运斗执行记录")
@JmixEntity
@Table(name = "MES_ZHUANYUNDOU_RECORD", indexes = {
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_MES_ZHUANYUNDOU", columnList = "MES_ZHUANYUNDOU_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_CURRENT_LOCATION", columnList = "CURRENT_LOCATION_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_FULL_OR_EMPTY", columnList = "FULL_OR_EMPTY_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_CHUJIAOZAO_OR_HUIJIAO_ZAO", columnList = "CHUJIAOZAO_OR_HUIJIAO_ZAO_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_HUIJIAO_JIAOCHI", columnList = "HUIJIAO_JIAOCHI_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_CHUJIAO_JIAOCHI", columnList = "CHUJIAO_JIAOCHI_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_PRE_LOCATION", columnList = "PRE_LOCATION_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_RECORD_AFTER_LOCATION", columnList = "AFTER_LOCATION_ID")
})
@Entity
public class MesZhuanyundouRecord {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("Wincc开始id")
    @Column(name = "PHASE_START_ID")
    private Integer phaseStartId;

    @Comment("阶段开始时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PHASE_START_TIME")
    private Date phaseStartTime;

    @Comment("阶段结束id")
    @Column(name = "PHASE_END_ID")
    private Integer phaseEndId;

    @Comment("阶段结束时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PHASE_END_TIME")
    private Date phaseEndTime;

    @Comment("阶段持续时长")
    @NumberFormat(pattern = "#.##")
    @Column(name = "PHASE_DURATION")
    private Float phaseDuration;

    @Comment("转运斗")
    @JoinColumn(name = "MES_ZHUANYUNDOU_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZhuanyundou mesZhuanyundou;

    @Comment("wincc的料斗编号")
    @Column(name = "WINCC_NO")
    private Integer wincc_No;

    @Comment("当前位置")
    @JoinColumn(name = "CURRENT_LOCATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig currentLocation;

    @Comment("空斗/满斗")
    @JoinColumn(name = "FULL_OR_EMPTY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig fullOrEmpty;

    @Comment("出窖糟/回窖糟")
    @JoinColumn(name = "CHUJIAOZAO_OR_HUIJIAO_ZAO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig chujiaozaoOrHuijiaoZao;

    @Comment("回窖糟：摊晾时长")
    @NumberFormat(pattern = "#.##")
    @Column(name = "TANLIANG_DURATION")
    private Float huijiaoTanliangDuration;

    @Comment("回窖糟：该糟醅源头—出窖层数")
    @Column(name = "JIAOCHI_LAYER")
    private Integer huijiaoJiaochiLayer;

    @Comment("回窖糟：该糟醅源头—出窖窖池编号")
    @JoinColumn(name = "HUIJIAO_JIAOCHI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesJiaochi huijiaoJiaochi;

    @Comment("回窖糟：该糟醅源头—出窖时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HUIJIAO_CHUJIAO_TIME")
    private Date huijiaoChujiaoTime;

    @Comment("回窖糟：糟醅类型")
    @Column(name = "HUIJIAO_ZAOPEI_TYPE")
    private Integer huijiaoZaopeiType;

    @Comment("回窖糟：加曲重量")
    @NumberFormat(pattern = "#.##")
    @Column(name = "HUIJIAO_QUFEI_QTY")
    private Float huijiaoQufeiQty;

    @Comment("回窖糟：糟醅重量")
    @Column(name = "HUIJIAO_ZAOPEI_QTY")
    private Float huijiaoZaopeiQty;

    @Comment("回窖糟：摊晾期间出口最大温度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "HUIJIAO_CHUKOU_MAX_TEMP")
    private Float huijiaoChukouMaxTemp;

    @Comment("回窖糟：摊晾期间出口最小温度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "HUIJIAO_CHUKOU_MIN_TEMP")
    private Float huijiaoChukouMinTemp;

    @Comment("回窖糟：摊晾开始时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HUIJIAO_TANLIANG_START_TIME")
    private Date huijiaoTanliangStartTime;

    @Comment("回窖糟：摊晾期间出口平均温度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "HUIJIAO_CHUKOU_AVG_TEMP")
    private Float huijiaoChukouAvgTemp;

    @Comment("出窖：出层数")
    @Column(name = "CHUJIAO_JIAOCHI_LAYER")
    private Integer chujiaoJiaochiLayer;

    @Comment("出窖：窖池")
    @JoinColumn(name = "CHUJIAO_JIAOCHI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesJiaochi chujiaoJiaochi;

    @Comment("出窖：出窖时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHUJIAO_TIME")
    private Date chujiaoTime;

    @Comment("出窖：糟醅类型")
    @Column(name = "CHUJIAO_ZAOPEI_TYPE")
    private Integer chujiaoZaopeiType;

    @Comment("前位置")
    @JoinColumn(name = "PRE_LOCATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig preLocation;

    @Comment("后位置")
    @JoinColumn(name = "AFTER_LOCATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesWinccItemConfig afterLocation;


    public MesZhuanyundou getMesZhuanyundou() {
        return mesZhuanyundou;
    }

    public void setMesZhuanyundou(MesZhuanyundou mesZhuanyundou) {
        this.mesZhuanyundou = mesZhuanyundou;
    }

    public Date getPhaseEndTime() {
        return phaseEndTime;
    }

    public void setPhaseEndTime(Date phaseEndTime) {
        this.phaseEndTime = phaseEndTime;
    }

    public Integer getPhaseEndId() {
        return phaseEndId;
    }

    public void setPhaseEndId(Integer phaseEndId) {
        this.phaseEndId = phaseEndId;
    }

    public Date getPhaseStartTime() {
        return phaseStartTime;
    }

    public void setPhaseStartTime(Date phaseStartTime) {
        this.phaseStartTime = phaseStartTime;
    }

    public Integer getPhaseStartId() {
        return phaseStartId;
    }

    public void setPhaseStartId(Integer winccStartId) {
        this.phaseStartId = winccStartId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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


    public EnumZaopeiType getChujiaoZaopeiType() {
        return chujiaoZaopeiType == null ? null : EnumZaopeiType.fromId(chujiaoZaopeiType);
    }

    public void setChujiaoZaopeiType(EnumZaopeiType chujiaoZaopeiType) {
        this.chujiaoZaopeiType = chujiaoZaopeiType == null ? null : chujiaoZaopeiType.getId();
    }

    public Date getChujiaoTime() {
        return chujiaoTime;
    }

    public void setChujiaoTime(Date chujiaoTime) {
        this.chujiaoTime = chujiaoTime;
    }

    public MesJiaochi getChujiaoJiaochi() {
        return chujiaoJiaochi;
    }

    public void setChujiaoJiaochi(MesJiaochi chujiaoJiaochi) {
        this.chujiaoJiaochi = chujiaoJiaochi;
    }

    public Integer getChujiaoJiaochiLayer() {
        return chujiaoJiaochiLayer;
    }

    public void setChujiaoJiaochiLayer(Integer chujiaoJiaochiLayer) {
        this.chujiaoJiaochiLayer = chujiaoJiaochiLayer;
    }

    public Float getHuijiaoChukouAvgTemp() {
        return huijiaoChukouAvgTemp;
    }

    public void setHuijiaoChukouAvgTemp(Float huijiaoChukouAvgTemp) {
        this.huijiaoChukouAvgTemp = huijiaoChukouAvgTemp;
    }

    public Date getHuijiaoTanliangStartTime() {
        return huijiaoTanliangStartTime;
    }

    public void setHuijiaoTanliangStartTime(Date huijiaoTanliangStartTime) {
        this.huijiaoTanliangStartTime = huijiaoTanliangStartTime;
    }

    public Float getHuijiaoChukouMinTemp() {
        return huijiaoChukouMinTemp;
    }

    public void setHuijiaoChukouMinTemp(Float huijiaoChukouMinTemp) {
        this.huijiaoChukouMinTemp = huijiaoChukouMinTemp;
    }

    public Float getHuijiaoChukouMaxTemp() {
        return huijiaoChukouMaxTemp;
    }

    public void setHuijiaoChukouMaxTemp(Float huijiaoChukouMaxTemp) {
        this.huijiaoChukouMaxTemp = huijiaoChukouMaxTemp;
    }

    public Float getHuijiaoZaopeiQty() {
        return huijiaoZaopeiQty;
    }

    public void setHuijiaoZaopeiQty(Float huijiaoZaopeiQty) {
        this.huijiaoZaopeiQty = huijiaoZaopeiQty;
    }

    public Float getHuijiaoQufeiQty() {
        return huijiaoQufeiQty;
    }

    public void setHuijiaoQufeiQty(Float huijiaoQufeiQty) {
        this.huijiaoQufeiQty = huijiaoQufeiQty;
    }

    public EnumZaopeiType getHuijiaoZaopeiType() {
        return huijiaoZaopeiType == null ? null : EnumZaopeiType.fromId(huijiaoZaopeiType);
    }

    public void setHuijiaoZaopeiType(EnumZaopeiType huijiaoZaopeiType) {
        this.huijiaoZaopeiType = huijiaoZaopeiType == null ? null : huijiaoZaopeiType.getId();
    }

    public Date getHuijiaoChujiaoTime() {
        return huijiaoChujiaoTime;
    }

    public void setHuijiaoChujiaoTime(Date huijiaoChujiaoTime) {
        this.huijiaoChujiaoTime = huijiaoChujiaoTime;
    }

    public MesJiaochi getHuijiaoJiaochi() {
        return huijiaoJiaochi;
    }

    public void setHuijiaoJiaochi(MesJiaochi huijiaoJiaochi) {
        this.huijiaoJiaochi = huijiaoJiaochi;
    }

    public Integer getHuijiaoJiaochiLayer() {
        return huijiaoJiaochiLayer;
    }

    public void setHuijiaoJiaochiLayer(Integer jiaochiLayer) {
        this.huijiaoJiaochiLayer = jiaochiLayer;
    }

    public Float getHuijiaoTanliangDuration() {
        return huijiaoTanliangDuration;
    }

    public void setHuijiaoTanliangDuration(Float tanliangDuration) {
        this.huijiaoTanliangDuration = tanliangDuration;
    }

    public MesWinccItemConfig getChujiaozaoOrHuijiaoZao() {
        return chujiaozaoOrHuijiaoZao;
    }

    public void setChujiaozaoOrHuijiaoZao(MesWinccItemConfig chujiaozaoOrHuijiaoZao) {
        this.chujiaozaoOrHuijiaoZao = chujiaozaoOrHuijiaoZao;
    }

    public MesWinccItemConfig getFullOrEmpty() {
        return fullOrEmpty;
    }

    public void setFullOrEmpty(MesWinccItemConfig fullOrEmpty) {
        this.fullOrEmpty = fullOrEmpty;
    }

    public MesWinccItemConfig getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(MesWinccItemConfig currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Integer getWincc_No() {
        return wincc_No;
    }

    public void setWincc_No(Integer wincc_No) {
        this.wincc_No = wincc_No;
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

}
