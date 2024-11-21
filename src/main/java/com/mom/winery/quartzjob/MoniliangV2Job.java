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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 20:34
 */
@DisallowConcurrentExecution
public class MoniliangV2Job implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Autowired
    private JobSaveDataService jobSaveDataService;

    @Authenticated
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.MONILIANG)
                .list();
        List<MesZengguo> mesZengguoList = dataManager.load(MesZengguo.class)
                .query("select e from MesZengguo e")
                .list();
        List<MesTanliangji> mesTanliangjiList = dataManager.load(MesTanliangji.class)
                .query("select e from MesTanliangji e")
                .list();

        for (JobConfig jobConfig : jobConfigList) {
            MesArea mesArea = jobConfig.getMesArea();
            Integer areaNo = jobConfig.getMesArea().getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();
            List<WinccMoniliang> winccMoniliangList = dataManager.load(WinccMoniliang.class)
                    .query("select e from WinccMoniliang e " +
                            "where e.areaNo = :areaNo " +
                            "and e.isDealed is null " +
                            "order by e.winccId")
                    .parameter("areaNo", areaNo)
                    .maxResults(5000)
                    .list();
            if(winccMoniliangList.isEmpty()){
                continue;
            }
            Integer maxWinccId = winccMoniliangList.stream().map(WinccMoniliang::getWinccId).max(Integer::compareTo).orElse(0);
            List<MesTanliangRealDataV2> mesTanliangRealTimeDataList = new ArrayList<>();
            List<MesRunliangRealtimeData> mesRunliangRealtimeDataList = new ArrayList<>();
            List<MesGlobalRealData> mesGlobalRealDataList = new ArrayList<>();
            List<MesZengguoRealDataV2> mesZengguoRealDataV2List = new ArrayList<>();

            for (WinccMoniliang winccMoniliang : winccMoniliangList) {
                winccMoniliang.setIsDealed(1);
                Date winccUpdateTime = winccMoniliang.getStarttime();
                String comment = winccMoniliang.getComment();
                String[] commentParts = comment.split(",");


                /**
                 * 配润模拟量数据
                 */
                MesRunliangRealtimeData mesRunliangRealtimeData = dataManager.create(MesRunliangRealtimeData.class);
                mesRunliangRealtimeData.setMesArea(mesArea);
                mesRunliangRealtimeData.setWinccUpdateId(winccMoniliang.getWinccId());
                mesRunliangRealtimeData.setWinccUdateTime(winccUpdateTime);

                setRunliangNormalInfo(mesRunliangRealtimeData, commentParts);
                mesRunliangRealtimeDataList.add(mesRunliangRealtimeData);


                /**
                 * 全局模拟量数据
                 */
                MesGlobalRealData mesGlobalRealData = dataManager.create(MesGlobalRealData.class);
                mesGlobalRealData.setMesArea(mesArea);
                mesGlobalRealData.setWinccUpdateId(winccMoniliang.getWinccId());
                mesGlobalRealData.setWinccUpdateTime(winccUpdateTime);
                setGlobalNormalInfo(mesGlobalRealData, commentParts);
                mesGlobalRealDataList.add(mesGlobalRealData);

                /**
                 * 摊晾机模拟量数据
                 */
                List<MesTanliangji> areaTanliangjiList = mesTanliangjiList.stream().filter(e -> e.getMesArea().equals(mesArea)).toList();
                for (MesTanliangji mesTanliangji : areaTanliangjiList) {
                    MesTanliangRealDataV2 mesTanliangRealData = dataManager.create(MesTanliangRealDataV2.class);
                    mesTanliangRealData.setMesArea(mesArea);
                    mesTanliangRealData.setMesTanliangji(mesTanliangji);
                    mesTanliangRealData.setWinccUpdateTime(winccUpdateTime);
                    mesTanliangRealData.setWinccUpdateId(winccMoniliang.getWinccId());
                    switch (mesTanliangji.getTanliangjiNo()) {
                        case 1:
                            setTanliangData(mesTanliangRealData, commentParts, 12, 14, 21,
                                    23, 134, 135);
                            break;
                        case 2:
                            setTanliangData(mesTanliangRealData, commentParts, 13, 15, 22,
                                    24, 136, 137);
                            break;
                    }
                    mesTanliangRealTimeDataList.add(mesTanliangRealData);
                }

                /**
                 * 甑锅模拟量数据
                 */
                List<MesZengguo> areaZengguoList = mesZengguoList.stream().filter(e -> e.getMesArea().equals(mesArea)).toList();
                for (MesZengguo mesZengguo : areaZengguoList) {
                    MesZengguoRealDataV2 mesZengguoRealData = dataManager.create(MesZengguoRealDataV2.class);
                    mesZengguoRealData.setMesArea(mesArea);
                    mesZengguoRealData.setMesZengguo(mesZengguo);
                    mesZengguoRealData.setWinccUpdateTime(winccUpdateTime);
                    mesZengguoRealData.setWinccUpdateId(winccMoniliang.getWinccId());
                    switch (mesZengguo.getZengguoCode()) {
                        case 1:
                            setZengguoData(mesZengguoRealData, commentParts, 25, 37, 49, 61, 73, 86, 98, 110, 122);
                            break;
                        case 2:
                            setZengguoData(mesZengguoRealData, commentParts, 26, 38, 50, 62, 74, 87, 99, 111, 123);
                            break;
                        case 3:
                            setZengguoData(mesZengguoRealData, commentParts, 27, 39, 51, 63, 75, 88, 100, 112, 124);
                            break;
                        case 4:
                            setZengguoData(mesZengguoRealData, commentParts, 28, 40, 52, 64, 76, 89, 101, 113, 125);
                            break;
                        case 5:
                            setZengguoData(mesZengguoRealData, commentParts, 29, 41, 53, 65, 77, 90, 102, 114, 126);
                            break;
                        case 6:
                            setZengguoData(mesZengguoRealData, commentParts, 30, 42, 54, 66, 78, 91, 103, 115, 127);
                            break;
                        case 7:
                            setZengguoData(mesZengguoRealData, commentParts, 31, 43, 55, 67, 79, 92, 104, 116, 128);
                            break;
                        case 8:
                            setZengguoData(mesZengguoRealData, commentParts, 32, 44, 56, 68, 80, 93, 105, 117, 129);
                            break;
                        case 9:
                            setZengguoData(mesZengguoRealData, commentParts, 33, 45, 57, 69, 81, 94, 106, 118, 130);
                            break;
                        case 10:
                            setZengguoData(mesZengguoRealData, commentParts, 34, 46, 58, 70, 82, 95, 107, 119, 131);
                            break;
                        case 11:
                            setZengguoData(mesZengguoRealData, commentParts, 35, 47, 59, 71, 83, 96, 108, 120, 132);
                            break;
                        case 12:
                            setZengguoData(mesZengguoRealData, commentParts, 36, 48, 60, 72, 84, 97, 109, 121, 133);
                            break;
                    }
                    mesZengguoRealDataV2List.add(mesZengguoRealData);
                }

            }
            jobConfig.setWinccId(maxWinccId);
            jobSaveDataService.saveRealData(jobConfig, mesTanliangRealTimeDataList, mesZengguoRealDataV2List, mesRunliangRealtimeDataList, mesGlobalRealDataList, winccMoniliangList);
        }
    }

    private void setZengguoData(MesZengguoRealDataV2 mesZengguoRealData, String[] commentParts, int zhengqiKaiduIndex, int lengningKaiduIndex, int zhengqiShunshiLiuliangIndex, int zhengqiYaliIndex, int guoniWenduIndex, int zhenkongYaliIndex, int diguoShuiwenIndex, int chujiuWenduIndex, int huishuiWenduIndex) {
        mesZengguoRealData.setZhengqiKaidu(Float.parseFloat(commentParts[zhengqiKaiduIndex]));
        mesZengguoRealData.setLengningKaidu(Float.parseFloat(commentParts[lengningKaiduIndex]));
        mesZengguoRealData.setZhengqiShunshiLiuliang(Float.parseFloat(commentParts[zhengqiShunshiLiuliangIndex]));
        mesZengguoRealData.setZhengqiYali(Float.parseFloat(commentParts[zhengqiYaliIndex]));
        mesZengguoRealData.setGuoniWendu(Float.parseFloat(commentParts[guoniWenduIndex]));
        mesZengguoRealData.setZhenkongYali(Float.parseFloat(commentParts[zhenkongYaliIndex]));
        mesZengguoRealData.setDiguoShuiwen(Float.parseFloat(commentParts[diguoShuiwenIndex]));
        mesZengguoRealData.setChujiuWendu(Float.parseFloat(commentParts[chujiuWenduIndex]));
        mesZengguoRealData.setHuishuiWendu(Float.parseFloat(commentParts[huishuiWenduIndex]));
    }

    private void setTanliangData(MesTanliangRealDataV2 mesTanliangRealData, String[] commentParts, int geiliaojiYuzhongIndex, int zancuncangYuzhongIndex, int geiliaojiLiusuIndex, int qufenLiusuIndex, int zhongbuTempIndex, int chukouTempIndex) {
        mesTanliangRealData.setTanliangGeiliaojiYuzhong(Float.parseFloat(commentParts[geiliaojiYuzhongIndex]));
        mesTanliangRealData.setQufeiZancuncangYuzhong(Float.parseFloat(commentParts[zancuncangYuzhongIndex]));
        mesTanliangRealData.setTangliangGeiliaojiLiusu(Float.parseFloat(commentParts[geiliaojiLiusuIndex]));
        mesTanliangRealData.setQufenLiusu(Float.parseFloat(commentParts[qufenLiusuIndex]));
        mesTanliangRealData.setTanliangZhongbuTemp(Float.parseFloat(commentParts[zhongbuTempIndex]));
        mesTanliangRealData.setTanliangChukouTemp(Float.parseFloat(commentParts[chukouTempIndex]));
    }



    private static void setGlobalNormalInfo(MesGlobalRealData mesGlobalRealData, String[] commentParts) {
        // 乏汽冷凝调节阀开度	85
        mesGlobalRealData.setFaqiLengningTiaojiefaKaidu(Float.parseFloat(commentParts[85]));
        // 清洗水罐液位	141
        mesGlobalRealData.setQingxiShuiguanYew(Float.parseFloat(commentParts[141]));
        // 循环水缓存罐液位    144
        mesGlobalRealData.setXunhuaShuiHuancunguanYew(Float.parseFloat(commentParts[144]));
        // 清洗水罐水温	147
        mesGlobalRealData.setQingxiShuiguanShuiwen(Float.parseFloat(commentParts[147]));
        //循环水温-进水	148
        mesGlobalRealData.setXunhuaShuiwenJinshui(Float.parseFloat(commentParts[148]));
        //循环水温-回水	149
        mesGlobalRealData.setXunhuaShuiwenHuishui(Float.parseFloat(commentParts[149]));
        //循环进水流量	150
        mesGlobalRealData.setXunhuaJinshuiLiuliang(Float.parseFloat(commentParts[150]));
        //循环出水流量	151
        mesGlobalRealData.setXunhuaChushuiLiuliang(Float.parseFloat(commentParts[151]));

        // 8	余重_1#机器人接料斗余重
        mesGlobalRealData.setJieliaodouYuzhong1( Float.parseFloat(commentParts[8]));
        //9	余重_2#机器人接料斗余重
        mesGlobalRealData.setJieliaodouYuzhong2( Float.parseFloat(commentParts[9]));
        //10	余重_3#机器人接料斗余重
        mesGlobalRealData.setJieliaodouYuzhong3( Float.parseFloat(commentParts[10]));
        //11	余重_4#机器人接料斗余重
        mesGlobalRealData.setJieliaodouYuzhong4( Float.parseFloat(commentParts[11]));


        // 量水罐液位	139
        mesGlobalRealData.setLiangshuiguanYewei( Float.parseFloat(commentParts[139]));
        // 尾酒罐液位	140
        mesGlobalRealData.setWeijiuguanYewei( Float.parseFloat(commentParts[140]));
        // 回收底锅水位	142
        mesGlobalRealData.setHuishouDiguoShuiwei( Float.parseFloat(commentParts[142]));
        //丢糟尾酒罐液位	143
        mesGlobalRealData.setDiuZaoWeijiuguanYewei( Float.parseFloat(commentParts[143]));
    }

    private static void setRunliangNormalInfo(MesRunliangRealtimeData mesRunliangRealtimeData, String[] commentParts) {
        // 0	余重_粗粮暂存仓
        mesRunliangRealtimeData.setCuliangZancuncangYuzhong(Float.parseFloat(commentParts[0]));
        //1	余重_细粮暂存仓
        mesRunliangRealtimeData.setXiliangZancuncangYuzhong(Float.parseFloat(commentParts[1]));
        //2	余重_粗粮料斗称
        mesRunliangRealtimeData.setCuliangLiaodouYuzhong(Float.parseFloat(commentParts[2]));
        //3	余重_细粮料斗称
        mesRunliangRealtimeData.setXiliangLiaodouYuzhong(Float.parseFloat(commentParts[3]));
        //4	余重_红糟给料机
        mesRunliangRealtimeData.setHongzaoGeiLiaojiYuzhong(Float.parseFloat(commentParts[4]));
        //5	余重_中上糟给料机
        mesRunliangRealtimeData.setZhongshangGeiLiaojiYuzhong(Float.parseFloat(commentParts[5]));
        //6	余重_底糟给料机
        mesRunliangRealtimeData.setDiZaoGeiLiaojiYuzhong(Float.parseFloat(commentParts[6]));
        //7	余重_稻壳暂存仓
        mesRunliangRealtimeData.setDaokeZancuncangYuzhong(Float.parseFloat(commentParts[7]));

        // 16	流速_粗粮
        mesRunliangRealtimeData.setLiusuCuliang(Float.parseFloat(commentParts[16]));
        //17	流速_细粮
        mesRunliangRealtimeData.setLiusuXiliang(Float.parseFloat(commentParts[17]));
        //18	流速_红糟
        mesRunliangRealtimeData.setLiusuHongzao(Float.parseFloat(commentParts[18]));
        //19	流速_中上糟
        mesRunliangRealtimeData.setLiusuZhongshang(Float.parseFloat(commentParts[19]));
        //20	流速_底糟
        mesRunliangRealtimeData.setLiusuDiZao(Float.parseFloat(commentParts[20]));

        // 润粮水罐液位	138
        mesRunliangRealtimeData.setRunliangShuiguanYew(Float.parseFloat(commentParts[138]));

        // 润粮罐水温	145
        mesRunliangRealtimeData.setRunliangGuanShuiwen(Float.parseFloat(commentParts[145]));
        //量水罐水温	146
        mesRunliangRealtimeData.setLiangshuiGuanShuiwen(Float.parseFloat(commentParts[146]));
    }
}
