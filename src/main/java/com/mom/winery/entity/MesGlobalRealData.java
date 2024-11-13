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

    @Comment("1#机器人接料斗余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "JIELIAODOU_YUZHONG1")
    private Float jieliaodouYuzhong1;

    @Comment("2#机器人接料斗余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "JIELIAODOU_YUZHONG2")
    private Float jieliaodouYuzhong2;

    @Comment("3#机器人接料斗余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "JIELIAODOU_YUZHONG3")
    private Float jieliaodouYuzhong3;

    @Comment("2#机器人接料斗余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "JIELIAODOU_YUZHONG4")
    private Float jieliaodouYuzhong4;

    public Float getJieliaodouYuzhong4() {
        return jieliaodouYuzhong4;
    }

    public void setJieliaodouYuzhong4(Float jieliaodouYuzhong4) {
        this.jieliaodouYuzhong4 = jieliaodouYuzhong4;
    }

    public Float getJieliaodouYuzhong3() {
        return jieliaodouYuzhong3;
    }

    public void setJieliaodouYuzhong3(Float jieliaodouYuzhong3) {
        this.jieliaodouYuzhong3 = jieliaodouYuzhong3;
    }

    public Float getJieliaodouYuzhong2() {
        return jieliaodouYuzhong2;
    }

    public void setJieliaodouYuzhong2(Float jieliaodouYuzhong2) {
        this.jieliaodouYuzhong2 = jieliaodouYuzhong2;
    }

    public Float getJieliaodouYuzhong1() {
        return jieliaodouYuzhong1;
    }

    public void setJieliaodouYuzhong1(Float jieliaodouYuzhong1) {
        this.jieliaodouYuzhong1 = jieliaodouYuzhong1;
    }

    @Comment("量水罐液位")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LIANGSHUIGUAN_YEWEI")
    private Float liangshuiguanYewei;

    @Comment("尾酒罐液位")
    @NumberFormat(pattern = "##.##")
    @Column(name = "WEIJIUGUAN_YEWEI")
    private Float weijiuguanYewei;

    @Comment("回收底锅水位")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHOU_DIGUO_SHUIWEI")
    private Float huishouDiguoShuiwei;

    @Comment("丢糟尾酒罐液位")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIUZAO_WEIJIUGUAN_YEWEI")
    private Float diuzaoWeijiuguanYewei;

    public Float getLiangshuiguanYewei() {
        return liangshuiguanYewei;
    }

    public void setLiangshuiguanYewei(Float liangshuiguanYewei) {
        this.liangshuiguanYewei = liangshuiguanYewei;
    }

    public Float getWeijiuguanYewei() {
        return weijiuguanYewei;
    }

    public void setWeijiuguanYewei(Float weijiuguanYewei) {
        this.weijiuguanYewei = weijiuguanYewei;
    }

    public Float getHuishouDiguoShuiwei() {
        return huishouDiguoShuiwei;
    }

    public void setHuishouDiguoShuiwei(Float huishouDiguoShuiwei) {
        this.huishouDiguoShuiwei = huishouDiguoShuiwei;
    }

    public Float getDiuzaoWeijiuguanYewei() {
        return diuzaoWeijiuguanYewei;
    }

    public void setDiuZaoWeijiuguanYewei(Float diuzaoWeijiuguanYewei) {this.diuzaoWeijiuguanYewei = diuzaoWeijiuguanYewei;}
}
