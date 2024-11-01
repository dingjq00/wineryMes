package com.mom.winery.view.meszengguounitprocedure;

import com.mom.winery.entity.MesZengguoUnitProcedure;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 18:13
 */

@Route(value = "mesZengguoUnitProcedures", layout = MainView.class)
@ViewController("MesZengguoUnitProcedure.list")
@ViewDescriptor("mes-zengguo-unit-procedure-list-view.xml")
@LookupComponent("mesZengguoUnitProceduresDataGrid")
@DialogMode(width = "64em")
public class MesZengguoUnitProcedureListView extends StandardListView<MesZengguoUnitProcedure> {
    @ViewComponent
    private DataGrid<MesZengguoUnitProcedure> mesZengguoUnitProceduresDataGrid;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DialogWindows dialogWindows;

    @Subscribe(id = "multiZengguoAnalysisi", subject = "clickListener")
    public void onMultiZengguoAnalysisiClick(final ClickEvent<JmixButton> event) {
        List<MesZengguoUnitProcedure> mesZengguoUnitProcedureList =  mesZengguoUnitProceduresDataGrid.getSelectedItems().stream().toList();
        if(mesZengguoUnitProcedureList.isEmpty()){
            notifications.create("请选择需要分析的甑锅执行记录", "未选择需要分析的甑锅执行记录").show();
            return;
        }
        List<Long> unitProcedureIds = mesZengguoUnitProcedureList.stream().map(MesZengguoUnitProcedure::getId).toList();
        DialogWindow<MesZengguoMultiAnalysisView> window = dialogWindows.view(this, MesZengguoMultiAnalysisView.class).build();
//        window.getView().setDc(productionOrderMaterialPreparerationDTOS);
        window.getView().setUnitProcedureIds(unitProcedureIds);
        window.open();

    }
}
