package com.mom.winery.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Comment;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.Date;

@JmixEntity
@Table(name = "MES_ZENG_SUM_DATA", indexes = {
        @Index(name = "IDX_MES_ZENG_SUM_DATA_EMPTY_FULL", columnList = "EMPTY_FULL_ID")
})
@Entity
public class MesZengSumData {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;


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

    // 料斗编码/编号
    @Comment("料斗编码/编号")
    @Column(name = "LIAODOU_NO")
    private Integer liaodouNo;

    //该斗当前所在位置
    @Comment("该斗当前所在位置")
    @Column(name = "LIAODOU_LOCATION")
    private Integer liaodouLocation;

    //"该料斗是否被摊粮机占用（摊晾机正在给他喂料）（可以忽略，不关注）"
    @Comment("该料斗是否被摊粮机占用（摊晾机正在给他喂料）")
    @Column(name = "LIAODOU_IS_OCCUPIED")
    private Boolean liaodouIsOccupied;

    //最近一次CIP清洗的时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("最近一次CIP清洗的时间")
    @Column(name = "LAST_CIP_TIME")
    private Date lastCipTime;

    //该料斗上次CIP时间至今是否已经超过了工艺要求
    @Comment("该料斗上次CIP时间至今是否已经超过了工艺要求")
    @Column(name = "IS_CIP_OVERDUE")
    private Boolean isCipOverdue;

    //空斗/满斗
    @JoinColumn(name = "EMPTY_FULL_ID")
    @ManyToOne
    @Comment("空斗/满斗")
    private MesWinccItemConfig emptyFull;

    //出窖糟/回窖糟
    @JoinColumn(name = "CHUJIAO_OR_HUIJIAO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("出窖糟/回窖糟")
    private MesWinccItemConfig chujiaoOrHuijiao;

    //出窖：出层数
    @Comment("出窖：出层数")
    @Column(name = "CHUJIAO_LAYER")
    private Integer chujiaoLayer;

    //出窖：窖池编号
    @Comment("出窖：窖池编号")
    @Column(name = "CHUJIAO_JIAOCHI_NO")
    private Integer chujiaoJiaochiNo;

    //出窖：出窖时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("出窖：出窖时间")
    @Column(name = "CHUJIAO_TIME")
    private Date chujiaoTime;

    @Comment("出窖：糟醅类型")
    @Column(name = "CHUJIAO_ZAOPEI_TYPE")
    private Integer chujiaoZaopeiType;

    //下半甑：糟源头—窖池号
    @Comment("下半甑：糟源头—窖池号")
    @Column(name = "DOWN_JIAOCHI_NO")
    private Integer downJiaochiNo;

    //下半甑：糟源头—出窖时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("下半甑：糟源头—出窖时间")
    @Column(name = "DOWN_CHUJIAO_TIME")
    private Date downChujiaoTime;

    //下半甑：糟源头—出窖层数
    @Comment("下半甑：糟源头—出窖层数")
    @Column(name = "DOWN_CHUJIAO_LAYER")
    private Integer downChujiaoLayer;

    //下半甑：糟源头—糟醅类型
    @Comment("下半甑：糟源头—糟醅类型")
    @Column(name = "DOWN_ZAOPEI_TYPE")
    private Integer downZaopeiType;

    //下半甑：润粮开始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("下半甑：润粮开始时间")
    @Column(name = "DOWN_RUNLIANG_START_TIME")
    private Date downRunliangStartTime;

    //下半甑：润粮结束时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("下半甑：润粮结束时间")
    @Column(name = "DOWN_RUNLIANG_END_TIME")
    private Date downRunliangEndTime;

    //下半甑：润粮时长
    @Comment("下半甑：润粮时长")
    @Column(name = "DOWN_RUNLIANG_DURATION")
    private Float downRunliangDuration;

    //下半甑：润粮时间是否达到工艺设定
    @Comment("下半甑：润粮时间是否达到工艺设定")
    @Column(name = "DOWN_IS_DURATION_OK")
    private Boolean downIsDurationOk;

    //下半甑：润粮加水量
    @Comment("下半甑：润粮加水量")
    @Column(name = "DOWN_RUNLIANG_WATER")
    private Float downRunliangWater;

    //下半甑：糟醅量
    @Comment("下半甑：糟醅量")
    @Column(name = "DOWN_ZAOPEI_QTY")
    private Float downZaopeiQty;

    //下半甑：加稻壳量
    @Comment("下半甑：加稻壳量")
    @Column(name = "DOWN_DAOKE")
    private Float downDaoKe;

    //下半甑：加粮量
    @Comment("下半甑：加粮量")
    @Column(name = "DOWN_LIANGSHI")
    private Float downLiangShi;

    //下半甑：加粮类型
    @Comment("下半甑：加粮类型")
    @Column(name = "DOWN_LIANG_TYPE")
    private Integer downLiangType;

    //上半甑：糟源头—窖池号
    @Comment("上半甑：糟源头—窖池号")
    @Column(name = "UP_JIAOCHI_NO")
    private Integer upJiaochiNo;

    //上半甑：糟源头—出窖时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("上半甑：糟源头—出窖时间")
    @Column(name = "UP_CHUJIAO_TIME")
    private Date upChujiaoTime;

    //上半甑：糟源头—出窖层数
    @Comment("上半甑：糟源头—出窖层数")
    @Column(name = "UP_CHUJIAO_LAYER")
    private Integer upChujiaoLayer;

    //上半甑：糟源头—糟醅类型
    @Comment("上半甑：糟源头—糟醅类型")
    @Column(name = "UP_ZAOPEI_TYPE")
    private Integer upZaopeiType;

    //上半甑：润粮开始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("上半甑：润粮开始时间")
    @Column(name = "UP_RUNLIANG_START_TIME")
    private Date upRunliangStartTime;

    //上半甑：润粮结束时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("上半甑：润粮结束时间")
    @Column(name = "UP_RUNLIANG_END_TIME")
    private Date upRunliangEndTime;

    //上半甑：润粮时长
    @Comment("上半甑：润粮时长")
    @Column(name = "UP_RUNLIANG_DURATION")
    private Float upRunliangDuration;

    //上半甑：润粮时间是否达到工艺设定
    @Comment("上半甑：润粮时间是否达到工艺设定")
    @Column(name = "UP_IS_DURATION_OK")
    private Boolean upIsDurationOk;

    //上半甑：润粮加水量
    @Comment("上半甑：润粮加水量")
    @Column(name = "UP_RUNLIANG_WATER")
    private Float upRunliangWater;

    //上半甑：糟醅量
    @Comment("上半甑：糟醅量")
    @Column(name = "UP_ZAOPEI_QTY")
    private Float upZaopeiQty;

    //上半甑：加稻壳量
    @Comment("上半甑：加稻壳量")
    @Column(name = "UP_DAOKE")
    private Float upDaoKe;

    //上半甑：加粮量
    @Comment("上半甑：加粮量")
    @Column(name = "UP_LIANGSHI")
    private Float upLiangShi;

    //上半甑：加粮类型
    @Comment("上半甑：加粮类型")
    @Column(name = "UP_LIANG_TYPE")
    private Integer upLiangType;

    // 甑锅任务开始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("甑锅任务开始时间")
    @Column(name = "ZENG_START_TIME")
    private Date zengStartTime;

    //机器人上甑开始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("机器人上甑开始时间")
    @Column(name = "ROBOT_UP_START_TIME")
    private Date robotUpStartTime;

    //卡盘馏酒开始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("卡盘馏酒开始时间")
    @Column(name = "KAPAN_START_TIME")
    private Date kapanStartTime;

    //馏酒结束时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("馏酒结束时间")
    @Column(name = "LIUJIU_END_TIME")
    private Date liujiuEndTime;

    //甑任务结束时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("甑任务结束时间")
    @Column(name = "ZENG_END_TIME")
    private Date zengEndTime;

    //甑序
    @Comment("甑序")
    @Column(name = "ZENG_NO")
    private Integer zengNo;

    //料源：来自?#甑桶
    @JoinColumn(name = "RESOURCE_ZENGGUO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("料源：来自?#甑桶")
    private MesZengguo resourceZengguo;

    @Comment("上甑糟醅类型")
    @Column(name = "ZENG_ZAOPEI_TYPE")
    private Integer zengZaopeiType;

    //上甑时长
    @Comment("上甑时长")
    @Column(name = "SHANGZENG_DURATION")
    private Float shangzengDuration;

    //上甑层数
    @Comment("上甑层数")
    @Column(name = "SHANGZENG_LAYER")
    private Integer shangzengLayer;

    //上甑高度
    @Comment("上甑高度")
    @Column(name = "SHANGZENG_HEIGHT")
    private Float shangzengHeight;

    //上甑累计重量
    @Comment("上甑累计重量")
    @Column(name = "SHANGZENG_WEIGHT")
    private Float shangzengWeight;

    //添加量_黄水
    @Comment("添加量_黄水")
    @Column(name = "ADD_HUANGSHUI")
    private Float addHuangshui;

    //添加量_酒尾
    @Comment("添加量_酒尾")
    @Column(name = "ADD_JIUWEI")
    private Float addJiuwei;

    //添加量_回收底锅水
    @Comment("添加量_回收底锅水")
    @Column(name = "ADD_HUISHOU")
    private Float addHuishou;

    //添加量_热水
    @Comment("添加量_热水")
    @Column(name = "ADD_RESHUI")
    private Float addReshui;

    //接酒时长_1级
    @Comment("接酒时长_1级")
    @Column(name = "JIEJIU_FIRSTCLASS_DURATION")
    private Float jiejiuFirstClassDuration;

    //接酒时长_2级
    @Comment("接酒时长_2级")
    @Column(name = "JIEJIU_SECONDCLASS_DURATION")
    private Float jiejiuSecondClassDuration;

    //接酒时长_3级
    @Comment("接酒时长_3级")
    @Column(name = "JIEJIU_THIRDCLASS_DURATION")
    private Float jiejiuThirdClassDuration;

    //接酒时长_酒尾
    @Comment("接酒时长_酒尾")
    @Column(name = "JIEJIU_JIUWEI_DURATION")
    private Float jiejiuJiuweiDuration;

    //接酒时长_废水
    @Comment("接酒时长_废水")
    @Column(name = "JIEJIU_FEISHUI_DURATION")
    private Float jiejiuFeishuiDuration;

    //晾水添加量
    @Comment("晾水添加量")
    @Column(name = "LIANGSHUI_ADD")
    private Float liangshuiAdd;

    //耗汽量_上甑
    @Comment("耗汽量_上甑")
    @Column(name = "HAOQI_SHANGZENG")
    private Float haoqiShangzeng;

    //耗汽量_蒸馏
    @Comment("耗汽量_蒸馏")
    @Column(name = "HAOQI_ZHENGLIU")
    private Float haoqiZhengliu;

    //蒸馏时长
    @Comment("蒸馏时长")
    @Column(name = "ZHENGLIU_DURATION")
    private Float zhengliuDuration;

    //馏酒+蒸煮时长（需计算，非直接给）
    @Comment("馏酒+蒸煮时长")
    @Column(name = "LIUJIU_ZHENZHU_DURATION")
    private Float liujiuZhenzhuDuration;

    // 回窖糟：摊晾开始时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("回窖糟：摊晾开始时间")
    @Column(name = "TANLIANG_START_TIME")
    private Date tanliangStartTime;

    //回窖糟：摊晾结束时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("回窖糟：摊晾结束时间")
    @Column(name = "TANLIANG_END_TIME")
    private Date tanliangEndTime;

    //回窖糟：摊晾时长
    @Comment("回窖糟：摊晾时长")
    @Column(name = "TANLIANG_DURATION")
    private Float tanliangDuration;

    //回窖糟：该糟醅源头—出窖层数
    @Comment("回窖糟：该糟醅源头—出窖层数")
    @Column(name = "TANLIANG_OUT_LAYER")
    private Integer tanliangOutLayer;

    //回窖糟：最终入窖窖池编号
    @Comment("回窖糟：最终入窖窖池编号")
    @Column(name = "TANLIANG_POOL_IN_NO")
    private Integer tanliangPoolInNo;

    //回窖糟：该糟醅源头—出窖时间
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("回窖糟：该糟醅源头—出窖时间")
    @Column(name = "TANLIANG_CHUJIAO_TIME")
    private Date tanliangChujiaoTime;

    //回窖糟：该糟醅源头—糟醅类型
    @Comment("回窖糟：该糟醅源头—糟醅类型")
    @Column(name = "TANLIANG_ZAOPEI_TYPE")
    private Integer tanliangZaopeiType;

    //回窖糟：糟醅重量
    @Comment("回窖糟：糟醅重量")
    @Column(name = "TANLIANG_ZAOPEI_WEIGHT")
    private Float tanliangZaopeiWeight;

    //回窖糟：加曲重量
    @Comment("回窖糟：加曲重量")
    @Column(name = "TANLIANG_JIAQU_WEIGHT")
    private Float tanliangJiaquWeight;

    //回窖糟：摊晾期间出口最大温度
    @Comment("回窖糟：摊晾期间出口最大温度")
    @Column(name = "TANLIANG_MAX_TEMP")
    private Float tanliangMaxTemp;

    //回窖糟：摊晾期间出口最小温度
    @Comment("回窖糟：摊晾期间出口最小温度")
    @Column(name = "TANLIANG_MIN_TEMP")
    private Float tanliangMinTemp;

    //回窖糟：摊晾期间出口平均温度
    @Comment("回窖糟：摊晾期间出口平均温度")
    @Column(name = "TANLIANG_AVG_TEMP")
    private Float tanliangAvgTemp;

    @Comment("出窖：糟醅类型")
    @Column(name = "ZAOPEI_TYPE")
    private Integer zaopeiType;

    public EnumZaopeiType getChujiaoZaopeiType() {
        return chujiaoZaopeiType == null ? null : EnumZaopeiType.fromId(chujiaoZaopeiType);
    }

    public void setChujiaoZaopeiType(EnumZaopeiType chujiaoZaopeiType) {
        this.chujiaoZaopeiType = chujiaoZaopeiType == null ? null : chujiaoZaopeiType.getId();
    }

    public Integer getZaopeiType() {
        return zaopeiType;
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


    public Integer getLiaodouNo() {
        return liaodouNo;
    }

    public void setLiaodouNo(Integer liaodouNo) {
        this.liaodouNo = liaodouNo;
    }

    public Integer getLiaodouLocation() {
        return liaodouLocation;
    }

    public void setLiaodouLocation(Integer liaodouLocation) {
        this.liaodouLocation = liaodouLocation;
    }

    public Boolean getLiaodouIsOccupied() {
        return liaodouIsOccupied;
    }

    public void setLiaodouIsOccupied(Boolean liaodouIsOccupied) {
        this.liaodouIsOccupied = liaodouIsOccupied;
    }

    public Date getLastCipTime() {
        return lastCipTime;
    }

    public void setLastCipTime(Date lastCipTime) {
        this.lastCipTime = lastCipTime;
    }

    public Boolean getIsCipOverdue() {
        return isCipOverdue;
    }

    public void setIsCipOverdue(Boolean isCipOverdue) {
        this.isCipOverdue = isCipOverdue;
    }

    public Integer getChujiaoLayer() {
        return chujiaoLayer;
    }

    public void setChujiaoLayer(Integer chujiaoLayer) {
        this.chujiaoLayer = chujiaoLayer;
    }

    public Integer getChujiaoJiaochiNo() {
        return chujiaoJiaochiNo;
    }

    public void setChujiaoJiaochiNo(Integer chujiaoJiaochiNo) {
        this.chujiaoJiaochiNo = chujiaoJiaochiNo;
    }

    public Date getChujiaoTime() {
        return chujiaoTime;
    }

    public void setChujiaoTime(Date chujiaoTime) {
        this.chujiaoTime = chujiaoTime;
    }


    public EnumZaopeiType getZengZaopeiType() {
        return zengZaopeiType == null ? null : EnumZaopeiType.fromId(zengZaopeiType);
    }

    public void setZengZaopeiType(EnumZaopeiType zengZaopeiType) {
        this.zengZaopeiType = zengZaopeiType == null ? null : zengZaopeiType.getId();
    }



    public Integer getDownJiaochiNo() {
        return downJiaochiNo;
    }

    public void setDownJiaochiNo(Integer downJiaochiNo) {
        this.downJiaochiNo = downJiaochiNo;
    }

    public Date getDownChujiaoTime() {
        return downChujiaoTime;
    }

    public void setDownChujiaoTime(Date downChujiaoTime) {
        this.downChujiaoTime = downChujiaoTime;
    }

    public Integer getDownChujiaoLayer() {
        return downChujiaoLayer;
    }

    public void setDownChujiaoLayer(Integer downChujiaoLayer) {
        this.downChujiaoLayer = downChujiaoLayer;
    }

    public Date getDownRunliangStartTime() {
        return downRunliangStartTime;
    }

    public void setDownRunliangStartTime(Date downRunliangStartTime) {
        this.downRunliangStartTime = downRunliangStartTime;
    }

    public Date getDownRunliangEndTime() {
        return downRunliangEndTime;
    }

    public void setDownRunliangEndTime(Date downRunliangEndTime) {
        this.downRunliangEndTime = downRunliangEndTime;
    }

    public Float getDownRunliangDuration() {
        return downRunliangDuration;
    }

    public void setDownRunliangDuration(Float downRunliangDuration) {
        this.downRunliangDuration = downRunliangDuration;
    }

    public Boolean getDownIsDurationOk() {
        return downIsDurationOk;
    }

    public void setDownIsDurationOk(Boolean downIsDurationOk) {
        this.downIsDurationOk = downIsDurationOk;
    }

    public Float getDownRunliangWater() {
        return downRunliangWater;
    }

    public void setDownRunliangWater(Float downRunliangWater) {
        this.downRunliangWater = downRunliangWater;
    }

    public Float getDownZaopeiQty() {
        return downZaopeiQty;
    }

    public void setDownZaopeiQty(Float downZaopeiQty) {
        this.downZaopeiQty = downZaopeiQty;
    }

    public Float getDownDaoKe() {
        return downDaoKe;
    }

    public void setDownDaoKe(Float downDaoKe) {
        this.downDaoKe = downDaoKe;
    }

    public Float getDownLiangShi() {
        return downLiangShi;
    }

    public void setDownLiangShi(Float downLiangShi) {
        this.downLiangShi = downLiangShi;
    }

    public Integer getUpJiaochiNo() {
        return upJiaochiNo;
    }

    public void setUpJiaochiNo(Integer upJiaochiNo) {
        this.upJiaochiNo = upJiaochiNo;
    }

    public Date getUpChujiaoTime() {
        return upChujiaoTime;
    }

    public void setUpChujiaoTime(Date upChujiaoTime) {
        this.upChujiaoTime = upChujiaoTime;
    }

    public Integer getUpChujiaoLayer() {
        return upChujiaoLayer;
    }

    public void setUpChujiaoLayer(Integer upChujiaoLayer) {
        this.upChujiaoLayer = upChujiaoLayer;
    }

    public Date getUpRunliangStartTime() {
        return upRunliangStartTime;
    }

    public void setUpRunliangStartTime(Date upRunliangStartTime) {
        this.upRunliangStartTime = upRunliangStartTime;
    }

    public Date getUpRunliangEndTime() {
        return upRunliangEndTime;
    }

    public void setUpRunliangEndTime(Date upRunliangEndTime) {
        this.upRunliangEndTime = upRunliangEndTime;
    }

    public Float getUpRunliangDuration() {
        return upRunliangDuration;
    }

    public void setUpRunliangDuration(Float upRunliangDuration) {
        this.upRunliangDuration = upRunliangDuration;
    }

    public Boolean getUpIsDurationOk() {
        return upIsDurationOk;
    }

    public void setUpIsDurationOk(Boolean upIsDurationOk) {
        this.upIsDurationOk = upIsDurationOk;
    }

    public Float getUpRunliangWater() {
        return upRunliangWater;
    }

    public void setUpRunliangWater(Float upRunliangWater) {
        this.upRunliangWater = upRunliangWater;
    }

    public Float getUpZaopeiQty() {
        return upZaopeiQty;
    }

    public void setUpZaopeiQty(Float upZaopeiQty) {
        this.upZaopeiQty = upZaopeiQty;
    }

    public Float getUpDaoKe() {
        return upDaoKe;
    }

    public void setUpDaoKe(Float upDaoKe) {
        this.upDaoKe = upDaoKe;
    }

    public Float getUpLiangShi() {
        return upLiangShi;
    }

    public void setUpLiangShi(Float upLiangShi) {
        this.upLiangShi = upLiangShi;
    }

    public Date getZengStartTime() {
        return zengStartTime;
    }

    public void setZengStartTime(Date zengStartTime) {
        this.zengStartTime = zengStartTime;
    }

    public Date getRobotUpStartTime() {
        return robotUpStartTime;
    }

    public void setRobotUpStartTime(Date robotUpStartTime) {
        this.robotUpStartTime = robotUpStartTime;
    }

    public Date getKapanStartTime() {
        return kapanStartTime;
    }

    public void setKapanStartTime(Date kapanStartTime) {
        this.kapanStartTime = kapanStartTime;
    }

    public Date getLiujiuEndTime() {
        return liujiuEndTime;
    }

    public void setLiujiuEndTime(Date liujiuEndTime) {
        this.liujiuEndTime = liujiuEndTime;
    }

    public Date getZengEndTime() {
        return zengEndTime;
    }

    public void setZengEndTime(Date zengEndTime) {
        this.zengEndTime = zengEndTime;
    }

    public Integer getZengNo() {
        return zengNo;
    }

    public void setZengNo(Integer zengNo) {
        this.zengNo = zengNo;
    }



    public void setTanliangZaopeiType(EnumZaopeiType tanliangZaopeiType) {
        this.tanliangZaopeiType = tanliangZaopeiType == null ? null : tanliangZaopeiType.getId();
    }

    public EnumZaopeiType getTanliangZaopeiType() {
        return tanliangZaopeiType == null ? null : EnumZaopeiType.fromId(tanliangZaopeiType);
    }

    public Float getShangzengDuration() {
        return shangzengDuration;
    }

    public void setShangzengDuration(Float shangzengDuration) {
        this.shangzengDuration = shangzengDuration;
    }

    public Integer getShangzengLayer() {
        return shangzengLayer;
    }

    public void setShangzengLayer(Integer shangzengLayer) {
        this.shangzengLayer = shangzengLayer;
    }

    public Float getShangzengHeight() {
        return shangzengHeight;
    }

    public void setShangzengHeight(Float shangzengHeight) {
        this.shangzengHeight = shangzengHeight;
    }

    public Float getShangzengWeight() {
        return shangzengWeight;
    }

    public void setShangzengWeight(Float shangzengWeight) {
        this.shangzengWeight = shangzengWeight;
    }

    public Float getAddHuangshui() {
        return addHuangshui;
    }

    public void setAddHuangshui(Float addHuangshui) {
        this.addHuangshui = addHuangshui;
    }

    public Float getAddJiuwei() {
        return addJiuwei;
    }

    public void setAddJiuwei(Float addJiuwei) {
        this.addJiuwei = addJiuwei;
    }

    public Float getAddHuishou() {
        return addHuishou;
    }

    public void setAddHuishou(Float addHuishou) {
        this.addHuishou = addHuishou;
    }

    public Float getAddReshui() {
        return addReshui;
    }

    public void setAddReshui(Float addReshui) {
        this.addReshui = addReshui;
    }

    public Float getJiejiuFirstClassDuration() {
        return jiejiuFirstClassDuration;
    }

    public void setJiejiuFirstClassDuration(Float jiejiuFirstClassDuration) {
        this.jiejiuFirstClassDuration = jiejiuFirstClassDuration;
    }

    public Float getJiejiuSecondClassDuration() {
        return jiejiuSecondClassDuration;
    }

    public void setJiejiuSecondClassDuration(Float jiejiuSecondClassDuration) {
        this.jiejiuSecondClassDuration = jiejiuSecondClassDuration;
    }

    public Float getJiejiuThirdClassDuration() {
        return jiejiuThirdClassDuration;
    }

    public void setJiejiuThirdClassDuration(Float jiejiuThirdClassDuration) {
        this.jiejiuThirdClassDuration = jiejiuThirdClassDuration;
    }

    public Float getJiejiuJiuweiDuration() {
        return jiejiuJiuweiDuration;
    }

    public void setJiejiuJiuweiDuration(Float jiejiuJiuweiDuration) {
        this.jiejiuJiuweiDuration = jiejiuJiuweiDuration;
    }

    public Float getJiejiuFeishuiDuration() {
        return jiejiuFeishuiDuration;
    }

    public void setJiejiuFeishuiDuration(Float jiejiuFeishuiDuration) {
        this.jiejiuFeishuiDuration = jiejiuFeishuiDuration;
    }

    public Float getLiangshuiAdd() {
        return liangshuiAdd;
    }

    public void setLiangshuiAdd(Float liangshuiAdd) {
        this.liangshuiAdd = liangshuiAdd;
    }

    public Float getHaoqiShangzeng() {
        return haoqiShangzeng;
    }

    public void setHaoqiShangzeng(Float haoqiShangzeng) {
        this.haoqiShangzeng = haoqiShangzeng;
    }

    public Float getHaoqiZhengliu() {
        return haoqiZhengliu;
    }

    public void setHaoqiZhengliu(Float haoqiZhengliu) {
        this.haoqiZhengliu = haoqiZhengliu;
    }

    public Float getZhengliuDuration() {
        return zhengliuDuration;
    }

    public void setZhengliuDuration(Float zhengliuDuration) {
        this.zhengliuDuration = zhengliuDuration;
    }

    public Float getLiujiuZhenzhuDuration() {
        return liujiuZhenzhuDuration;
    }

    public void setLiujiuZhenzhuDuration(Float liujiuZhenzhuDuration) {
        this.liujiuZhenzhuDuration = liujiuZhenzhuDuration;
    }

    public Date getTanliangStartTime() {
        return tanliangStartTime;
    }

    public void setTanliangStartTime(Date tanliangStartTime) {
        this.tanliangStartTime = tanliangStartTime;
    }

    public Date getTanliangEndTime() {
        return tanliangEndTime;
    }

    public void setTanliangEndTime(Date tanliangEndTime) {
        this.tanliangEndTime = tanliangEndTime;
    }

    public Float getTanliangDuration() {
        return tanliangDuration;
    }

    public void setTanliangDuration(Float tanliangDuration) {
        this.tanliangDuration = tanliangDuration;
    }

    public Integer getTanliangOutLayer() {
        return tanliangOutLayer;
    }

    public void setTanliangOutLayer(Integer tanliangOutLayer) {
        this.tanliangOutLayer = tanliangOutLayer;
    }

    public Integer getTanliangPoolInNo() {
        return tanliangPoolInNo;
    }

    public void setTanliangPoolInNo(Integer tanliangPoolInNo) {
        this.tanliangPoolInNo = tanliangPoolInNo;
    }

    public Date getTanliangChujiaoTime() {
        return tanliangChujiaoTime;
    }

    public void setTanliangChujiaoTime(Date tanliangChujiaoTime) {
        this.tanliangChujiaoTime = tanliangChujiaoTime;
    }

    public Float getTanliangZaopeiWeight() {
        return tanliangZaopeiWeight;
    }

    public void setTanliangZaopeiWeight(Float tanliangZaopeiWeight) {
        this.tanliangZaopeiWeight = tanliangZaopeiWeight;
    }

    public Float getTanliangJiaquWeight() {
        return tanliangJiaquWeight;
    }

    public void setTanliangJiaquWeight(Float tanliangJiaquWeight) {
        this.tanliangJiaquWeight = tanliangJiaquWeight;
    }

    public Float getTanliangMaxTemp() {
        return tanliangMaxTemp;
    }

    public void setTanliangMaxTemp(Float tanliangMaxTemp) {
        this.tanliangMaxTemp = tanliangMaxTemp;
    }

    public Float getTanliangMinTemp() {
        return tanliangMinTemp;
    }

    public void setTanliangMinTemp(Float tanliangMinTemp) {
        this.tanliangMinTemp = tanliangMinTemp;
    }

    //    public void setZaopeiType(Integer zaopeiType) {
//        this.zaopeiType = zaopeiType;
//    }

//    public Integer getZaopeiType() {
//        return zaopeiType;
//    }

    public void setResourceZengguo(MesZengguo resourceZengguo) {
        this.resourceZengguo = resourceZengguo;
    }

    public MesZengguo getResourceZengguo() {
        return resourceZengguo;
    }

    public void setUpLiangType(EnumLiangshiType upLiangType) {
        this.upLiangType = upLiangType == null ? null : upLiangType.getId();
    }

    public EnumLiangshiType getUpLiangType() {
        return upLiangType == null ? null : EnumLiangshiType.fromId(upLiangType);
    }

    public void setDownLiangType(EnumLiangshiType downLiangType) {
        this.downLiangType = downLiangType == null ? null : downLiangType.getId();
    }

    public EnumLiangshiType getDownLiangType() {
        return downLiangType == null ? null : EnumLiangshiType.fromId(downLiangType);
    }

    public void setUpZaopeiType(EnumZaopeiType upZaopeiType) {
        this.upZaopeiType = upZaopeiType == null ? null : upZaopeiType.getId();
    }

    public EnumZaopeiType getUpZaopeiType() {
        return upZaopeiType == null ? null : EnumZaopeiType.fromId(upZaopeiType);
    }

    public void setDownZaopeiType(EnumZaopeiType downZaopeiType) {
        this.downZaopeiType = downZaopeiType == null ? null : downZaopeiType.getId();
    }

    public EnumZaopeiType getDownZaopeiType() {
        return downZaopeiType == null ? null : EnumZaopeiType.fromId(downZaopeiType);
    }

    public void setChujiaoOrHuijiao(MesWinccItemConfig chujiaoOrHuijiao) {
        this.chujiaoOrHuijiao = chujiaoOrHuijiao;
    }

    public MesWinccItemConfig getChujiaoOrHuijiao() {
        return chujiaoOrHuijiao;
    }

    public void setEmptyFull(MesWinccItemConfig emptyFull) {
        this.emptyFull = emptyFull;
    }

    public MesWinccItemConfig getEmptyFull() {
        return emptyFull;
    }

    public Float getTanliangAvgTemp() {
        return tanliangAvgTemp;
    }
    public void setTanliangAvgTemp(Float tanliangAvgTemp) {
        this.tanliangAvgTemp = tanliangAvgTemp;
    }
}
