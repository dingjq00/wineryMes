package com.mom.winery.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.NumberFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.Date;

@JmixEntity
@Table(name = "MES_TANLIANGJI", indexes = {
        @Index(name = "IDX_MES_TANLIANGJI_MES_AREA", columnList = "MES_AREA_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_RESOURCE_ZENGGUO", columnList = "RESOURCE_ZENGGUO_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_CURRENT_ZHUANYUNDOU", columnList = "CURRENT_ZHUANYUNDOU_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_WINCC_INPUT_JIAOCHI", columnList = "WINCC_INPUT_JIAOCHI_ID")
})
@Entity
public class MesTanliangji {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "MES_AREA_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MesArea mesArea;

    @InstanceName
    @NotNull
    @Column(name = "TANLIANGJI_CODE", nullable = false, length = 120)
    private String tanliangjiCode;

    @JoinColumn(name = "RESOURCE_ZENGGUO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguo resourceZengguo;

    @Column(name = "ZENG_SEQUENCE")
    private Integer zengSequence;

    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUpdateTime;

    @NumberFormat(pattern = "#.##")
    @Column(name = "WINCC_ZAOPEI_QTY")
    private Float winccZaopeiQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "WINCC_JIAQU_QTY")
    private Float winccJiaquQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "WINCC_OUT_MAX_TEMP")
    private Float winccOutMaxTemp;

    @Column(name = "WINCC_OUT_MIN_TEMP")
    private String winccOutMinTemp;

    @Column(name = "WINCC_OUT_AVG_TEMP")
    private String winccOutAvgTemp;

    @Column(name = "WINCC_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccStartTime;

    @Column(name = "WINCC_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccEndTime;

    @NumberFormat(pattern = "#.##")
    @Column(name = "WINCC_DURATION")
    private Float winccDuration;

    @NumberFormat(pattern = "#.##")
    @Column(name = "LIANGSHUI_ADD_QTY")
    private Float liangshuiAddQty;

    @NumberFormat(pattern = "#.##")
    @Column(name = "DAOKE_ADD_QTY_DOWN")
    private Float daokeAddQtyDown;

    @NumberFormat(pattern = "#.##")
    @Column(name = "LIANGSHI_ADD_QTY_DOWN")
    private Float liangshiAddQtyDown;

    @NumberFormat(pattern = "#.##")
    @Column(name = "ZAOPEI_ADD_QTY_DOWN")
    private Float zaopeiAddQtyDown;

    @NumberFormat(pattern = "#.##")
    @Column(name = "DAOKE_ADD_QTY_UP")
    private Float daokeAddQtyUp;

    @NumberFormat(pattern = "#.##")
    @Column(name = "LIANGSHI_ADD_QTY_UP")
    private Float liangshiAddQtyUp;

    @NumberFormat(pattern = "#.##")
    @Column(name = "ZAOPEI_ADD_QTY_UP")
    private Float zaopeiAddQtyUp;

    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHENGLIU_DURATION")
    private Float zhengliuDuration;

    @NumberFormat(pattern = "#.##")
    @Column(name = "SHANGZENG_DURATION")
    private Float shangzengDuration;

    @Column(name = "ZAOPEI_TYPE")
    private Integer zaopeiType;

    @NumberFormat(pattern = "#.##")
    @Column(name = "SHANGZENG_TOTAL_QTY")
    private Float shangzengTotalQty;

    @JoinColumn(name = "CURRENT_ZHUANYUNDOU_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZhuanyundou currentZhuanyundou;

    @Column(name = "WINCC_JIAOCHI_LAYER")
    private Integer winccJiaochiLayer;

    @Column(name = "WINCC_CHUJIAO_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccChujiaoTime;

    @Column(name = "WINCC_ZAOPEI_TYPE")
    private Integer winccZaopeiType;

    @JoinColumn(name = "WINCC_INPUT_JIAOCHI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesJiaochi winccInputJiaochi;

    @NumberFormat(pattern = "############")
    @Column(name = "WINCC_START_ID")
    private Integer winccStartId;

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

    public Date getWinccUpdateTime() {
        return winccUpdateTime;
    }

    public void setWinccUpdateTime(Date startTime) {
        this.winccUpdateTime = startTime;
    }

    public Float getLiangshuiAddQty() {
        return liangshuiAddQty;
    }

    public void setLiangshuiAddQty(Float liangshuiAddQty) {
        this.liangshuiAddQty = liangshuiAddQty;
    }

    public Integer getWinccStartId() {
        return winccStartId;
    }

    public void setWinccStartId(Integer winccStartId) {
        this.winccStartId = winccStartId;
    }

    public String getWinccOutAvgTemp() {
        return winccOutAvgTemp;
    }

    public void setWinccOutAvgTemp(String winccOutAvgTemp) {
        this.winccOutAvgTemp = winccOutAvgTemp;
    }

    public String getWinccOutMinTemp() {
        return winccOutMinTemp;
    }

    public void setWinccOutMinTemp(String winccOutMinTemp) {
        this.winccOutMinTemp = winccOutMinTemp;
    }

    public Float getWinccOutMaxTemp() {
        return winccOutMaxTemp;
    }

    public void setWinccOutMaxTemp(Float winccOutMaxTemp) {
        this.winccOutMaxTemp = winccOutMaxTemp;
    }

    public Float getWinccJiaquQty() {
        return winccJiaquQty;
    }

    public void setWinccJiaquQty(Float winccJiaquQty) {
        this.winccJiaquQty = winccJiaquQty;
    }

    public Float getWinccZaopeiQty() {
        return winccZaopeiQty;
    }

    public void setWinccZaopeiQty(Float winccZaopeiQty) {
        this.winccZaopeiQty = winccZaopeiQty;
    }

    public EnumZaopeiType getWinccZaopeiType() {
        return winccZaopeiType == null ? null : EnumZaopeiType.fromId(winccZaopeiType);
    }

    public void setWinccZaopeiType(EnumZaopeiType winccZaopeiType) {
        this.winccZaopeiType = winccZaopeiType == null ? null : winccZaopeiType.getId();
    }

    public Date getWinccChujiaoTime() {
        return winccChujiaoTime;
    }

    public void setWinccChujiaoTime(Date winccChujiaoTime) {
        this.winccChujiaoTime = winccChujiaoTime;
    }

    public MesJiaochi getWinccInputJiaochi() {
        return winccInputJiaochi;
    }

    public void setWinccInputJiaochi(MesJiaochi winccInputJiaochi) {
        this.winccInputJiaochi = winccInputJiaochi;
    }

    public Integer getWinccJiaochiLayer() {
        return winccJiaochiLayer;
    }

    public void setWinccJiaochiLayer(Integer winccJiaochiLayer) {
        this.winccJiaochiLayer = winccJiaochiLayer;
    }

    public Float getWinccDuration() {
        return winccDuration;
    }

    public void setWinccDuration(Float winccDuration) {
        this.winccDuration = winccDuration;
    }

    public Date getWinccEndTime() {
        return winccEndTime;
    }

    public void setWinccEndTime(Date winccEndTime) {
        this.winccEndTime = winccEndTime;
    }

    public Date getWinccStartTime() {
        return winccStartTime;
    }

    public void setWinccStartTime(Date winccStartTime) {
        this.winccStartTime = winccStartTime;
    }

    public MesZhuanyundou getCurrentZhuanyundou() {
        return currentZhuanyundou;
    }

    public void setCurrentZhuanyundou(MesZhuanyundou currentZhuanyundou) {
        this.currentZhuanyundou = currentZhuanyundou;
    }

    public Float getShangzengTotalQty() {
        return shangzengTotalQty;
    }

    public void setShangzengTotalQty(Float shangzengTotalQty) {
        this.shangzengTotalQty = shangzengTotalQty;
    }

    public EnumZaopeiType getZaopeiType() {
        return zaopeiType == null ? null : EnumZaopeiType.fromId(zaopeiType);
    }

    public void setZaopeiType(EnumZaopeiType zaopeiType) {
        this.zaopeiType = zaopeiType == null ? null : zaopeiType.getId();
    }

    public Float getShangzengDuration() {
        return shangzengDuration;
    }

    public void setShangzengDuration(Float shangzengDuration) {
        this.shangzengDuration = shangzengDuration;
    }

    public Float getZhengliuDuration() {
        return zhengliuDuration;
    }

    public void setZhengliuDuration(Float zhengliuDuration) {
        this.zhengliuDuration = zhengliuDuration;
    }

    public Float getZaopeiAddQtyUp() {
        return zaopeiAddQtyUp;
    }

    public void setZaopeiAddQtyUp(Float zaopeiAddQtyUp) {
        this.zaopeiAddQtyUp = zaopeiAddQtyUp;
    }

    public Float getLiangshiAddQtyUp() {
        return liangshiAddQtyUp;
    }

    public void setLiangshiAddQtyUp(Float liangshiAddQtyUp) {
        this.liangshiAddQtyUp = liangshiAddQtyUp;
    }

    public Float getDaokeAddQtyUp() {
        return daokeAddQtyUp;
    }

    public void setDaokeAddQtyUp(Float daokeAddQty) {
        this.daokeAddQtyUp = daokeAddQty;
    }

    public Float getZaopeiAddQtyDown() {
        return zaopeiAddQtyDown;
    }

    public void setZaopeiAddQtyDown(Float zaopeiAddQtyDown) {
        this.zaopeiAddQtyDown = zaopeiAddQtyDown;
    }

    public Float getLiangshiAddQtyDown() {
        return liangshiAddQtyDown;
    }

    public void setLiangshiAddQtyDown(Float liangshiAddQty) {
        this.liangshiAddQtyDown = liangshiAddQty;
    }

    public Float getDaokeAddQtyDown() {
        return daokeAddQtyDown;
    }

    public void setDaokeAddQtyDown(Float daokeAddQtyDown) {
        this.daokeAddQtyDown = daokeAddQtyDown;
    }

    public Integer getZengSequence() {
        return zengSequence;
    }

    public void setZengSequence(Integer zengSequence) {
        this.zengSequence = zengSequence;
    }

    public MesZengguo getResourceZengguo() {
        return resourceZengguo;
    }

    public void setResourceZengguo(MesZengguo resourceZengguo) {
        this.resourceZengguo = resourceZengguo;
    }

    public MesArea getMesArea() {
        return mesArea;
    }

    public void setMesArea(MesArea mesArea) {
        this.mesArea = mesArea;
    }

    public String getTanliangjiCode() {
        return tanliangjiCode;
    }

    public void setTanliangjiCode(String tanliangjiCode) {
        this.tanliangjiCode = tanliangjiCode;
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
