package com.mom.winery.view.mesarea;

import com.mom.winery.entity.MesArea;
import com.mom.winery.entity.MesRunliangdou;
import com.mom.winery.entity.MesTanliangji;
import com.mom.winery.entity.MesZengguo;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 14:45
 */

@Route(value = "mesAreas", layout = MainView.class)
@ViewController("MesArea.list")
@ViewDescriptor("mes-area-list-view.xml")
@LookupComponent("mesAreasDataGrid")
@DialogMode(width = "64em")
public class MesAreaListView extends StandardListView<MesArea> {
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private DataGrid<MesArea> mesAreasDataGrid;
    @Autowired
    private Notifications notifications;

    @Subscribe(id = "createBasicInfoFor12", subject = "clickListener")
    public void onCreateBasicInfoFor12Click(final ClickEvent<JmixButton> event) {
        MesArea mesArea = mesAreasDataGrid.getSingleSelectedItem();
        if (mesArea == null) {
            notifications.create("Create Basic Info For 12")
                    .show();
            return;
        }
        List<MesArea> mesAreas = dataManager.load(MesArea.class)
                .query("select e from MesArea e where e.mesShopfloor.mesShopfloorCode = :shopFloorCode")
                .parameter("shopFloorCode", "12车间")
                .list();
        List<MesRunliangdou> mesRunliangdous = dataManager.load(MesRunliangdou.class)
                .query("select e from MesRunliangdou e where e.mesArea = :mesArea " +
                        "order by e.createdDate")
                .parameter("mesArea", mesArea)
                .list();

        List<MesTanliangji> mesTanliangjis = dataManager.load(MesTanliangji.class)
                .query("select e from MesTanliangji e where e.mesArea = :mesArea " +
                        "order by e.createdDate")
                .parameter("mesArea", mesArea)
                .list();
        List<MesZengguo> mesZengguos = dataManager.load(MesZengguo.class)
                .query("select e from MesZengguo e where e.mesArea = :mesArea " +
                        "order by e.createdDate")
                .parameter("mesArea", mesArea)
                .list();
        List<MesRunliangdou> runliangdous = new ArrayList<>();
        List<MesTanliangji> tanliangjis = new ArrayList<>();
        List<MesZengguo> zengguos = new ArrayList<>();
        for (MesRunliangdou runliangdou : mesRunliangdous) {
            for (MesArea area : mesAreas) {
                MesRunliangdou runliangdouNew = dataManager.create(MesRunliangdou.class);
                runliangdouNew.setMesArea(area);
                runliangdouNew.setRunliangdouCode(runliangdou.getRunliangdouCode());
                runliangdouNew.setRunliangdouNo(runliangdou.getRunliangdouNo());
                runliangdous.add(runliangdouNew);
            }
        }
        for(MesTanliangji tanliangji : mesTanliangjis){
            for(MesArea area : mesAreas){
                MesTanliangji tanliangjiNew = dataManager.create(MesTanliangji.class);
                tanliangjiNew.setMesArea(area);
                tanliangjiNew.setTanliangjiCode(tanliangji.getTanliangjiCode());
                tanliangjiNew.setTanliangjiNo(tanliangji.getTanliangjiNo());
                tanliangjis.add(tanliangjiNew);
            }
        }
        for(MesZengguo zengguo : mesZengguos){
            for(MesArea area : mesAreas){
                MesZengguo zengguoNew = dataManager.create(MesZengguo.class);
                zengguoNew.setMesArea(area);
                zengguoNew.setZengguoCode(zengguo.getZengguoCode());
                zengguoNew.setZengguoName(zengguo.getZengguoName());
                zengguoNew.setZengSequence(zengguo.getZengSequence());
                zengguos.add(zengguoNew);
            }
        }
        dataManager.unconstrained().save(new SaveContext().setDiscardSaved(true).saving(runliangdous,zengguos,tanliangjis));

    }
}
