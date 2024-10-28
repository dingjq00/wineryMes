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
@Table(name = "MES_TANLIANGJI_REAL_TIME_DATA", indexes = {
        @Index(name = "IDX_MES_TANLIANGJI_REAL_TIME_DATA_MES_AREA", columnList = "MES_AREA_ID")
})
@Entity
public class MesTanliangjiRealTimeData {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("发生时间")
    @Column(name = "WINCC_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUpdateTime;

    @Comment("生产单元")
    @JoinColumn(name = "MES_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesArea mesArea;

    @NumberFormat(pattern = "#.##")
    @Comment("余重_1#摊晾给料机")
    @Column(name = "TANLIANG_GEILIAOJI_YUZHONG1")
    private Float tanliangGeiliaojiYuzhong1;

    @Comment("余重_2#摊晾给料机")
    @NumberFormat(pattern = "#.##")
    @Column(name = "TANLIANG_GEILIAOJI_YUZHONG2")
    private Float tanliangGeiliaojiYuzhong2;

    @NumberFormat(pattern = "#.##")
    @Comment("余重_1#曲粉暂存仓")
    @Column(name = "QUFEI_ZANCUNCANG_YUZHONG1")
    private Float qufeiZancuncangYuzhong1;

    @NumberFormat(pattern = "#.##")
    @Comment("余重_2#曲粉暂存仓")
    @Column(name = "QUFEI_ZANCUNCANG_YUZHONG2")
    private Float qufeiZancuncangYuzhong2;

    @NumberFormat(pattern = "#.##")
    @Comment("流速_1#摊晾给料机")
    @Column(name = "TANGLIANG_GEILIAOJI_LIUSU1")
    private Float tangliangGeiliaojiLiusu1;

    @NumberFormat(pattern = "#.##")
    @Comment("流速_2#摊晾给料机")
    @Column(name = "TANGLIANG_GEILIAOJI_LIUSU2")
    private Float tangliangGeiliaojiLiusu2;

    @NumberFormat(pattern = "#.##")
    @Comment("流速_1#曲粉")
    @Column(name = "QUFEN_LIUSU1")
    private Float qufenLiusu1;

    @NumberFormat(pattern = "#.##")
    @Comment("流速_2#曲粉")
    @Column(name = "QUFEN_LIUSU2")
    private Float qufenLiusu2;

    @NumberFormat(pattern = "#.##")
    @Comment("1#摊晾中部温度")
    @Column(name = "TANLIANG_ZHONGBU_TEMP1")
    private Float tanliangZhongbuTemp1;

    @NumberFormat(pattern = "#.##")
    @Comment("2#摊晾中部温度")
    @Column(name = "TANLIANG_ZHONGBU_TEMP2")
    private Float tanliangZhongbuTemp2;

    @NumberFormat(pattern = "#.##")
    @Comment("1#摊晾出口温度")
    @Column(name = "TANLIANG_CHUKOU_TEMP1")
    private Float tanliangChukouTemp1;

    @NumberFormat(pattern = "#.##")
    @Comment("2#摊晾出口温度")
    @Column(name = "TANLIANG_CHUKOU_TEMP2")
    private Float tanliangChukouTemp2;

    @Column(name = "WINCC_ID")
    private Integer winccId;

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

    public Integer getWinccId() {
        return winccId;
    }

    public void setWinccId(Integer winccId) {
        this.winccId = winccId;
    }

    public MesArea getMesArea() {
        return mesArea;
    }

    public void setMesArea(MesArea mesArea) {
        this.mesArea = mesArea;
    }

    public Date getWinccUpdateTime() {
        return winccUpdateTime;
    }

    public void setWinccUpdateTime(Date winccUpdateTime) {
        this.winccUpdateTime = winccUpdateTime;
    }

    public Float getTanliangChukouTemp2() {
        return tanliangChukouTemp2;
    }

    public void setTanliangChukouTemp2(Float tanliangChukouTemp2) {
        this.tanliangChukouTemp2 = tanliangChukouTemp2;
    }

    public Float getTanliangZhongbuTemp2() {
        return tanliangZhongbuTemp2;
    }

    public void setTanliangZhongbuTemp2(Float tanliangZhongbuTemp2) {
        this.tanliangZhongbuTemp2 = tanliangZhongbuTemp2;
    }

    public Float getTanliangChukouTemp1() {
        return tanliangChukouTemp1;
    }

    public void setTanliangChukouTemp1(Float tanliangChukouTemp1) {
        this.tanliangChukouTemp1 = tanliangChukouTemp1;
    }

    public Float getTanliangZhongbuTemp1() {
        return tanliangZhongbuTemp1;
    }

    public void setTanliangZhongbuTemp1(Float tanliangZhongbuTemp1) {
        this.tanliangZhongbuTemp1 = tanliangZhongbuTemp1;
    }

    public Float getQufenLiusu2() {
        return qufenLiusu2;
    }

    public void setQufenLiusu2(Float qufenLiusu2) {
        this.qufenLiusu2 = qufenLiusu2;
    }

    public Float getQufenLiusu1() {
        return qufenLiusu1;
    }

    public void setQufenLiusu1(Float qufenLiusu1) {
        this.qufenLiusu1 = qufenLiusu1;
    }

    public Float getTangliangGeiliaojiLiusu2() {
        return tangliangGeiliaojiLiusu2;
    }

    public void setTangliangGeiliaojiLiusu2(Float tangliangGeiliaojiLiusu2) {
        this.tangliangGeiliaojiLiusu2 = tangliangGeiliaojiLiusu2;
    }

    public Float getTangliangGeiliaojiLiusu1() {
        return tangliangGeiliaojiLiusu1;
    }

    public void setTangliangGeiliaojiLiusu1(Float tangliangGeiliaojiLiusu1) {
        this.tangliangGeiliaojiLiusu1 = tangliangGeiliaojiLiusu1;
    }

    public Float getQufeiZancuncangYuzhong2() {
        return qufeiZancuncangYuzhong2;
    }

    public void setQufeiZancuncangYuzhong2(Float qufeiZancuncangYuzhong2) {
        this.qufeiZancuncangYuzhong2 = qufeiZancuncangYuzhong2;
    }

    public Float getQufeiZancuncangYuzhong1() {
        return qufeiZancuncangYuzhong1;
    }

    public void setQufeiZancuncangYuzhong1(Float qufeiZancuncangYuzhong1) {
        this.qufeiZancuncangYuzhong1 = qufeiZancuncangYuzhong1;
    }

    public Float getTanliangGeiliaojiYuzhong2() {
        return tanliangGeiliaojiYuzhong2;
    }

    public void setTanliangGeiliaojiYuzhong2(Float tanliangGeiliaojiYuzhong2) {
        this.tanliangGeiliaojiYuzhong2 = tanliangGeiliaojiYuzhong2;
    }

    public Float getTanliangGeiliaojiYuzhong1() {
        return tanliangGeiliaojiYuzhong1;
    }

    public void setTanliangGeiliaojiYuzhong1(Float tanliangGeiliaojiYuzhong1) {
        this.tanliangGeiliaojiYuzhong1 = tanliangGeiliaojiYuzhong1;
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
