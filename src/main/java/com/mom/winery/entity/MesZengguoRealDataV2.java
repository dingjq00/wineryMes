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
@Table(name = "MES_ZENGGUO_REAL_DATA_V2", indexes = {
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_V2_MES_ZENGGUO", columnList = "MES_ZENGGUO_ID"),
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_V2_MES_ZENGGUO_UNIT_PROCEDURE", columnList = "MES_ZENGGUO_UNIT_PROCEDURE_ID"),
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_V2_MES_ZENGGUO_OPERATION", columnList = "MES_ZENGGUO_OPERATION_ID"),
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_V2_MES_ZENGGUO_RECORD", columnList = "MES_ZENGGUO_RECORD_ID"),
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_V2_MES_AREA", columnList = "MES_AREA_ID"),
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_V2_MES_ZENGGUO_MINI_RECORD", columnList = "MES_ZENGGUO_MINI_RECORD_ID")
})
@Entity
public class MesZengguoRealDataV2 {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("生产单元")
    @JoinColumn(name = "MES_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesArea mesArea;

    @Comment("甑锅")
    @JoinColumn(name = "MES_ZENGGUO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguo mesZengguo;

    @Comment("Wincc更新 Id")
    @NumberFormat(pattern = "#")
    @Column(name = "WINCC_UPDATE_ID")
    private Integer winccUpdateId;

    @Comment("Wincc更新时间")
    @Column(name = "WINCC_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUpdateTime;

    @Comment("蒸汽开度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHENGQI_KAIDU")
    private Float zhengqiKaidu;

    @Comment("冷凝开度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LENGNING_KAIDU")
    private Float lengningKaidu;

    @Comment("蒸汽瞬时流量")
    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG")
    private Float zhengqiShunshiLiuliang;

    @Comment("蒸汽压力")
    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHENGQI_YALI")
    private Float zhengqiYali;

    @Comment("锅内温度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "GUONI_WENDU")
    private Float guoniWendu;

    @Comment("真空压力")
    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHENKONG_YALI")
    private Float zhenkongYali;

    @Comment("底锅水温")
    @NumberFormat(pattern = "#.##")
    @Column(name = "DIGUO_SHUIWEN")
    private Float diguoShuiwen;

    @Comment("出酒温度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "CHUJIU_WENDU")
    private Float chujiuWendu;

    @Comment("回水温度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "HUISHUI_WENDU")
    private Float huishuiWendu;

    @Comment("甑锅UnitProcedure")
    @JoinColumn(name = "MES_ZENGGUO_UNIT_PROCEDURE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguoUnitProcedure mesZengguoUnitProcedure;

    @Comment("甑锅Operation")
    @JoinColumn(name = "MES_ZENGGUO_OPERATION_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguoOperation mesZengguoOperation;

    @Comment("甑锅record")
    @JoinColumn(name = "MES_ZENGGUO_RECORD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguoRecord mesZengguoRecord;

    @Comment("甑锅最小阶段")
    @JoinColumn(name = "MES_ZENGGUO_MINI_RECORD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesZengguoRecord mesZengguoMiniRecord;

    @Comment("顺序号")
    @NumberFormat(pattern = "#")
    @Column(name = "TEMP_INDEX")
    private Integer tempIndex;

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

    public MesZengguoRecord getMesZengguoMiniRecord() {
        return mesZengguoMiniRecord;
    }

    public void setMesZengguoMiniRecord(MesZengguoRecord mesZengguoMiniRecord) {
        this.mesZengguoMiniRecord = mesZengguoMiniRecord;
    }

    public MesArea getMesArea() {
        return mesArea;
    }

    public void setMesArea(MesArea mesArea) {
        this.mesArea = mesArea;
    }

    public Integer getTempIndex() {
        return tempIndex;
    }

    public void setTempIndex(Integer tempIndex) {
        this.tempIndex = tempIndex;
    }

    public MesZengguoRecord getMesZengguoRecord() {
        return mesZengguoRecord;
    }

    public void setMesZengguoRecord(MesZengguoRecord mesZengguoRecord) {
        this.mesZengguoRecord = mesZengguoRecord;
    }

    public MesZengguoOperation getMesZengguoOperation() {
        return mesZengguoOperation;
    }

    public void setMesZengguoOperation(MesZengguoOperation mesZengguoOperation) {
        this.mesZengguoOperation = mesZengguoOperation;
    }

    public MesZengguoUnitProcedure getMesZengguoUnitProcedure() {
        return mesZengguoUnitProcedure;
    }

    public void setMesZengguoUnitProcedure(MesZengguoUnitProcedure mesZengguoUnitProcedure) {
        this.mesZengguoUnitProcedure = mesZengguoUnitProcedure;
    }

    public Float getHuishuiWendu() {
        return huishuiWendu;
    }

    public void setHuishuiWendu(Float huishuiWendu) {
        this.huishuiWendu = huishuiWendu;
    }

    public Float getChujiuWendu() {
        return chujiuWendu;
    }

    public void setChujiuWendu(Float chujiuWendu) {
        this.chujiuWendu = chujiuWendu;
    }

    public Float getDiguoShuiwen() {
        return diguoShuiwen;
    }

    public void setDiguoShuiwen(Float diguoShuiwen) {
        this.diguoShuiwen = diguoShuiwen;
    }

    public Float getZhenkongYali() {
        return zhenkongYali;
    }

    public void setZhenkongYali(Float zhenkongYali) {
        this.zhenkongYali = zhenkongYali;
    }

    public Float getGuoniWendu() {
        return guoniWendu;
    }

    public void setGuoniWendu(Float guoniWendu) {
        this.guoniWendu = guoniWendu;
    }

    public Float getZhengqiYali() {
        return zhengqiYali;
    }

    public void setZhengqiYali(Float zhengqiYali) {
        this.zhengqiYali = zhengqiYali;
    }

    public Float getZhengqiShunshiLiuliang() {
        return zhengqiShunshiLiuliang;
    }

    public void setZhengqiShunshiLiuliang(Float zhengqiShunshiLiuliang) {
        this.zhengqiShunshiLiuliang = zhengqiShunshiLiuliang;
    }

    public Float getLengningKaidu() {
        return lengningKaidu;
    }

    public void setLengningKaidu(Float lengningKaidu) {
        this.lengningKaidu = lengningKaidu;
    }

    public Float getZhengqiKaidu() {
        return zhengqiKaidu;
    }

    public void setZhengqiKaidu(Float zhengqiKaidu) {
        this.zhengqiKaidu = zhengqiKaidu;
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

    public MesZengguo getMesZengguo() {
        return mesZengguo;
    }

    public void setMesZengguo(MesZengguo mesZengguo) {
        this.mesZengguo = mesZengguo;
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
