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
public class MoniliangJob implements Job {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private WinccDataDealCommons winccDataDealCommons;

    @Authenticated
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<JobConfig> jobConfigList = dataManager.load(JobConfig.class)
                .query("select e from JobConfig e where e.mainPhase = :mainPhase")
                .parameter("mainPhase", EnumProcessMainPhase.MONILIANG)
                .list();

        for (JobConfig jobConfig : jobConfigList) {
            MesArea mesArea = jobConfig.getMesArea();
            Integer areaNo = jobConfig.getMesArea().getAreaCode();
            Integer currentWinccId = jobConfig.getWinccId();
            List<WinccMoniliang> winccMoniliangList = dataManager.load(WinccMoniliang.class)
                    .query("select e from WinccMoniliang e " +
                            "where e.areaNo = :areaNo " +
                            "and e.isDealed is null ")
                    .parameter("areaNo", areaNo)
                    .list();
            Integer maxWinccId = winccMoniliangList.stream().map(WinccMoniliang::getWinccId).max(Integer::compareTo).orElse(0);
            List<MesTanliangjiRealTimeData> mesTanliangjiRealTimeDataList = new ArrayList<>();
            List<MesZengguoRealData> mesZengguoRealDataList = new ArrayList<>();
            List<MesRunliangRealtimeData> mesRunliangRealtimeDataList = new ArrayList<>();
            List<MesGlobalRealData> mesGlobalRealDataList = new ArrayList<>();

            for (WinccMoniliang winccMoniliang : winccMoniliangList) {
                winccMoniliang.setIsDealed(1);
                Date winccUpdateTime = winccMoniliang.getStarttime();
                String comment = winccMoniliang.getComment();
                String[] commentParts = comment.split(",");
                /**
                 * 摊晾机模拟量数据
                 */
                MesTanliangjiRealTimeData mesTanliangjiRealTimeData = dataManager.create(MesTanliangjiRealTimeData.class);
                mesTanliangjiRealTimeData.setMesArea(mesArea);
                mesTanliangjiRealTimeData.setWinccUpdateTime(winccUpdateTime);
                mesTanliangjiRealTimeData.setWinccId(winccMoniliang.getWinccId());

                setTanliangjiNormalData(mesTanliangjiRealTimeData, commentParts);

                mesTanliangjiRealTimeDataList.add(mesTanliangjiRealTimeData);

                /**
                 * 甑锅模拟量数据
                 */
                MesZengguoRealData mesZengguoRealData = dataManager.create(MesZengguoRealData.class);
                mesZengguoRealData.setMesArea(mesArea);
                mesZengguoRealData.setWinccUpdateTime(winccUpdateTime);
                mesZengguoRealData.setWinccUpdateId(winccMoniliang.getWinccId());
                setZengguoNormalData(mesZengguoRealData, commentParts);

                mesZengguoRealDataList.add(mesZengguoRealData);

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

            }
            jobConfig.setWinccId(maxWinccId);

            saveData(jobConfig, mesTanliangjiRealTimeDataList, mesZengguoRealDataList, mesRunliangRealtimeDataList, mesGlobalRealDataList, winccMoniliangList);
        }
    }

    @Transactional
    protected void saveData(JobConfig jobConfig, List<MesTanliangjiRealTimeData> mesTanliangjiRealTimeDataList, List<MesZengguoRealData> mesZengguoRealDataList, List<MesRunliangRealtimeData> mesRunliangRealtimeDataList, List<MesGlobalRealData> mesGlobalRealDataList, List<WinccMoniliang> winccMoniliangList) {
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true)
                .saving(mesTanliangjiRealTimeDataList)
                .saving(mesZengguoRealDataList)
                .saving(mesRunliangRealtimeDataList)
                .saving(mesGlobalRealDataList)
                .saving(winccMoniliangList)
                .saving(jobConfig));
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

    private static void setZengguoNormalData(MesZengguoRealData mesZengguoRealData, String[] commentParts) {

        // 余重_1#机器人接料斗余重//余重_2#机器人接料斗余重//余重_3#机器人接料斗余重//余重_4#机器人接料斗余重
        setJieliaodouYuzhong(mesZengguoRealData, commentParts);

        // 设置锅内温度
        setZhengqiKaidu(mesZengguoRealData, commentParts);
        // 设置冷凝开度
        setLengningKaidu(mesZengguoRealData, commentParts);
        // 设置蒸汽瞬时流量
        setZhengqiShunshiLiuliang(mesZengguoRealData, commentParts);
        // 设置蒸汽压力
        setZhengqiYali(mesZengguoRealData, commentParts);
        // 设置锅内温度
        setGuoniWendu(mesZengguoRealData, commentParts);
        // 设置真空压力
        setZhengkongYali(mesZengguoRealData, commentParts);
        // 设置底锅水温
        setDiguoShuiwen(mesZengguoRealData, commentParts);
        // 设置出酒温度
        setChujiuWendu(mesZengguoRealData, commentParts);
        // 设置回水温度
        setHuishuiWendu(mesZengguoRealData, commentParts);
        // 量水罐液位	139
        mesZengguoRealData.setLiangshuiguanYewei( Float.parseFloat(commentParts[139]));
        // 尾酒罐液位	140
        mesZengguoRealData.setWeijiuguanYewei( Float.parseFloat(commentParts[140]));
        // 回收底锅水位	142
        mesZengguoRealData.setHuishouDiguoShuiwei( Float.parseFloat(commentParts[142]));
        //丢糟尾酒罐液位	143
        mesZengguoRealData.setDiuZaoWeijiuguanYewei( Float.parseFloat(commentParts[143]));
    }

    private static void setJieliaodouYuzhong(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        // 8	余重_1#机器人接料斗余重
        mesZengguoRealData.setJieliaodouYuzhong1( Float.parseFloat(commentParts[8]));
        //9	余重_2#机器人接料斗余重
        mesZengguoRealData.setJieliaodouYuzhong2( Float.parseFloat(commentParts[9]));
        //10	余重_3#机器人接料斗余重
        mesZengguoRealData.setJieliaodouYuzhong3( Float.parseFloat(commentParts[10]));
        //11	余重_4#机器人接料斗余重
        mesZengguoRealData.setJieliaodouYuzhong4( Float.parseFloat(commentParts[11]));
    }

    private static void setHuishuiWendu(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        //1#回水温度	122
        mesZengguoRealData.setHuishuiWendu1( Float.parseFloat(commentParts[122]));
        //2#回水温度	123
        mesZengguoRealData.setHuishuiWendu2( Float.parseFloat(commentParts[123]));
        //3#回水温度	124
        mesZengguoRealData.setHuishuiWendu3( Float.parseFloat(commentParts[124]));
        //4#回水温度	125
        mesZengguoRealData.setHuishuiWendu4( Float.parseFloat(commentParts[125]));
        //5#回水温度	126
        mesZengguoRealData.setHuishuiWendu5( Float.parseFloat(commentParts[126]));
        //6#回水温度	127
        mesZengguoRealData.setHuishuiWendu6( Float.parseFloat(commentParts[127]));
        //7#回水温度	128
        mesZengguoRealData.setHuishuiWendu7( Float.parseFloat(commentParts[128]));
        //8#回水温度	129
        mesZengguoRealData.setHuishuiWendu8( Float.parseFloat(commentParts[129]));
        //9#回水温度	130
        mesZengguoRealData.setHuishuiWendu9( Float.parseFloat(commentParts[130]));
        //10#回水温度	131
        mesZengguoRealData.setHuishuiWendu10( Float.parseFloat(commentParts[131]));
        //11#回水温度	132
        mesZengguoRealData.setHuishuiWendu11( Float.parseFloat(commentParts[132]));
        //12#回水温度	133
        mesZengguoRealData.setHuishuiWendu12( Float.parseFloat(commentParts[133]));
    }

    private static void setChujiuWendu(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        //1#出酒温度	110
        mesZengguoRealData.setChujiuWendu1( Float.parseFloat(commentParts[110]));
        //2#出酒温度	111
        mesZengguoRealData.setChujiuWendu2( Float.parseFloat(commentParts[111]));
        //3#出酒温度	112
        mesZengguoRealData.setChujiuWendu3( Float.parseFloat(commentParts[112]));
        //4#出酒温度	113
        mesZengguoRealData.setChujiuWendu4( Float.parseFloat(commentParts[113]));
        //5#出酒温度	114
        mesZengguoRealData.setChujiuWendu5( Float.parseFloat(commentParts[114]));
        //6#出酒温度	115
        mesZengguoRealData.setChujiuWendu6( Float.parseFloat(commentParts[115]));
        //7#出酒温度	116
        mesZengguoRealData.setChujiuWendu7( Float.parseFloat(commentParts[116]));
        //8#出酒温度	117
        mesZengguoRealData.setChujiuWendu8( Float.parseFloat(commentParts[117]));
        //9#出酒温度	118
        mesZengguoRealData.setChujiuWendu9( Float.parseFloat(commentParts[118]));
        //10#出酒温度	119
        mesZengguoRealData.setChujiuWendu10( Float.parseFloat(commentParts[119]));
        //11#出酒温度	120
        mesZengguoRealData.setChujiuWendu11( Float.parseFloat(commentParts[120]));
        //12#出酒温度	121
        mesZengguoRealData.setChujiuWendu12( Float.parseFloat(commentParts[121]));
    }

    private static void setDiguoShuiwen(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        // 1#底锅水温	98
        mesZengguoRealData.setDiguoShuiwen1( Float.parseFloat(commentParts[98]));
        //2#底锅水温	99
        mesZengguoRealData.setDiguoShuiwen2( Float.parseFloat(commentParts[99]));
        //3#底锅水温	100
        mesZengguoRealData.setDiguoShuiwen3( Float.parseFloat(commentParts[100]));
        //4#底锅水温	101
        mesZengguoRealData.setDiguoShuiwen4( Float.parseFloat(commentParts[101]));
        //5#底锅水温	102
        mesZengguoRealData.setDiguoShuiwen5( Float.parseFloat(commentParts[102]));
        //6#底锅水温	103
        mesZengguoRealData.setDiguoShuiwen6( Float.parseFloat(commentParts[103]));
        //7#底锅水温	104
        mesZengguoRealData.setDiguoShuiwen7( Float.parseFloat(commentParts[104]));
        //8#底锅水温	105
        mesZengguoRealData.setDiguoShuiwen8( Float.parseFloat(commentParts[105]));
        //9#底锅水温	106
        mesZengguoRealData.setDiguoShuiwen9( Float.parseFloat(commentParts[106]));
        //10#底锅水温	107
        mesZengguoRealData.setDiguoShuiwen10( Float.parseFloat(commentParts[107]));
        //11#底锅水温	108
        mesZengguoRealData.setDiguoShuiwen11( Float.parseFloat(commentParts[108]));
        //12#底锅水温	109
        mesZengguoRealData.setDiguoShuiwen12( Float.parseFloat(commentParts[109]));
    }

    private static void setZhengkongYali(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        // 1#真空压力	86
        mesZengguoRealData.setZhenkongYali1( Float.parseFloat(commentParts[86]));
        //2#真空压力	87
        mesZengguoRealData.setZhenkongYali2( Float.parseFloat(commentParts[87]));
        //3#真空压力	88
        mesZengguoRealData.setZhenkongYali3( Float.parseFloat(commentParts[88]));
        //4#真空压力	89
        mesZengguoRealData.setZhenkongYali4( Float.parseFloat(commentParts[89]));
        //5#真空压力	90
        mesZengguoRealData.setZhenkongYali5( Float.parseFloat(commentParts[90]));
        //6#真空压力	91
        mesZengguoRealData.setZhenkongYali6( Float.parseFloat(commentParts[91]));
        //7#真空压力	92
        mesZengguoRealData.setZhenkongYali7( Float.parseFloat(commentParts[92]));
        //8#真空压力	93
        mesZengguoRealData.setZhenkongYali8( Float.parseFloat(commentParts[93]));
        //9#真空压力	94
        mesZengguoRealData.setZhenkongYali9( Float.parseFloat(commentParts[94]));
        //10#真空压力	95
        mesZengguoRealData.setZhenkongYali10( Float.parseFloat(commentParts[95]));
        //11#真空压力	96
        mesZengguoRealData.setZhenkongYali11( Float.parseFloat(commentParts[96]));
        //12#真空压力	97
        mesZengguoRealData.setZhenkongYali12( Float.parseFloat(commentParts[97]));
    }

    private static void setTanliangjiNormalData(MesTanliangjiRealTimeData mesTanliangjiRealTimeData, String[] commentParts) {
        // 余重_1#摊晾给料机//余重_2#摊晾给料机//余重_1#曲粉暂存仓//余重_2#曲粉暂存仓
        mesTanliangjiRealTimeData.setTanliangGeiliaojiYuzhong1( Float.parseFloat(commentParts[12]));
        mesTanliangjiRealTimeData.setTanliangGeiliaojiYuzhong2( Float.parseFloat(commentParts[13]));
        mesTanliangjiRealTimeData.setQufeiZancuncangYuzhong1( Float.parseFloat(commentParts[14]));
        mesTanliangjiRealTimeData.setQufeiZancuncangYuzhong2( Float.parseFloat(commentParts[15]));
        // 21 流速_1#摊晾给料机   22 流速_2#摊晾给料机   23 流速_1#曲粉   24 流速_2#曲粉
        mesTanliangjiRealTimeData.setTangliangGeiliaojiLiusu1( Float.parseFloat(commentParts[21]));
        mesTanliangjiRealTimeData.setTangliangGeiliaojiLiusu2( Float.parseFloat(commentParts[22]));
        mesTanliangjiRealTimeData.setQufenLiusu1( Float.parseFloat(commentParts[23]));
        mesTanliangjiRealTimeData.setQufenLiusu2( Float.parseFloat(commentParts[24]));
        //134 1#摊晾中部温度； 135 1#摊晾出口温度； 136 2#摊晾中部温度； 137 2#摊晾出口温度
        mesTanliangjiRealTimeData.setTanliangZhongbuTemp1( Float.parseFloat(commentParts[134]));
        mesTanliangjiRealTimeData.setTanliangChukouTemp1( Float.parseFloat(commentParts[135]));
        mesTanliangjiRealTimeData.setTanliangZhongbuTemp2( Float.parseFloat(commentParts[136]));
        mesTanliangjiRealTimeData.setTanliangChukouTemp2( Float.parseFloat(commentParts[137]));
    }

    private static void setGuoniWendu(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        //1#锅内温度	73
        mesZengguoRealData.setGuoniWendu1( Float.parseFloat(commentParts[73]));
        //2#锅内温度	74
        mesZengguoRealData.setGuoniWendu2( Float.parseFloat(commentParts[74]));
        //3#锅内温度	75
        mesZengguoRealData.setGuoniWendu3( Float.parseFloat(commentParts[75]));
        //4#锅内温度	76
        mesZengguoRealData.setGuoniWendu4( Float.parseFloat(commentParts[76]));
        //5#锅内温度	77
        mesZengguoRealData.setGuoniWendu5( Float.parseFloat(commentParts[77]));
        //6#锅内温度	78
        mesZengguoRealData.setGuoniWendu6( Float.parseFloat(commentParts[78]));
        //7#锅内温度	79
        mesZengguoRealData.setGuoniWendu7( Float.parseFloat(commentParts[79]));
        //8#锅内温度	80
        mesZengguoRealData.setGuoniWendu8( Float.parseFloat(commentParts[80]));
        //9#锅内温度	81
        mesZengguoRealData.setGuoniWendu9( Float.parseFloat(commentParts[81]));
        //10#锅内温度	82
        mesZengguoRealData.setGuoniWendu10( Float.parseFloat(commentParts[82]));
        //11#锅内温度	83
        mesZengguoRealData.setGuoniWendu11( Float.parseFloat(commentParts[83]));
        //12#锅内温度	84
        mesZengguoRealData.setGuoniWendu12( Float.parseFloat(commentParts[84]));
    }

    private static void setZhengqiYali(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        //1#蒸汽压力	61
        mesZengguoRealData.setZhengqiYali1( Float.parseFloat(commentParts[61]));
        //2#蒸汽压力	62
        mesZengguoRealData.setZhengqiYali2( Float.parseFloat(commentParts[62]));
        //3#蒸汽压力	63
        mesZengguoRealData.setZhengqiYali3( Float.parseFloat(commentParts[63]));
        //4#蒸汽压力	64
        mesZengguoRealData.setZhengqiYali4( Float.parseFloat(commentParts[64]));
        //5#蒸汽压力	65
        mesZengguoRealData.setZhengqiYali5( Float.parseFloat(commentParts[65]));
        //6#蒸汽压力	66
        mesZengguoRealData.setZhengqiYali6( Float.parseFloat(commentParts[66]));
        //7#蒸汽压力	67
        mesZengguoRealData.setZhengqiYali7( Float.parseFloat(commentParts[67]));
        //8#蒸汽压力	68
        mesZengguoRealData.setZhengqiYali8( Float.parseFloat(commentParts[68]));
        //9#蒸汽压力	69
        mesZengguoRealData.setZhengqiYali9( Float.parseFloat(commentParts[69]));
        //10#蒸汽压力	70
        mesZengguoRealData.setZhengqiYali10( Float.parseFloat(commentParts[70]));
        //11#蒸汽压力	71
        mesZengguoRealData.setZhengqiYali11( Float.parseFloat(commentParts[71]));
        //12#蒸汽压力	72
        mesZengguoRealData.setZhengqiYali12( Float.parseFloat(commentParts[72]));
    }

    private static void setZhengqiShunshiLiuliang(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        //1#蒸汽瞬时流量	49
        mesZengguoRealData.setZhengqiShunshiLiuliang1( Float.parseFloat(commentParts[49]));
        //2#蒸汽瞬时流量	50
        mesZengguoRealData.setZhengqiShunshiLiuliang2( Float.parseFloat(commentParts[50]));
        //3#蒸汽瞬时流量	51
        mesZengguoRealData.setZhengqiShunshiLiuliang3( Float.parseFloat(commentParts[51]));
        //4#蒸汽瞬时流量	52
        mesZengguoRealData.setZhengqiShunshiLiuliang4( Float.parseFloat(commentParts[52]));
        //5#蒸汽瞬时流量	53
        mesZengguoRealData.setZhengqiShunshiLiuliang5( Float.parseFloat(commentParts[53]));
        //6#蒸汽瞬时流量	54
        mesZengguoRealData.setZhengqiShunshiLiuliang6( Float.parseFloat(commentParts[54]));
        //7#蒸汽瞬时流量	55
        mesZengguoRealData.setZhengqiShunshiLiuliang7( Float.parseFloat(commentParts[55]));
        //8#蒸汽瞬时流量	56
        mesZengguoRealData.setZhengqiShunshiLiuliang8( Float.parseFloat(commentParts[56]));
        //9#蒸汽瞬时流量	57
        mesZengguoRealData.setZhengqiShunshiLiuliang9( Float.parseFloat(commentParts[57]));
        //10#蒸汽瞬时流量	58
        mesZengguoRealData.setZhengqiShunshiLiuliang10( Float.parseFloat(commentParts[58]));
        //11#蒸汽瞬时流量	59
        mesZengguoRealData.setZhengqiShunshiLiuliang11( Float.parseFloat(commentParts[59]));
        //12#蒸汽瞬时流量	60
        mesZengguoRealData.setZhengqiShunshiLiuliang12( Float.parseFloat(commentParts[60]));
    }

    private static void setLengningKaidu(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        // 1#冷凝开度	37
        mesZengguoRealData.setLengningKaidu1( Float.parseFloat(commentParts[37]));
        //2#冷凝开度	38
        mesZengguoRealData.setLengningKaidu2( Float.parseFloat(commentParts[38]));
        //3#冷凝开度	39
        mesZengguoRealData.setLengningKaidu3( Float.parseFloat(commentParts[39]));
        //4#冷凝开度	40
        mesZengguoRealData.setLengningKaidu4( Float.parseFloat(commentParts[40]));
        //5#冷凝开度	41
        mesZengguoRealData.setLengningKaidu5( Float.parseFloat(commentParts[41]));
        //6#冷凝开度	42
        mesZengguoRealData.setLengningKaidu6( Float.parseFloat(commentParts[42]));
        //7#冷凝开度	43
        mesZengguoRealData.setLengningKaidu7( Float.parseFloat(commentParts[43]));
        //8#冷凝开度	44
        mesZengguoRealData.setLengningKaidu8( Float.parseFloat(commentParts[44]));
        //9#冷凝开度	45
        mesZengguoRealData.setLengningKaidu9( Float.parseFloat(commentParts[45]));
        //10#冷凝开度	46
        mesZengguoRealData.setLengningKaidu10( Float.parseFloat(commentParts[46]));
        //11#冷凝开度	47
        mesZengguoRealData.setLengningKaidu11( Float.parseFloat(commentParts[47]));
        //12#冷凝开度	48
        mesZengguoRealData.setLengningKaidu12( Float.parseFloat(commentParts[48]));
    }

    private static void setZhengqiKaidu(MesZengguoRealData mesZengguoRealData, String[] commentParts) {
        // 1#蒸汽开度	25
        mesZengguoRealData.setZhengqiKaidu1( Float.parseFloat(commentParts[25]));
        //2#蒸汽开度	26
        mesZengguoRealData.setZhengqiKaidu2( Float.parseFloat(commentParts[26]));
        //3#蒸汽开度	27
        mesZengguoRealData.setZhengqiKaidu3( Float.parseFloat(commentParts[27]));
        //4#蒸汽开度	28
        mesZengguoRealData.setZhengqiKaidu4( Float.parseFloat(commentParts[28]));
        //5#蒸汽开度	29
        mesZengguoRealData.setZhengqiKaidu5( Float.parseFloat(commentParts[29]));
        //6#蒸汽开度	30
        mesZengguoRealData.setZhengqiKaidu6( Float.parseFloat(commentParts[30]));
        //7#蒸汽开度	31
        mesZengguoRealData.setZhengqiKaidu7( Float.parseFloat(commentParts[31]));
        //8#蒸汽开度	32
        mesZengguoRealData.setZhengqiKaidu8( Float.parseFloat(commentParts[32]));
        //9#蒸汽开度	33
        mesZengguoRealData.setZhengqiKaidu9( Float.parseFloat(commentParts[33]));
        //10#蒸汽开度	34
        mesZengguoRealData.setZhengqiKaidu10( Float.parseFloat(commentParts[34]));
        //11#蒸汽开度	35
        mesZengguoRealData.setZhengqiKaidu11( Float.parseFloat(commentParts[35]));
        // 12#蒸汽开度	36
        mesZengguoRealData.setZhengqiKaidu12( Float.parseFloat(commentParts[36]));
    }

}
