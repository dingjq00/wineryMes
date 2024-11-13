package com.mom.winery.quartzjob;

import com.mom.winery.app.WinccDataDealCommons;
import com.mom.winery.entity.*;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.core.security.Authenticated;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/11/13 12:16
 */
@DisallowConcurrentExecution
public class SumDataJob implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Autowired
    private JobSaveDataService jobSaveDataService;

    @Authenticated
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.ZENGDATA)
                .list();
        List<MesTanliangji> mesTanliangjiList = dataManager.load(MesTanliangji.class)
                .query("select e from MesTanliangji e")
                .list();

        List<MesWinccItemConfig> mesWinccItemConfigList = dataManager.load(MesWinccItemConfig.class)
                .query("select e from MesWinccItemConfig e ")
                .list();

        List<MesJiaochi> mesJiaochiList = dataManager.load(MesJiaochi.class)
                .query("select e from MesJiaochi e ")
                .list();

        List<MesZengguo> mesZengguoList = dataManager.load(MesZengguo.class)
                .query("select e from MesZengguo e")
                .list();

        for (JobConfig jobConfig : jobConfigList) {
            MesArea mesArea = jobConfig.getMesArea();
            Integer areaNo = mesArea.getAreaCode();
            List<WinccZengdata> winccZengdataList = dataManager.load(WinccZengdata.class)
                    .query("select e from WinccZengdata e " +
                            "where e.areaNo = :areaNo " +
                            "and e.isDealed is null " +
                            "order by e.starttime asc")
                    .parameter("areaNo", areaNo)
                    .list();
            List<MesZengSumData> mesZengSumDataList = new ArrayList<>();
            for (WinccZengdata winccZengdata : winccZengdataList) {
                String comment = winccZengdata.getComment();
                // 如果comment 为null 或为空，不处理
                if (comment == null || comment.isEmpty()) {
                    continue;
                }
                // Split the comment string by commas
                String[] commentParts = comment.split(",");

                MesZengSumData mesZengSumData = dataManager.create(MesZengSumData.class);

                // 料斗编码/编号	0
                mesZengSumData.setLiaodouNo(Integer.parseInt(commentParts[0]));
                //该斗当前所在位置	1
                mesZengSumData.setLiaodouLocation(Integer.parseInt(commentParts[1]));
                //"该料斗是否被摊粮机占用（摊晾机正在给他喂料）2
                mesZengSumData.setLiaodouIsOccupied(Boolean.parseBoolean(commentParts[2]));
                //最近一次CIP清洗的时间	3
                mesZengSumData.setLastCipTime(winccDataDealCommons.convertStringToDate(commentParts[3]));
                //该料斗上次CIP时间至今是否已经超过了工艺要求	4
                mesZengSumData.setIsCipOverdue(Boolean.parseBoolean(commentParts[4]));
                //空斗/满斗	5
                boolean isFull = Boolean.parseBoolean(commentParts[5]);
                Integer emptyFull = isFull ? 1 : 0;
                MesWinccItemConfig mesWinccItemConfig = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList,EnumMesWinccItemConfig.EMPTY_FULL, emptyFull);
                mesZengSumData.setEmptyFull(mesWinccItemConfig);
                //出窖糟/回窖糟	6
                boolean isChujiao = Boolean.parseBoolean(commentParts[6]);
                Integer chujiaoHuijiao = isChujiao ? 1 : 0;
                mesWinccItemConfig = winccDataDealCommons.getMesWinccItemConfig(mesWinccItemConfigList,EnumMesWinccItemConfig.CHUJIAO_HUIJIAO,chujiaoHuijiao);
                mesZengSumData.setChujiaoOrHuijiao(mesWinccItemConfig);
                // 出窖：出层数（可以忽略，不关注）	7
                mesZengSumData.setChujiaoLayer(Integer.parseInt(commentParts[7]));
                //出窖：窖池编号（可以忽略，不关注）	8
                mesZengSumData.setChujiaoJiaochiNo(Integer.parseInt(commentParts[8]));
                //出窖：出窖时间（可以忽略，不关注）	9
                mesZengSumData.setChujiaoTime(winccDataDealCommons.convertStringToDate(commentParts[9]));
                //出窖：糟醅类型（可以忽略，不关注）	10
                mesZengSumData.setChujiaoZaopeiType(EnumZaopeiType.fromId(Integer.parseInt(commentParts[10])));
                //粗粮（下半甑）：糟源头—窖池号	11
                mesZengSumData.setDownJiaochiNo(Integer.parseInt(commentParts[11]));
                //粗粮（下半甑）：糟源头—出窖时间	12
                mesZengSumData.setDownChujiaoTime(winccDataDealCommons.convertStringToDate(commentParts[12]));
                //粗粮（下半甑）：糟源头—出窖层数	13
                mesZengSumData.setDownChujiaoLayer(Integer.parseInt(commentParts[13]));
                //粗粮（下半甑）：糟源头—糟醅类型	14
                mesZengSumData.setDownZaopeiType(EnumZaopeiType.fromId(Integer.parseInt(commentParts[14])));
                //粗粮（下半甑）：润粮开始时间	15
                mesZengSumData.setDownRunliangStartTime(winccDataDealCommons.convertStringToDate(commentParts[15]));
                //粗粮（下半甑）：润粮结束时间	16
                mesZengSumData.setDownRunliangEndTime(winccDataDealCommons.convertStringToDate(commentParts[16]));
                //粗粮（下半甑）：润粮时长	17
                mesZengSumData.setDownRunliangDuration(Float.parseFloat(commentParts[17]));
                //粗粮（下半甑）：润粮时间是否达到工艺设定	18
                mesZengSumData.setDownIsDurationOk(Boolean.parseBoolean(commentParts[18]));
                //粗粮（下半甑）：润粮加水量	19
                mesZengSumData.setDownRunliangWater(Float.parseFloat(commentParts[19]));
                //粗粮（下半甑）：糟醅量	20
                mesZengSumData.setDownZaopeiQty(Float.parseFloat(commentParts[20]));
                //粗粮（下半甑）：加稻壳量	21
                mesZengSumData.setDownDaoKe(Float.parseFloat(commentParts[21]));
                //粗粮（下半甑）：加粮量	22
                mesZengSumData.setDownLiangShi(Float.parseFloat(commentParts[22]));
                //粗粮（下半甑）：加粮类型	23
                boolean isDownFull = Boolean.parseBoolean(commentParts[23]);
                Integer downFull = isDownFull ? 1 : 0;
                mesZengSumData.setDownLiangType(EnumLiangshiType.fromId(downFull));
                //细粮：糟源头—窖池号	24
                mesZengSumData.setUpJiaochiNo(Integer.parseInt(commentParts[24]));
                //细粮：糟源头—出窖时间	25
                mesZengSumData.setUpChujiaoTime(winccDataDealCommons.convertStringToDate(commentParts[25]));
                //细粮：糟源头—出窖层数	26
                mesZengSumData.setUpChujiaoLayer(Integer.parseInt(commentParts[26]));
                //细粮：糟源头—糟醅类型	27
                mesZengSumData.setUpZaopeiType(EnumZaopeiType.fromId(Integer.parseInt(commentParts[27])));
                //细粮：润粮开始时间	28
                mesZengSumData.setUpRunliangStartTime(winccDataDealCommons.convertStringToDate(commentParts[28]));
                //细粮：润粮结束时间	29
                mesZengSumData.setUpRunliangEndTime(winccDataDealCommons.convertStringToDate(commentParts[29]));
                //细粮：润粮时长	30
                mesZengSumData.setUpRunliangDuration(Float.parseFloat(commentParts[30]));
                //细粮：润粮时间是否达到工艺设定	31
                mesZengSumData.setUpIsDurationOk(Boolean.parseBoolean(commentParts[31]));
                //细粮：润粮加水量	32
                mesZengSumData.setUpRunliangWater(Float.parseFloat(commentParts[32]));
                //细粮：糟醅量	33
                mesZengSumData.setUpZaopeiQty(Float.parseFloat(commentParts[33]));
                //细粮：加稻壳量	34
                mesZengSumData.setUpDaoKe(Float.parseFloat(commentParts[34]));
                //细粮：加粮量	35
                mesZengSumData.setUpLiangShi(Float.parseFloat(commentParts[35]));
                //细粮：加粮类型	36
                boolean isUpFull = Boolean.parseBoolean(commentParts[36]);
                Integer upFull = isUpFull ? 1 : 0;
                mesZengSumData.setUpLiangType(EnumLiangshiType.fromId(upFull));

                // 甑锅任务开始时间	37
                mesZengSumData.setZengStartTime(winccDataDealCommons.convertStringToDate(commentParts[37]));
                //机器人上甑开始时间	38
                mesZengSumData.setRobotUpStartTime(winccDataDealCommons.convertStringToDate(commentParts[38]));
                //卡盘馏酒开始时间	39
                mesZengSumData.setKapanStartTime(winccDataDealCommons.convertStringToDate(commentParts[39]));
                //馏酒结束时间	40
                mesZengSumData.setLiujiuEndTime(winccDataDealCommons.convertStringToDate(commentParts[40]));
                //甑任务结束时间	41
                mesZengSumData.setZengEndTime(winccDataDealCommons.convertStringToDate(commentParts[41]));
                //甑序	42
                mesZengSumData.setZengNo(Integer.parseInt(commentParts[42]));
                //料源：来自?#甑桶	43
                MesZengguo mesZengguo = mesZengguoList.stream()
                        .filter(e ->e.getMesArea().getAreaCode().equals(areaNo) && e.getZengguoCode().equals(Integer.parseInt(commentParts[43])))
                        .findFirst().orElse(null);
                mesZengSumData.setResourceZengguo(mesZengguo);
                //糟醅类型	44
                mesZengSumData.setZengZaopeiType(EnumZaopeiType.fromId(Integer.parseInt(commentParts[44])));
                //上甑时长	45
                mesZengSumData.setShangzengDuration(Float.parseFloat(commentParts[45]));
                //上甑层数	46
                mesZengSumData.setShangzengLayer(Integer.parseInt(commentParts[46]));
                //上甑高度	47
                mesZengSumData.setShangzengHeight(Float.parseFloat(commentParts[47]));
                //上甑累计重量	48
                mesZengSumData.setShangzengWeight(Float.parseFloat(commentParts[48]));
                //添加量_黄水	49
                mesZengSumData.setAddHuangshui(Float.parseFloat(commentParts[49]));
                //添加量_酒尾	50
                mesZengSumData.setAddJiuwei(Float.parseFloat(commentParts[50]));
                //添加量_回收底锅水	51
                mesZengSumData.setAddHuishou(Float.parseFloat(commentParts[51]));
                //添加量_热水	52
                mesZengSumData.setAddReshui(Float.parseFloat(commentParts[52]));
                //接酒时长_1级	53
                mesZengSumData.setJiejiuFirstClassDuration(Float.parseFloat(commentParts[53]));
                //接酒时长_2级	54
                mesZengSumData.setJiejiuSecondClassDuration(Float.parseFloat(commentParts[54]));
                //接酒时长_3级	55
                mesZengSumData.setJiejiuThirdClassDuration(Float.parseFloat(commentParts[55]));
                //接酒时长_酒尾	56
                mesZengSumData.setJiejiuJiuweiDuration(Float.parseFloat(commentParts[56]));
                //接酒时长_废水	57
                mesZengSumData.setJiejiuFeishuiDuration(Float.parseFloat(commentParts[57]));
                //晾水添加量	58
                mesZengSumData.setLiangshuiAdd(Float.parseFloat(commentParts[58]));
                //耗汽量_上甑	59
                mesZengSumData.setHaoqiShangzeng(Float.parseFloat(commentParts[59]));
                //耗汽量_蒸馏	60
                mesZengSumData.setHaoqiZhengliu(Float.parseFloat(commentParts[60]));
                //蒸馏时长	61
                mesZengSumData.setZhengliuDuration(Float.parseFloat(commentParts[61]));

                //回窖糟：摊晾开始时间	62
                mesZengSumData.setTanliangStartTime(winccDataDealCommons.convertStringToDate(commentParts[62]));
                //回窖糟：摊晾结束时间	63
                mesZengSumData.setTanliangEndTime(winccDataDealCommons.convertStringToDate(commentParts[63]));
                //回窖糟：摊晾时长	64
                mesZengSumData.setTanliangDuration(Float.parseFloat(commentParts[64]));
                //回窖糟：该糟醅源头—出窖层数	65
                mesZengSumData.setTanliangOutLayer(Integer.parseInt(commentParts[65]));
                //回窖糟：最终入窖窖池编号	66
                mesZengSumData.setTanliangPoolInNo(Integer.parseInt(commentParts[66]));
                //回窖糟：该糟醅源头—出窖时间	67
                mesZengSumData.setTanliangChujiaoTime(winccDataDealCommons.convertStringToDate(commentParts[67]));
                //回窖糟：该糟醅源头—糟醅类型	68
                mesZengSumData.setTanliangZaopeiType(EnumZaopeiType.fromId(Integer.parseInt(commentParts[68])));
                //回窖糟：糟醅重量	69
                mesZengSumData.setTanliangZaopeiWeight(Float.parseFloat(commentParts[69]));
                //回窖糟：加曲重量	70
                mesZengSumData.setTanliangJiaquWeight(Float.parseFloat(commentParts[70]));
                //回窖糟：摊晾期间出口最大温度	71
                mesZengSumData.setTanliangMaxTemp(Float.parseFloat(commentParts[71]));
                //回窖糟：摊晾期间出口最小温度	72
                mesZengSumData.setTanliangMinTemp(Float.parseFloat(commentParts[72]));
                //回窖糟：摊晾期间出口平均温度	73
                mesZengSumData.setTanliangAvgTemp(Float.parseFloat(commentParts[73]));

                mesZengSumDataList.add(mesZengSumData);
                winccZengdata.setIsDealed(1);
            }
            dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(mesZengSumDataList, winccZengdataList));
        }
    }
}
