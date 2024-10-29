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

@Comment("全局模拟量")
@JmixEntity
@Table(name = "MES_GLOBAL_REAL_DATA", indexes = {
        @Index(name = "IDX_MES_GLOBAL_REAL_DATA_MES_AREA", columnList = "MES_AREA_ID")
})
@Entity
public class MesGlobalRealData {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("生产单元")
    @JoinColumn(name = "MES_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesArea mesArea;

    @Comment("更新时间")
    @Column(name = "WINCC_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUpdateTime;

    @Comment("wincc更新Id")
    @Column(name = "WINCC_UPDATE_ID")
    private Integer winccUpdateId;
    // 乏汽冷凝调节阀开度
    @Comment("乏汽冷凝调节阀开度")
    @NumberFormat(pattern = "#.##")
    @Column(name = "FAQI_LENGNING_TIAOJIEFA_KAIDU")
    private Float faqiLengningTiaojiefaKaidu;


    public Float getFaqiLengningTiaojiefaKaidu() {
        return faqiLengningTiaojiefaKaidu;
    }

    public void setFaqiLengningTiaojiefaKaidu(Float faqiLengningTiaojiefaKaidu) {
        this.faqiLengningTiaojiefaKaidu = faqiLengningTiaojiefaKaidu;
    }

    // 清洗水罐液位
    @Comment("清洗水罐液位")
    @NumberFormat(pattern = "#.##")
    @Column(name = "QINGXI_SHUIGUAN_YEW")
    private Float qingxiShuiguanYew;

    public Float getQingxiShuiguanYew() {
        return qingxiShuiguanYew;
    }

    public void setQingxiShuiguanYew(Float qingxiShuiguanYew) {
        this.qingxiShuiguanYew = qingxiShuiguanYew;
    }

    // 循环水缓存罐液位
    @Comment("循环水缓存罐液位")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XUNHUA_SHUI_HUANCUNGUAN_YEW")
    private Float xunhuaShuiHuancunguanYew;

    public Float getXunhuaShuiHuancunguanYew() {
        return xunhuaShuiHuancunguanYew;
    }

    public void setXunhuaShuiHuancunguanYew(Float xunhuaShuiHuancunguanYew) {
        this.xunhuaShuiHuancunguanYew = xunhuaShuiHuancunguanYew;
    }

    // 清洗水罐水温
    @Comment("清洗水罐水温")
    @NumberFormat(pattern = "#.##")
    @Column(name = "QINGXI_SHUIGUAN_SHUIWEN")
    private Float qingxiShuiguanShuiwen;

    public Float getQingxiShuiguanShuiwen() {
        return qingxiShuiguanShuiwen;
    }

    public void setQingxiShuiguanShuiwen(Float qingxiShuiguanShuiwen) {
        this.qingxiShuiguanShuiwen = qingxiShuiguanShuiwen;
    }

    // 循环水温-进水
    @Comment("循环水温-进水")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XUNHUA_SHUIWEN_JINSHUI")
    private Float xunhuaShuiwenJinshui;

    public Float getXunhuaShuiwenJinshui() {
        return xunhuaShuiwenJinshui;
    }

    public void setXunhuaShuiwenJinshui(Float xunhuaShuiwenJinshui) {
        this.xunhuaShuiwenJinshui = xunhuaShuiwenJinshui;
    }

    // 循环水温-回水
    @Comment("循环水温-回水")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XUNHUA_SHUIWEN_HUISHUI")
    private Float xunhuaShuiwenHuishui;

    public Float getXunhuaShuiwenHuishui() {
        return xunhuaShuiwenHuishui;
    }

    public void setXunhuaShuiwenHuishui(Float xunhuaShuiwenHuishui) {
        this.xunhuaShuiwenHuishui = xunhuaShuiwenHuishui;
    }

    // 循环进水流量
    @Comment("循环进水流量")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XUNHUA_JINSHUI_LIULIANG")
    private Float xunhuaJinshuiLiuliang;

    public Float getXunhuaJinshuiLiuliang() {
        return xunhuaJinshuiLiuliang;
    }

    public void setXunhuaJinshuiLiuliang(Float xunhuaJinshuiLiuliang) {
        this.xunhuaJinshuiLiuliang = xunhuaJinshuiLiuliang;
    }

    // 循环出水流量
    @Comment("循环出水流量")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XUNHUA_CHUSHUI_LIULIANG")
    private Float xunhuaChushuiLiuliang;

    public Float getXunhuaChushuiLiuliang() {
        return xunhuaChushuiLiuliang;
    }

    public void setXunhuaChushuiLiuliang(Float xunhuaChushuiLiuliang) {
        this.xunhuaChushuiLiuliang = xunhuaChushuiLiuliang;
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


    public Integer getWinccUpdateId() {
        return winccUpdateId;
    }

    public void setWinccUpdateId(Integer winccUpdateId) {
        this.winccUpdateId = winccUpdateId;
    }

    public Date getWinccUpdateTime() {
        return winccUpdateTime;
    }

    public void setWinccUpdateTime(Date winccUpdateTime) {
        this.winccUpdateTime = winccUpdateTime;
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
}
