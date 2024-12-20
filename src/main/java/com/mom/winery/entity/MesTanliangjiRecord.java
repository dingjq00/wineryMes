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
@Table(name = "MES_TANLIANGJI_RECORD", indexes = {
        @Index(name = "IDX_MES_TANLIANGJI_RECORD_RESOURCE_ZENGGUO", columnList = "RESOURCE_ZENGGUO_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_RECORD_CURRENT_ZHUANYUNDOU", columnList = "CURRENT_ZHUANYUNDOU_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_RECORD_WINCC_INPUT_JIAOCHI", columnList = "WINCC_INPUT_JIAOCHI_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_RECORD_MES_TANLIANGJI", columnList = "MES_TANLIANGJI_ID"),
        @Index(name = "IDX_MES_TANLIANGJI_RECORD_SHIFT_TEAM", columnList = "SHIFT_TEAM_ID")
})
@Entity
public class MesTanliangjiRecord {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @JoinColumn(name = "MES_TANLIANGJI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesTanliangji mesTanliangji;

    @JoinColumn(name = "RESOURCE_ZENGGUO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguo resourceZengguo;

    @Column(name = "ZENG_SEQUENCE")
    private Integer zengSequence;

    @Column(name = "PHASE_START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date phaseStartTime;

    @Column(name = "PHASE_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date phaseEndTime;

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

    @NumberFormat(pattern = "#.#")
    @Column(name = "PHASE_DURATION")
    private Float phaseDuration;

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

    @NumberFormat(pattern = "#############")
    @Column(name = "WINCC_START_ID")
    private Integer winccStartId;

    @NumberFormat(pattern = "##############")
    @Column(name = "WINCC_END_ID")
    private Integer winccEndId;

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

    @Comment("班组")
    @JoinColumn(name = "SHIFT_TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesShiftTeam shiftTeam;

    @Comment("班次")
    @Column(name = "ENUM_SHIFT")
    private String enumShift;

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

    public Date getPhaseEndTime() {
        return phaseEndTime;
    }

    public void setPhaseEndTime(Date phaseEndTime) {
        this.phaseEndTime = phaseEndTime;
    }

    public Date getPhaseStartTime() {
        return phaseStartTime;
    }

    public void setPhaseStartTime(Date phaseStartTime) {
        this.phaseStartTime = phaseStartTime;
    }

    public Float getPhaseDuration() {
        return phaseDuration;
    }

    public void setPhaseDuration(Float phaseDuration) {
        this.phaseDuration = phaseDuration;
    }

    public Float getLiangshuiAddQty() {
        return liangshuiAddQty;
    }

    public void setLiangshuiAddQty(Float liangshuiAddQty) {
        this.liangshuiAddQty = liangshuiAddQty;
    }

    public Integer getWinccEndId() {
        return winccEndId;
    }

    public void setWinccEndId(Integer winccEndId) {
        this.winccEndId = winccEndId;
    }

    public Integer getWinccStartId() {
        return winccStartId;
    }

    public void setWinccStartId(Integer winccStartId) {
        this.winccStartId = winccStartId;
    }

    public MesTanliangji getMesTanliangji() {
        return mesTanliangji;
    }

    public void setMesTanliangji(MesTanliangji mesTanliangji) {
        this.mesTanliangji = mesTanliangji;
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
