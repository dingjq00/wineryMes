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
@Table(name = "MES_ZENGGUO_REAL_DATA", indexes = {
        @Index(name = "IDX_MES_ZENGGUO_REAL_DATA_MES_AREA", columnList = "MES_AREA_ID")
})
@Entity
public class MesZengguoRealData {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("生产单元")
    @JoinColumn(name = "MES_AREA_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesArea mesArea;

    @Comment("数据更新时间")
    @Column(name = "WINCC_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date winccUpdateTime;

    @Comment("数据更新WinccId")
    @Column(name = "WINCC_UPDATE_ID")
    private Integer winccUpdateId;

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

    @Comment("1#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU1")
    private Float zhengqiKaidu1;

    @Comment("2#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU2")
    private Float zhengqiKaidu2;

    @Comment("3#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU3")
    private Float zhengqiKaidu3;

    @Comment("4#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU4")
    private Float zhengqiKaidu4;

    @Comment("5#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU5")
    private Float zhengqiKaidu5;

    @Comment("6#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU6")
    private Float zhengqiKaidu6;

    @Comment("7#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU7")
    private Float zhengqiKaidu7;

    @Comment("8#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU8")
    private Float zhengqiKaidu8;

    @Comment("9#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU9")
    private Float zhengqiKaidu9;

    @Comment("10#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU10")
    private Float zhengqiKaidu10;

    @Comment("11#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU11")
    private Float zhengqiKaidu11;

    @Comment("12#蒸汽开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_KAIDU12")
    private Float zhengqiKaidu12;

    @Comment("1#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU1")
    private Float lengningKaidu1;

    @Comment("2#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU2")
    private Float lengningKaidu2;

    @Comment("3#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU3")
    private Float lengningKaidu3;

    @Comment("4#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU4")
    private Float lengningKaidu4;

    @Comment("5#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU5")
    private Float lengningKaidu5;

    @Comment("6#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU6")
    private Float lengningKaidu6;

    @Comment("7#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU7")
    private Float lengningKaidu7;

    @Comment("8#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU8")
    private Float lengningKaidu8;

    @Comment("9#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU9")
    private Float lengningKaidu9;

    @Comment("10#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU10")
    private Float lengningKaidu10;

    @Comment("11#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU11")
    private Float lengningKaidu11;

    @Comment("12#冷凝开度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "LENGNING_KAIDU12")
    private Float lengningKaidu12;

    @Comment("1#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG1")
    private Float zhengqiShunshiLiuliang1;

    @Comment("2#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG2")
    private Float zhengqiShunshiLiuliang2;

    @Comment("3#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG3")
    private Float zhengqiShunshiLiuliang3;

    @Comment("4#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG4")
    private Float zhengqiShunshiLiuliang4;

    @Comment("5#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG5")
    private Float zhengqiShunshiLiuliang5;

    @Comment("6#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG6")
    private Float zhengqiShunshiLiuliang6;

    @Comment("7#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG7")
    private Float zhengqiShunshiLiuliang7;

    @Comment("8#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG8")
    private Float zhengqiShunshiLiuliang8;

    @Comment("9#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG9")
    private Float zhengqiShunshiLiuliang9;

    @Comment("10#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG10")
    private Float zhengqiShunshiLiuliang10;

    @Comment("11#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG11")
    private Float zhengqiShunshiLiuliang11;

    @Comment("12#蒸汽瞬时流量")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_SHUNSHI_LIULIANG12")
    private Float zhengqiShunshiLiuliang12;

    @Comment("1#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI1")
    private Float zhengqiYali1;

    @Comment("2#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI2")
    private Float zhengqiYali2;

    @Comment("3#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI3")
    private Float zhengqiYali3;

    @Comment("4#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI4")
    private Float zhengqiYali4;

    @Comment("5#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI5")
    private Float zhengqiYali5;

    @Comment("6#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI6")
    private Float zhengqiYali6;

    @Comment("7#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI7")
    private Float zhengqiYali7;

    @Comment("8#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI8")
    private Float zhengqiYali8;

    @Comment("9#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI9")
    private Float zhengqiYali9;

    @Comment("10#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI10")
    private Float zhengqiYali10;

    @Comment("11#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI11")
    private Float zhengqiYali11;

    @Comment("12#蒸汽压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENGQI_YALI12")
    private Float zhengqiYali12;

    @Comment("1#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU1")
    private Float guoniWendu1;

    @Comment("2#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU2")
    private Float guoniWendu2;

    @Comment("3#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU3")
    private Float guoniWendu3;

    @Comment("4#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU4")
    private Float guoniWendu4;

    @Comment("5#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU5")
    private Float guoniWendu5;

    @Comment("6#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU6")
    private Float guoniWendu6;

    @Comment("7#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU7")
    private Float guoniWendu7;

    @Comment("8#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU8")
    private Float guoniWendu8;

    @Comment("9#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU9")
    private Float guoniWendu9;

    @Comment("10#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU10")
    private Float guoniWendu10;

    @Comment("11#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU11")
    private Float guoniWendu11;

    @Comment("12#锅内温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "GUONI_WENDU12")
    private Float guoniWendu12;

    @Comment("1#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI1")
    private Float zhenkongYali1;

    @Comment("2#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI2")
    private Float zhenkongYali2;

    @Comment("3#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI3")
    private Float zhenkongYali3;

    @Comment("4#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI4")
    private Float zhenkongYali4;

    @Comment("5#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI5")
    private Float zhenkongYali5;

    @Comment("6#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI6")
    private Float zhenkongYali6;

    @Comment("7#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI7")
    private Float zhenkongYali7;

    @Comment("8#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI8")
    private Float zhenkongYali8;

    @Comment("9#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI9")
    private Float zhenkongYali9;

    @Comment("10#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI10")
    private Float zhenkongYali10;

    @Comment("11#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI11")
    private Float zhenkongYali11;

    @Comment("12#真空压力")
    @NumberFormat(pattern = "##.##")
    @Column(name = "ZHENKONG_YALI12")
    private Float zhenkongYali12;

    @Comment("1#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN1")
    private Float diguoShuiwen1;

    @Comment("2#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN2")
    private Float diguoShuiwen2;

    @Comment("3#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN3")
    private Float diguoShuiwen3;

    @Comment("4#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN4")
    private Float diguoShuiwen4;

    @Comment("5#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN5")
    private Float diguoShuiwen5;

    @Comment("6#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN6")
    private Float diguoShuiwen6;

    @Comment("7#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN7")
    private Float diguoShuiwen7;

    @Comment("8#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN8")
    private Float diguoShuiwen8;

    @Comment("9#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN9")
    private Float diguoShuiwen9;

    @Comment("10#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN10")
    private Float diguoShuiwen10;

    @Comment("11#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN11")
    private Float diguoShuiwen11;

    @Comment("12#底锅水温")
    @NumberFormat(pattern = "##.##")
    @Column(name = "DIGUO_SHUIWEN12")
    private Float diguoShuiwen12;

    @Comment("1#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU1")
    private Float chujiuWendu1;

    @Comment("2#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU2")
    private Float chujiuWendu2;

    @Comment("3#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU3")
    private Float chujiuWendu3;

    @Comment("4#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU4")
    private Float chujiuWendu4;

    @Comment("5#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU5")
    private Float chujiuWendu5;

    @Comment("6#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU6")
    private Float chujiuWendu6;

    @Comment("7#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU7")
    private Float chujiuWendu7;

    @Comment("8#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU8")
    private Float chujiuWendu8;

    @Comment("9#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU9")
    private Float chujiuWendu9;

    @Comment("10#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU10")
    private Float chujiuWendu10;

    @Comment("11#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU11")
    private Float chujiuWendu11;

    @Comment("12#出酒温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "CHUJIU_WENDU12")
    private Float chujiuWendu12;

    @Comment("1#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU1")
    private Float huishuiWendu1;

    @Comment("2#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU2")
    private Float huishuiWendu2;

    @Comment("3#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU3")
    private Float huishuiWendu3;

    @Comment("4#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU4")
    private Float huishuiWendu4;

    @Comment("5#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU5")
    private Float huishuiWendu5;

    @Comment("6#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU6")
    private Float huishuiWendu6;

    @Comment("7#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU7")
    private Float huishuiWendu7;

    @Comment("8#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU8")
    private Float huishuiWendu8;

    @Comment("9#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU9")
    private Float huishuiWendu9;

    @Comment("10#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU10")
    private Float huishuiWendu10;

    @Comment("11#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU11")
    private Float huishuiWendu11;

    @Comment("12#回水温度")
    @NumberFormat(pattern = "##.##")
    @Column(name = "HUISHUI_WENDU12")
    private Float huishuiWendu12;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getZhengqiKaidu1() {
        return zhengqiKaidu1;
    }

    public void setZhengqiKaidu1(Float zhengqiKaidu1) {
        this.zhengqiKaidu1 = zhengqiKaidu1;
    }


    public Float getZhengqiKaidu2() {
        return zhengqiKaidu2;
    }

    public void setZhengqiKaidu2(Float zhengqiKaidu2) {
        this.zhengqiKaidu2 = zhengqiKaidu2;
    }

    public Float getZhengqiKaidu3() {
        return zhengqiKaidu3;
    }

    public void setZhengqiKaidu3(Float zhengqiKaidu3) {
        this.zhengqiKaidu3 = zhengqiKaidu3;
    }

    public Float getZhengqiKaidu4() {
        return zhengqiKaidu4;
    }

    public void setZhengqiKaidu4(Float zhengqiKaidu4) {
        this.zhengqiKaidu4 = zhengqiKaidu4;
    }

    public Float getZhengqiKaidu5() {
        return zhengqiKaidu5;
    }

    public void setZhengqiKaidu5(Float zhengqiKaidu5) {
        this.zhengqiKaidu5 = zhengqiKaidu5;
    }

    public Float getZhengqiKaidu6() {
        return zhengqiKaidu6;
    }

    public void setZhengqiKaidu6(Float zhengqiKaidu6) {
        this.zhengqiKaidu6 = zhengqiKaidu6;
    }

    public Float getZhengqiKaidu7() {
        return zhengqiKaidu7;
    }

    public void setZhengqiKaidu7(Float zhengqiKaidu7) {
        this.zhengqiKaidu7 = zhengqiKaidu7;
    }

    public Float getZhengqiKaidu8() {
        return zhengqiKaidu8;
    }

    public void setZhengqiKaidu8(Float zhengqiKaidu8) {
        this.zhengqiKaidu8 = zhengqiKaidu8;
    }

    public Float getZhengqiKaidu9() {
        return zhengqiKaidu9;
    }

    public void setZhengqiKaidu9(Float zhengqiKaidu9) {
        this.zhengqiKaidu9 = zhengqiKaidu9;
    }

    public Float getZhengqiKaidu10() {
        return zhengqiKaidu10;
    }

    public void setZhengqiKaidu10(Float zhengqiKaidu10) {
        this.zhengqiKaidu10 = zhengqiKaidu10;
    }

    public Float getZhengqiKaidu11() {
        return zhengqiKaidu11;
    }

    public void setZhengqiKaidu11(Float zhengqiKaidu11) {
        this.zhengqiKaidu11 = zhengqiKaidu11;
    }

    public Float getZhengqiKaidu12() {
        return zhengqiKaidu12;
    }

    public void setZhengqiKaidu12(Float zhengqiKaidu12) {
        this.zhengqiKaidu12 = zhengqiKaidu12;
    }

    public Float getLengningKaidu1() {
        return lengningKaidu1;
    }

    public void setLengningKaidu1(Float lengningKaidu1) {
        this.lengningKaidu1 = lengningKaidu1;
    }

    public Float getLengningKaidu2() {
        return lengningKaidu2;
    }

    public void setLengningKaidu2(Float lengningKaidu2) {
        this.lengningKaidu2 = lengningKaidu2;
    }

    public Float getLengningKaidu3() {
        return lengningKaidu3;
    }

    public void setLengningKaidu3(Float lengningKaidu3) {
        this.lengningKaidu3 = lengningKaidu3;
    }

    public Float getLengningKaidu4() {
        return lengningKaidu4;
    }

    public void setLengningKaidu4(Float lengningKaidu4) {
        this.lengningKaidu4 = lengningKaidu4;
    }

    public Float getLengningKaidu5() {
        return lengningKaidu5;
    }

    public void setLengningKaidu5(Float lengningKaidu5) {
        this.lengningKaidu5 = lengningKaidu5;
    }

    public Float getLengningKaidu6() {
        return lengningKaidu6;
    }

    public void setLengningKaidu6(Float lengningKaidu6) {
        this.lengningKaidu6 = lengningKaidu6;
    }

    public Float getLengningKaidu7() {
        return lengningKaidu7;
    }

    public void setLengningKaidu7(Float lengningKaidu7) {
        this.lengningKaidu7 = lengningKaidu7;
    }

    public Float getLengningKaidu8() {
        return lengningKaidu8;
    }

    public void setLengningKaidu8(Float lengningKaidu8) {
        this.lengningKaidu8 = lengningKaidu8;
    }

    public Float getLengningKaidu9() {
        return lengningKaidu9;
    }

    public void setLengningKaidu9(Float lengningKaidu9) {
        this.lengningKaidu9 = lengningKaidu9;
    }

    public Float getLengningKaidu10() {
        return lengningKaidu10;
    }

    public void setLengningKaidu10(Float lengningKaidu10) {
        this.lengningKaidu10 = lengningKaidu10;
    }

    public Float getLengningKaidu11() {
        return lengningKaidu11;
    }

    public void setLengningKaidu11(Float lengningKaidu11) {
        this.lengningKaidu11 = lengningKaidu11;
    }

    public Float getLengningKaidu12() {
        return lengningKaidu12;
    }

    public void setLengningKaidu12(Float lengningKaidu12) {
        this.lengningKaidu12 = lengningKaidu12;
    }

    public Float getZhengqiShunshiLiuliang1() {
        return zhengqiShunshiLiuliang1;
    }

    public void setZhengqiShunshiLiuliang1(Float zhengqiShunshiLiuliang1) {
        this.zhengqiShunshiLiuliang1 = zhengqiShunshiLiuliang1;
    }

    public Float getZhengqiShunshiLiuliang2() {
        return zhengqiShunshiLiuliang2;
    }

    public void setZhengqiShunshiLiuliang2(Float zhengqiShunshiLiuliang2) {
        this.zhengqiShunshiLiuliang2 = zhengqiShunshiLiuliang2;
    }

    public Float getZhengqiShunshiLiuliang3() {
        return zhengqiShunshiLiuliang3;
    }

    public void setZhengqiShunshiLiuliang3(Float zhengqiShunshiLiuliang3) {
        this.zhengqiShunshiLiuliang3 = zhengqiShunshiLiuliang3;
    }

    public Float getZhengqiShunshiLiuliang4() {
        return zhengqiShunshiLiuliang4;
    }

    public void setZhengqiShunshiLiuliang4(Float zhengqiShunshiLiuliang4) {
        this.zhengqiShunshiLiuliang4 = zhengqiShunshiLiuliang4;
    }

    public Float getZhengqiShunshiLiuliang5() {
        return zhengqiShunshiLiuliang5;
    }

    public void setZhengqiShunshiLiuliang5(Float zhengqiShunshiLiuliang5) {
        this.zhengqiShunshiLiuliang5 = zhengqiShunshiLiuliang5;
    }

    public Float getZhengqiShunshiLiuliang6() {
        return zhengqiShunshiLiuliang6;
    }

    public void setZhengqiShunshiLiuliang6(Float zhengqiShunshiLiuliang6) {
        this.zhengqiShunshiLiuliang6 = zhengqiShunshiLiuliang6;
    }

    public Float getZhengqiShunshiLiuliang7() {
        return zhengqiShunshiLiuliang7;
    }

    public void setZhengqiShunshiLiuliang7(Float zhengqiShunshiLiuliang7) {
        this.zhengqiShunshiLiuliang7 = zhengqiShunshiLiuliang7;
    }

    public Float getZhengqiShunshiLiuliang8() {
        return zhengqiShunshiLiuliang8;
    }

    public void setZhengqiShunshiLiuliang8(Float zhengqiShunshiLiuliang8) {
        this.zhengqiShunshiLiuliang8 = zhengqiShunshiLiuliang8;
    }

    public Float getZhengqiShunshiLiuliang9() {
        return zhengqiShunshiLiuliang9;
    }

    public void setZhengqiShunshiLiuliang9(Float zhengqiShunshiLiuliang9) {
        this.zhengqiShunshiLiuliang9 = zhengqiShunshiLiuliang9;
    }

    public Float getZhengqiShunshiLiuliang10() {
        return zhengqiShunshiLiuliang10;
    }

    public void setZhengqiShunshiLiuliang10(Float zhengqiShunshiLiuliang10) {
        this.zhengqiShunshiLiuliang10 = zhengqiShunshiLiuliang10;
    }

    public Float getZhengqiShunshiLiuliang11() {
        return zhengqiShunshiLiuliang11;
    }

    public void setZhengqiShunshiLiuliang11(Float zhengqiShunshiLiuliang11) {
        this.zhengqiShunshiLiuliang11 = zhengqiShunshiLiuliang11;
    }

    public Float getZhengqiShunshiLiuliang12() {
        return zhengqiShunshiLiuliang12;
    }

    public void setZhengqiShunshiLiuliang12(Float zhengqiShunshiLiuliang12) {
        this.zhengqiShunshiLiuliang12 = zhengqiShunshiLiuliang12;
    }


    public Float getZhengqiYali1() {
        return zhengqiYali1;
    }

    public void setZhengqiYali1(Float zhengqiYali1) {
        this.zhengqiYali1 = zhengqiYali1;
    }

    public Float getZhengqiYali2() {
        return zhengqiYali2;
    }

    public void setZhengqiYali2(Float zhengqiYali2) {
        this.zhengqiYali2 = zhengqiYali2;
    }

    public Float getZhengqiYali3() {
        return zhengqiYali3;
    }

    public void setZhengqiYali3(Float zhengqiYali3) {
        this.zhengqiYali3 = zhengqiYali3;
    }

    public Float getZhengqiYali4() {
        return zhengqiYali4;
    }

    public void setZhengqiYali4(Float zhengqiYali4) {
        this.zhengqiYali4 = zhengqiYali4;
    }

    public Float getZhengqiYali5() {
        return zhengqiYali5;
    }

    public void setZhengqiYali5(Float zhengqiYali5) {
        this.zhengqiYali5 = zhengqiYali5;
    }

    public Float getZhengqiYali6() {
        return zhengqiYali6;
    }

    public void setZhengqiYali6(Float zhengqiYali6) {
        this.zhengqiYali6 = zhengqiYali6;
    }

    public Float getZhengqiYali7() {
        return zhengqiYali7;
    }

    public void setZhengqiYali7(Float zhengqiYali7) {
        this.zhengqiYali7 = zhengqiYali7;
    }

    public Float getZhengqiYali8() {
        return zhengqiYali8;
    }

    public void setZhengqiYali8(Float zhengqiYali8) {
        this.zhengqiYali8 = zhengqiYali8;
    }

    public Float getZhengqiYali9() {
        return zhengqiYali9;
    }

    public void setZhengqiYali9(Float zhengqiYali9) {
        this.zhengqiYali9 = zhengqiYali9;
    }

    public Float getZhengqiYali10() {
        return zhengqiYali10;
    }

    public void setZhengqiYali10(Float zhengqiYali10) {
        this.zhengqiYali10 = zhengqiYali10;
    }

    public Float getZhengqiYali11() {
        return zhengqiYali11;
    }

    public void setZhengqiYali11(Float zhengqiYali11) {
        this.zhengqiYali11 = zhengqiYali11;
    }

    public Float getZhengqiYali12() {
        return zhengqiYali12;
    }

    public void setZhengqiYali12(Float zhengqiYali12) {
        this.zhengqiYali12 = zhengqiYali12;
    }


    public Float getGuoniWendu1() {
        return guoniWendu1;
    }

    public void setGuoniWendu1(Float guoniWendu1) {
        this.guoniWendu1 = guoniWendu1;
    }

    public Float getGuoniWendu2() {
        return guoniWendu2;
    }

    public void setGuoniWendu2(Float guoniWendu2) {
        this.guoniWendu2 = guoniWendu2;
    }

    public Float getGuoniWendu3() {
        return guoniWendu3;
    }

    public void setGuoniWendu3(Float guoniWendu3) {
        this.guoniWendu3 = guoniWendu3;
    }

    public Float getGuoniWendu4() {
        return guoniWendu4;
    }

    public void setGuoniWendu4(Float guoniWendu4) {
        this.guoniWendu4 = guoniWendu4;
    }

    public Float getGuoniWendu5() {
        return guoniWendu5;
    }

    public void setGuoniWendu5(Float guoniWendu5) {
        this.guoniWendu5 = guoniWendu5;
    }

    public Float getGuoniWendu6() {
        return guoniWendu6;
    }

    public void setGuoniWendu6(Float guoniWendu6) {
        this.guoniWendu6 = guoniWendu6;
    }

    public Float getGuoniWendu7() {
        return guoniWendu7;
    }

    public void setGuoniWendu7(Float guoniWendu7) {
        this.guoniWendu7 = guoniWendu7;
    }

    public Float getGuoniWendu8() {
        return guoniWendu8;
    }

    public void setGuoniWendu8(Float guoniWendu8) {
        this.guoniWendu8 = guoniWendu8;
    }

    public Float getGuoniWendu9() {
        return guoniWendu9;
    }

    public void setGuoniWendu9(Float guoniWendu9) {
        this.guoniWendu9 = guoniWendu9;
    }

    public Float getGuoniWendu10() {
        return guoniWendu10;
    }

    public void setGuoniWendu10(Float guoniWendu10) {
        this.guoniWendu10 = guoniWendu10;
    }

    public Float getGuoniWendu11() {
        return guoniWendu11;
    }

    public void setGuoniWendu11(Float guoniWendu11) {
        this.guoniWendu11 = guoniWendu11;
    }

    public Float getGuoniWendu12() {
        return guoniWendu12;
    }

    public void setGuoniWendu12(Float guoniWendu12) {
        this.guoniWendu12 = guoniWendu12;
    }


    public Float getZhenkongYali1() {
        return zhenkongYali1;
    }

    public void setZhenkongYali1(Float zhenkongYali1) {
        this.zhenkongYali1 = zhenkongYali1;
    }

    public Float getZhenkongYali2() {
        return zhenkongYali2;
    }

    public void setZhenkongYali2(Float zhenkongYali2) {
        this.zhenkongYali2 = zhenkongYali2;
    }

    public Float getZhenkongYali3() {
        return zhenkongYali3;
    }

    public void setZhenkongYali3(Float zhenkongYali3) {
        this.zhenkongYali3 = zhenkongYali3;
    }

    public Float getZhenkongYali4() {
        return zhenkongYali4;
    }

    public void setZhenkongYali4(Float zhenkongYali4) {
        this.zhenkongYali4 = zhenkongYali4;
    }

    public Float getZhenkongYali5() {
        return zhenkongYali5;
    }

    public void setZhenkongYali5(Float zhenkongYali5) {
        this.zhenkongYali5 = zhenkongYali5;
    }

    public Float getZhenkongYali6() {
        return zhenkongYali6;
    }

    public void setZhenkongYali6(Float zhenkongYali6) {
        this.zhenkongYali6 = zhenkongYali6;
    }

    public Float getZhenkongYali7() {
        return zhenkongYali7;
    }

    public void setZhenkongYali7(Float zhenkongYali7) {
        this.zhenkongYali7 = zhenkongYali7;
    }

    public Float getZhenkongYali8() {
        return zhenkongYali8;
    }

    public void setZhenkongYali8(Float zhenkongYali8) {
        this.zhenkongYali8 = zhenkongYali8;
    }

    public Float getZhenkongYali9() {
        return zhenkongYali9;
    }

    public void setZhenkongYali9(Float zhenkongYali9) {
        this.zhenkongYali9 = zhenkongYali9;
    }

    public Float getZhenkongYali10() {
        return zhenkongYali10;
    }

    public void setZhenkongYali10(Float zhenkongYali10) {
        this.zhenkongYali10 = zhenkongYali10;
    }

    public Float getZhenkongYali11() {
        return zhenkongYali11;
    }

    public void setZhenkongYali11(Float zhenkongYali11) {
        this.zhenkongYali11 = zhenkongYali11;
    }

    public Float getZhenkongYali12() {
        return zhenkongYali12;
    }

    public void setZhenkongYali12(Float zhenkongYali12) {
        this.zhenkongYali12 = zhenkongYali12;
    }


    public Float getDiguoShuiwen1() {
        return diguoShuiwen1;
    }

    public void setDiguoShuiwen1(Float diguoShuiwen1) {
        this.diguoShuiwen1 = diguoShuiwen1;
    }

    public Float getDiguoShuiwen2() {
        return diguoShuiwen2;
    }

    public void setDiguoShuiwen2(Float diguoShuiwen2) {
        this.diguoShuiwen2 = diguoShuiwen2;
    }

    public Float getDiguoShuiwen3() {
        return diguoShuiwen3;
    }

    public void setDiguoShuiwen3(Float diguoShuiwen3) {
        this.diguoShuiwen3 = diguoShuiwen3;
    }

    public Float getDiguoShuiwen4() {
        return diguoShuiwen4;
    }

    public void setDiguoShuiwen4(Float diguoShuiwen4) {
        this.diguoShuiwen4 = diguoShuiwen4;
    }

    public Float getDiguoShuiwen5() {
        return diguoShuiwen5;
    }

    public void setDiguoShuiwen5(Float diguoShuiwen5) {
        this.diguoShuiwen5 = diguoShuiwen5;
    }

    public Float getDiguoShuiwen6() {
        return diguoShuiwen6;
    }

    public void setDiguoShuiwen6(Float diguoShuiwen6) {
        this.diguoShuiwen6 = diguoShuiwen6;
    }

    public Float getDiguoShuiwen7() {
        return diguoShuiwen7;
    }

    public void setDiguoShuiwen7(Float diguoShuiwen7) {
        this.diguoShuiwen7 = diguoShuiwen7;
    }

    public Float getDiguoShuiwen8() {
        return diguoShuiwen8;
    }

    public void setDiguoShuiwen8(Float diguoShuiwen8) {
        this.diguoShuiwen8 = diguoShuiwen8;
    }

    public Float getDiguoShuiwen9() {
        return diguoShuiwen9;
    }

    public void setDiguoShuiwen9(Float diguoShuiwen9) {
        this.diguoShuiwen9 = diguoShuiwen9;
    }

    public Float getDiguoShuiwen10() {
        return diguoShuiwen10;
    }

    public void setDiguoShuiwen10(Float diguoShuiwen10) {
        this.diguoShuiwen10 = diguoShuiwen10;
    }

    public Float getDiguoShuiwen11() {
        return diguoShuiwen11;
    }

    public void setDiguoShuiwen11(Float diguoShuiwen11) {
        this.diguoShuiwen11 = diguoShuiwen11;
    }

    public Float getDiguoShuiwen12() {
        return diguoShuiwen12;
    }

    public void setDiguoShuiwen12(Float diguoShuiwen12) {
        this.diguoShuiwen12 = diguoShuiwen12;
    }

    public Float getChujiuWendu1() {
        return chujiuWendu1;
    }

    public void setChujiuWendu1(Float chujiuWendu1) {
        this.chujiuWendu1 = chujiuWendu1;
    }

    public Float getChujiuWendu2() {
        return chujiuWendu2;
    }

    public void setChujiuWendu2(Float chujiuWendu2) {
        this.chujiuWendu2 = chujiuWendu2;
    }

    public Float getChujiuWendu3() {
        return chujiuWendu3;
    }

    public void setChujiuWendu3(Float chujiuWendu3) {
        this.chujiuWendu3 = chujiuWendu3;
    }

    public Float getChujiuWendu4() {
        return chujiuWendu4;
    }

    public void setChujiuWendu4(Float chujiuWendu4) {
        this.chujiuWendu4 = chujiuWendu4;
    }

    public Float getChujiuWendu5() {
        return chujiuWendu5;
    }

    public void setChujiuWendu5(Float chujiuWendu5) {
        this.chujiuWendu5 = chujiuWendu5;
    }

    public Float getChujiuWendu6() {
        return chujiuWendu6;
    }

    public void setChujiuWendu6(Float chujiuWendu6) {
        this.chujiuWendu6 = chujiuWendu6;
    }

    public Float getChujiuWendu7() {
        return chujiuWendu7;
    }

    public void setChujiuWendu7(Float chujiuWendu7) {
        this.chujiuWendu7 = chujiuWendu7;
    }

    public Float getChujiuWendu8() {
        return chujiuWendu8;
    }

    public void setChujiuWendu8(Float chujiuWendu8) {
        this.chujiuWendu8 = chujiuWendu8;
    }

    public Float getChujiuWendu9() {
        return chujiuWendu9;
    }

    public void setChujiuWendu9(Float chujiuWendu9) {
        this.chujiuWendu9 = chujiuWendu9;
    }

    public Float getChujiuWendu10() {
        return chujiuWendu10;
    }

    public void setChujiuWendu10(Float chujiuWendu10) {
        this.chujiuWendu10 = chujiuWendu10;
    }

    public Float getChujiuWendu11() {
        return chujiuWendu11;
    }

    public void setChujiuWendu11(Float chujiuWendu11) {
        this.chujiuWendu11 = chujiuWendu11;
    }

    public Float getChujiuWendu12() {
        return chujiuWendu12;
    }

    public void setChujiuWendu12(Float chujiuWendu12) {
        this.chujiuWendu12 = chujiuWendu12;
    }


    public Float getHuishuiWendu1() {
        return huishuiWendu1;
    }

    public void setHuishuiWendu1(Float huishuiWendu1) {
        this.huishuiWendu1 = huishuiWendu1;
    }

    public Float getHuishuiWendu2() {
        return huishuiWendu2;
    }

    public void setHuishuiWendu2(Float huishuiWendu2) {
        this.huishuiWendu2 = huishuiWendu2;
    }

    public Float getHuishuiWendu3() {
        return huishuiWendu3;
    }

    public void setHuishuiWendu3(Float huishuiWendu3) {
        this.huishuiWendu3 = huishuiWendu3;
    }

    public Float getHuishuiWendu4() {
        return huishuiWendu4;
    }

    public void setHuishuiWendu4(Float huishuiWendu4) {
        this.huishuiWendu4 = huishuiWendu4;
    }

    public Float getHuishuiWendu5() {
        return huishuiWendu5;
    }

    public void setHuishuiWendu5(Float huishuiWendu5) {
        this.huishuiWendu5 = huishuiWendu5;
    }

    public Float getHuishuiWendu6() {
        return huishuiWendu6;
    }

    public void setHuishuiWendu6(Float huishuiWendu6) {
        this.huishuiWendu6 = huishuiWendu6;
    }

    public Float getHuishuiWendu7() {
        return huishuiWendu7;
    }

    public void setHuishuiWendu7(Float huishuiWendu7) {
        this.huishuiWendu7 = huishuiWendu7;
    }

    public Float getHuishuiWendu8() {
        return huishuiWendu8;
    }

    public void setHuishuiWendu8(Float huishuiWendu8) {
        this.huishuiWendu8 = huishuiWendu8;
    }

    public Float getHuishuiWendu9() {
        return huishuiWendu9;
    }

    public void setHuishuiWendu9(Float huishuiWendu9) {
        this.huishuiWendu9 = huishuiWendu9;
    }

    public Float getHuishuiWendu10() {
        return huishuiWendu10;
    }

    public void setHuishuiWendu10(Float huishuiWendu10) {
        this.huishuiWendu10 = huishuiWendu10;
    }

    public Float getHuishuiWendu11() {
        return huishuiWendu11;
    }

    public void setHuishuiWendu11(Float huishuiWendu11) {
        this.huishuiWendu11 = huishuiWendu11;
    }

    public Float getHuishuiWendu12() {
        return huishuiWendu12;
    }

    public void setHuishuiWendu12(Float huishuiWendu12) {
        this.huishuiWendu12 = huishuiWendu12;
    }

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

    public Float getDiuzaoWeijiuguanYewei() {
        return diuzaoWeijiuguanYewei;
    }

    public void setDiuZaoWeijiuguanYewei(Float diuzaoWeijiuguanYewei) {this.diuzaoWeijiuguanYewei = diuzaoWeijiuguanYewei;}










}
