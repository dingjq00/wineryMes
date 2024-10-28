package com.mom.winery.app;

import com.mom.winery.entity.EnumMesWinccItemConfig;
import com.mom.winery.entity.MesArea;
import com.mom.winery.entity.MesJiaochi;
import com.mom.winery.entity.MesWinccItemConfig;
import com.mom.winery.quartzjob.RunliangJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class WinccDataDealCommons {
    private static final Logger log = LoggerFactory.getLogger(RunliangJob.class);
    public MesWinccItemConfig getMesWinccItemConfig(List<MesWinccItemConfig> mesWinccItemConfigList, EnumMesWinccItemConfig configType, Integer valueNo) {
        return mesWinccItemConfigList.stream()
                .filter(config -> config.getConfigType().equals(configType) && config.getValueNo().equals(valueNo))
                .findFirst()
                .orElse(null);
    }

    public MesJiaochi getMesJiaochi(List<MesJiaochi> mesJiaochiList, MesArea mesArea, Integer jiaochiNo) {
        return mesJiaochiList.stream()
                .filter(mesJiaochi -> mesJiaochi.getMesCell().getMesArea().equals(mesArea) && mesJiaochi.getJiaochiNo().equals(jiaochiNo))
                .findFirst()
                .orElse(null);
    }

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            log.error("Error", e);
            return null;
        }
    }
}
