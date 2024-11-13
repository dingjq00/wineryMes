package com.mom.winery.view.mestanliangjirecord;

import com.mom.winery.entity.MesTanliangjiRecord;
import com.mom.winery.entity.MesZengguo;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 15:54
 */

@Route(value = "mesTanliangjiRecords", layout = MainView.class)
@ViewController("MesTanliangjiRecord.list")
@ViewDescriptor("mes-tanliangji-record-list-view.xml")
@LookupComponent("mesTanliangjiRecordsDataGrid")
@DialogMode(width = "64em")
public class MesTanliangjiRecordListView extends StandardListView<MesTanliangjiRecord> {
    @Autowired
    private DataManager dataManager;

    @Subscribe(id = "changeResorce", subject = "clickListener")
    public void onChangeResorceClick(final ClickEvent<JmixButton> event) {
        List<MesTanliangjiRecord> mesTanliangjiRecordList12 = dataManager.load(MesTanliangjiRecord.class)
                .query("select e from MesTanliangjiRecord e where e.mesTanliangji.mesArea.mesShopfloor.mesShopfloorName = :shopFloorName")
                .parameter("shopFloorName", "12车间")
                .list();
        List<MesZengguo> mesZengguoList = dataManager.load(MesZengguo.class)
                .query("select e from MesZengguo e where e.mesArea.mesShopfloor.mesShopfloorName = :shopFloorName")
                .parameter("shopFloorName", "12车间")
                .list();
        for (MesTanliangjiRecord mesTanliangjiRecord : mesTanliangjiRecordList12) {
            MesZengguo rawMesZengguo = mesTanliangjiRecord.getResourceZengguo();
            if(rawMesZengguo == null){
                continue;
            }
            Integer rawMesZengguoCode = rawMesZengguo.getZengguoCode();
            MesZengguo mesZengguo = mesZengguoList.stream()
                    .filter(e -> e.getZengguoCode().equals(rawMesZengguoCode))
                    .findFirst().orElse(null);
            mesTanliangjiRecord.setResourceZengguo(mesZengguo);
        }
        dataManager.save(new SaveContext().setDiscardSaved(true).saving(mesTanliangjiRecordList12));

    }
}
