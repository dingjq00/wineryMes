package com.mom.winery.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Comment;
import io.jmix.core.metamodel.annotation.InstanceName;
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
@Table(name = "MES_ZHUANYUNDOU", indexes = {
        @Index(name = "IDX_MES_ZHUANYUNDOU_MES_AREA", columnList = "MES_AREA_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_CURRENT_LOCATION", columnList = "CURRENT_LOCATION_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_FULL_OR_EMPTY", columnList = "FULL_OR_EMPTY_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_CHUJIAOZAO_OR_HUIJIAO_ZAO", columnList = "CHUJIAOZAO_OR_HUIJIAO_ZAO_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_HUIJIAO_JIAOCHI", columnList = "HUIJIAO_JIAOCHI_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_CHUJIAO_JIAOCHI", columnList = "CHUJIAO_JIAOCHI_ID"),
        @Index(name = "IDX_MES_ZHUANYUNDOU_MES_SHOPFLOOR", columnList = "MES_SHOPFLOOR_ID")
})
@Entity
public class MesZhuanyundou {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("生产车间")
    @JoinColumn(name = "MES_SHOPFLOOR_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesShopfloor mesShopfloor;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "MES_AREA_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MesArea mesArea;

    @Comment("转运斗编号")
    @Column(name = "ZHUANYUNDOU_NO")
    private Integer zhuanyundouNo;

    @Comment("转运斗名称")
    @InstanceName
    @Column(name = "ZHUANYUNDOU_CODE", length = 120)
    private String zhuanyundouName;

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

    @Comment("Wincc更新id")
    @Column(name = "WINCC_UPDATE_ID")
    private Integer winccUpdateId;

    @Comment("Wincc更新时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "WINCC_UPDATE_TIME")
    private Date winccUpdateTime;

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

    public MesShopfloor getMesShopfloor() {
        return mesShopfloor;
    }

    public void setMesShopfloor(MesShopfloor mesShopfloor) {
        this.mesShopfloor = mesShopfloor;
    }

    public Date getWinccUpdateTime() {
        return winccUpdateTime;
    }

    public void setWinccUpdateTime(Date winccUpdateTime) {
        this.winccUpdateTime = winccUpdateTime;
    }

    public Integer getWinccUpdateId() {
        return winccUpdateId;
    }

    public void setWinccUpdateId(Integer winccUpdateId) {
        this.winccUpdateId = winccUpdateId;
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

    public Integer getZhuanyundouNo() {
        return zhuanyundouNo;
    }

    public void setZhuanyundouNo(Integer zhuanyundouNo) {
        this.zhuanyundouNo = zhuanyundouNo;
    }

    public MesArea getMesArea() {
        return mesArea;
    }

    public void setMesArea(MesArea mesArea) {
        this.mesArea = mesArea;
    }

    public String getZhuanyundouName() {
        return zhuanyundouName;
    }

    public void setZhuanyundouName(String zhuanyundouName) {
        this.zhuanyundouName = zhuanyundouName;
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
