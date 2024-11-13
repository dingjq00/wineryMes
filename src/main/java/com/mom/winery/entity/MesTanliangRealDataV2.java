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
@Table(name = "MES_TANLIANG_REAL_DATA_V2", indexes = {
        @Index(name = "IDX_MES_TANLIANG_REAL_DATA_V2_MES_AREA", columnList = "MES_AREA_ID"),
        @Index(name = "IDX_MES_TANLIANG_REAL_DATA_V2_MES_TANLIANGJI", columnList = "MES_TANLIANGJI_ID"),
        @Index(name = "IDX_MES_TANLIANG_REAL_DATA_V2_MES_TANLIANG_JI_RECORD", columnList = "MES_TANLIANG_JI_RECORD_ID")
})
@Entity
public class MesTanliangRealDataV2 {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("生产单元")
    @JoinColumn(name = "MES_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesArea mesArea;

    @Comment("摊晾机")
    @JoinColumn(name = "MES_TANLIANGJI_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesTanliangji mesTanliangji;

    @Comment("摊晾记录")
    @JoinColumn(name = "MES_TANLIANG_JI_RECORD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesTanliangjiRecord mesTanliangJiRecord;

    @Comment("给料机余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "TANLIANG_GEILIAOJI_YUZHONG")
    private Float tanliangGeiliaojiYuzhong;

    @NumberFormat(pattern = "#.##")
    @Comment("曲粉暂存仓余重")
    @Column(name = "QUFEI_ZANCUNCANG_YUZHONG")
    private Float qufeiZancuncangYuzhong;

    @NumberFormat(pattern = "#.##")
    @Comment("给料机流速")
    @Column(name = "TANGLIANG_GEILIAOJI_LIUSU")
    private Float tangliangGeiliaojiLiusu;

    @NumberFormat(pattern = "#.##")
    @Comment("曲粉流速")
    @Column(name = "QUFEN_LIUSU")
    private Float qufenLiusu;

    @NumberFormat(pattern = "#.##")
    @Comment("摊晾中部温度")
    @Column(name = "TANLIANG_ZHONGBU_TEMP")
    private Float tanliangZhongbuTemp;

    @NumberFormat(pattern = "#.##")
    @Comment("摊晾出口温度")
    @Column(name = "TANLIANG_CHUKOU_TEMP")
    private Float tanliangChukouTemp;

    @Comment("临时顺序号,用于画图使用")
    @Column(name = "TEMP_INDEX")
    private Integer tempIndex;

    @Comment("Wincc更新Id")
    @Column(name = "WINCC_UPDATE_ID")
    private Integer winccUpdateId;

    @Comment("Wincc更新时间")
    @Column(name = "WINCC_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
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

    public Integer getTempIndex() {
        return tempIndex;
    }

    public void setTempIndex(Integer tempIndex) {
        this.tempIndex = tempIndex;
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

    public MesTanliangjiRecord getMesTanliangJiRecord() {
        return mesTanliangJiRecord;
    }

    public void setMesTanliangJiRecord(MesTanliangjiRecord mesTanliangJiRecord) {
        this.mesTanliangJiRecord = mesTanliangJiRecord;
    }

    public MesTanliangji getMesTanliangji() {
        return mesTanliangji;
    }

    public void setMesTanliangji(MesTanliangji mesTanliangji) {
        this.mesTanliangji = mesTanliangji;
    }

    public MesArea getMesArea() {
        return mesArea;
    }

    public void setMesArea(MesArea mesArea) {
        this.mesArea = mesArea;
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


    // Getter and Setter for tanliangGeiliaojiYuzhong
    public Float getTanliangGeiliaojiYuzhong() {
        return tanliangGeiliaojiYuzhong;
    }

    public void setTanliangGeiliaojiYuzhong(Float tanliangGeiliaojiYuzhong) {
        this.tanliangGeiliaojiYuzhong = tanliangGeiliaojiYuzhong;
    }

    // Getter and Setter for qufeiZancuncangYuzhong
    public Float getQufeiZancuncangYuzhong() {
        return qufeiZancuncangYuzhong;
    }

    public void setQufeiZancuncangYuzhong(Float qufeiZancuncangYuzhong) {
        this.qufeiZancuncangYuzhong = qufeiZancuncangYuzhong;
    }

    // Getter and Setter for tangliangGeiliaojiLiusu
    public Float getTangliangGeiliaojiLiusu() {
        return tangliangGeiliaojiLiusu;
    }

    public void setTangliangGeiliaojiLiusu(Float tangliangGeiliaojiLiusu) {
        this.tangliangGeiliaojiLiusu = tangliangGeiliaojiLiusu;
    }

    // Getter and Setter for qufenLiusu
    public Float getQufenLiusu() {
        return qufenLiusu;
    }

    public void setQufenLiusu(Float qufenLiusu) {
        this.qufenLiusu = qufenLiusu;
    }

    // Getter and Setter for tanliangZhongbuTemp
    public Float getTanliangZhongbuTemp() {
        return tanliangZhongbuTemp;
    }

    public void setTanliangZhongbuTemp(Float tanliangZhongbuTemp) {
        this.tanliangZhongbuTemp = tanliangZhongbuTemp;
    }

    // Getter and Setter for tanliangChukouTemp
    public Float getTanliangChukouTemp() {
        return tanliangChukouTemp;
    }

    public void setTanliangChukouTemp(Float tanliangChukouTemp) {
        this.tanliangChukouTemp = tanliangChukouTemp;
    }
}
