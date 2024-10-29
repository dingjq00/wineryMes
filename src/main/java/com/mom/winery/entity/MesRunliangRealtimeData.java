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
@Table(name = "MES_RUNLIANG_REALTIME_DATA", indexes = {
        @Index(name = "IDX_MES_RUNLIANG_REALTIME_DATA_MES_AREA", columnList = "MES_AREA_ID")
})
@Entity
public class MesRunliangRealtimeData {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("生产单元")
    @JoinColumn(name = "MES_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesArea mesArea;

    @Comment("数据更新时间")
    @Column(name = "WINCC_UDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUdateTime;

    @Comment("wincc更新Id")
    @Column(name = "WINCC_UPDATE_ID")
    private Integer winccUpdateId;



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

    // 余重_粗粮暂存仓
    @Comment("粗粮暂存仓余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "CULIANG_ZANCUNCANG_YUZHONG")
    private Float culiangZancuncangYuzhong;

    public Float getCuliangZancuncangYuzhong() {
        return culiangZancuncangYuzhong;
    }

    public void setCuliangZancuncangYuzhong(Float culiangZancuncangYuzhong) {
        this.culiangZancuncangYuzhong = culiangZancuncangYuzhong;
    }

    // 余重_细粮暂存仓
    @Comment("细粮暂存仓余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XILIANG_ZANCUNCANG_YUZHONG")
    private Float xiliangZancuncangYuzhong;

    public Float getXiliangZancuncangYuzhong() {
        return xiliangZancuncangYuzhong;
    }

    public void setXiliangZancuncangYuzhong(Float xiliangZancuncangYuzhong) {
        this.xiliangZancuncangYuzhong = xiliangZancuncangYuzhong;
    }

    // 余重_粗粮料斗称
    @Comment("粗粮料斗余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "CULIANG_LIAODOU_YUZHONG")
    private Float culiangLiaodouYuzhong;

    public Float getCuliangLiaodouYuzhong() {
        return culiangLiaodouYuzhong;
    }

    public void setCuliangLiaodouYuzhong(Float culiangLiaodouYuzhong) {
        this.culiangLiaodouYuzhong = culiangLiaodouYuzhong;
    }

    // 余重_细粮料斗称
    @Comment("细粮料斗余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "XILIANG_LIAODOU_YUZHONG")
    private Float xiliangLiaodouYuzhong;

    public Float getXiliangLiaodouYuzhong() {
        return xiliangLiaodouYuzhong;
    }

    public void setXiliangLiaodouYuzhong(Float xiliangLiaodouYuzhong) {
        this.xiliangLiaodouYuzhong = xiliangLiaodouYuzhong;
    }
    //余重_红糟给料机
    @Comment("红糟给料机余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "HONGZAO_GIVE_LIAOJI_YUZHONG")
    private Float hongzaoGeiLiaojiYuzhong;

    public Float getHongzaoGeiLiaojiYuzhong() {
        return hongzaoGeiLiaojiYuzhong;
    }

    public void setHongzaoGeiLiaojiYuzhong(Float hongzaoGeiLiaojiYuzhong) {
        this.hongzaoGeiLiaojiYuzhong = hongzaoGeiLiaojiYuzhong;
    }

    //余重_中上糟给料机
    @Comment("中上糟给料机余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "ZHONGSHANG_GIVE_LIAOJI_YUZHONG")
    private Float zhongshangGeiLiaojiYuzhong;

    public Float getZhongshangGeiLiaojiYuzhong() {
        return zhongshangGeiLiaojiYuzhong;
    }

    public void setZhongshangGeiLiaojiYuzhong(Float zhongshangGeiLiaojiYuzhong) {
        this.zhongshangGeiLiaojiYuzhong = zhongshangGeiLiaojiYuzhong;
    }
    //余重_底糟给料机
    @Comment("底糟给料机余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "DIZAO_GIVE_LIAOJI_YUZHONG")
    private Float diZaoGeiLiaojiYuzhong;

    public Float getDiZaoGeiLiaojiYuzhong() {
        return diZaoGeiLiaojiYuzhong;
    }

    public void setDiZaoGeiLiaojiYuzhong(Float diZaoGeiLiaojiYuzhong) {
        this.diZaoGeiLiaojiYuzhong = diZaoGeiLiaojiYuzhong;
    }
    //余重_稻壳暂存仓
    @Comment("稻壳暂存仓余重")
    @NumberFormat(pattern = "#.##")
    @Column(name = "DAOKE_ZANCUNCANG_YUZHONG")
    private Float daokeZancuncangYuzhong;

    public Float getDaokeZancuncangYuzhong() {
        return daokeZancuncangYuzhong;
    }

    public void setDaokeZancuncangYuzhong(Float daokeZancuncangYuzhong) {
        this.daokeZancuncangYuzhong = daokeZancuncangYuzhong;
    }

    // 流速_粗粮
    @Comment("粗粮流速")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LIUSU_CULIANG")
    private Float liusuCuliang;

    public Float getLiusuCuliang() {
        return liusuCuliang;
    }

    public void setLiusuCuliang(Float liusuCuliang) {
        this.liusuCuliang = liusuCuliang;
    }
    //流速_细粮
    @Comment("细粮流速")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LIUSU_XILIANG")
    private Float liusuXiliang;

    public Float getLiusuXiliang() {
        return liusuXiliang;
    }

    public void setLiusuXiliang(Float liusuXiliang) {
        this.liusuXiliang = liusuXiliang;
    }
    //流速_红糟
    @Comment("红糟流速")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LIUSU_HONGZAO")
    private Float liusuHongzao;

    public Float getLiusuHongzao() {
        return liusuHongzao;
    }

    public void setLiusuHongzao(Float liusuHongzao) {
        this.liusuHongzao = liusuHongzao;
    }
    //流速_中上糟
    @Comment("中上糟流速")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LIUSU_ZHONGSHANG")
    private Float liusuZhongshang;

    public Float getLiusuZhongshang() {
        return liusuZhongshang;
    }

    public void setLiusuZhongshang(Float liusuZhongshang) {
        this.liusuZhongshang = liusuZhongshang;
    }
    //流速_底糟
    @Comment("底糟流速")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LIUSU_DIZAO")
    private Float liusuDiZao;

    public Float getLiusuDiZao() {
        return liusuDiZao;
    }

    public void setLiusuDiZao(Float liusuDiZao) {
        this.liusuDiZao = liusuDiZao;
    }

    // 润粮水罐液位
    @Comment("润粮水罐液位")
    @NumberFormat(pattern = "#.##")
    @Column(name = "RUNLIANG_SHUIGUAN_YEW")
    private Float runliangShuiguanYew;

    public Float getRunliangShuiguanYew() {
        return runliangShuiguanYew;
    }

    public void setRunliangShuiguanYew(Float runliangShuiguanYew) {
        this.runliangShuiguanYew = runliangShuiguanYew;
    }

    // 润粮罐水温
    @Comment("润粮罐水温")
    @NumberFormat(pattern = "#.##")
    @Column(name = "RUNLIANG_GUAN_SHUIWEN")
    private Float runliangGuanShuiwen;

    public Float getRunliangGuanShuiwen() {
        return runliangGuanShuiwen;
    }

    public void setRunliangGuanShuiwen(Float runliangGuanShuiwen) {
        this.runliangGuanShuiwen = runliangGuanShuiwen;
    }

    // 量水罐水温
    @Comment("量水罐水温")
    @NumberFormat(pattern = "#.##")
    @Column(name = "LIANGSHUI_GUAN_SHUIWEN")
    private Float liangshuiGuanShuiwen;

    public Float getLiangshuiGuanShuiwen() {
        return liangshuiGuanShuiwen;
    }

    public void setLiangshuiGuanShuiwen(Float liangshuiGuanShuiwen) {
        this.liangshuiGuanShuiwen = liangshuiGuanShuiwen;
    }

    public Integer getWinccUpdateId() {
        return winccUpdateId;
    }

    public void setWinccUpdateId(Integer winccUpdateId) {
        this.winccUpdateId = winccUpdateId;
    }

    public Date getWinccUdateTime() {
        return winccUdateTime;
    }

    public void setWinccUdateTime(Date winccUdateTime) {
        this.winccUdateTime = winccUdateTime;
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
