package com.mom.winery.view.meszengguo;

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
 * @Date 2024/10/25 14:36
 */

@Route(value = "mesZengguos", layout = MainView.class)
@ViewController("MesZengguo.list")
@ViewDescriptor("mes-zengguo-list-view.xml")
@LookupComponent("mesZengguosDataGrid")
@DialogMode(width = "64em")
public class MesZengguoListView extends StandardListView<MesZengguo> {
    @Autowired
    private DataManager dataManager;

    @Subscribe(id = "changeName", subject = "clickListener")
    public void onChangeNameClick(final ClickEvent<JmixButton> event) {
        List<MesZengguo> zengguoList = dataManager.load(MesZengguo.class)
                .query("select e from MesZengguo e" )
                .list();
        for (MesZengguo mesZengguo : zengguoList) {
            Integer code = mesZengguo.getZengguoCode();
            String codeStr = code.toString();
            if(code < 10){
                codeStr = "0" + codeStr;
                String newName = mesZengguo.getMesArea().getAreaName() + codeStr+ "#";
                mesZengguo.setZengguoName(newName);
            }
        }
        dataManager.save(new SaveContext().saving(zengguoList));
    }
}
