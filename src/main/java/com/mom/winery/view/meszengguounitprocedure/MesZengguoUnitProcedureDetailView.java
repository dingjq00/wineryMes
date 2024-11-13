package com.mom.winery.view.meszengguounitprocedure;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.entity.MesZengguoOperation;
import com.mom.winery.entity.MesZengguoUnitProcedure;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 18:13
 */

@Route(value = "mesZengguoUnitProcedures/:id", layout = MainView.class)
@ViewController("MesZengguoUnitProcedure.detail")
@ViewDescriptor("mes-zengguo-unit-procedure-detail-view.xml")
@EditedEntityContainer("mesZengguoUnitProcedureDc")
public class MesZengguoUnitProcedureDetailView extends StandardDetailView<MesZengguoUnitProcedure> {
    @ViewComponent
    private CollectionLoader<MesZengguoOperation> mesZengguoOperationsDl;


    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        MesZengguoUnitProcedure unitProcedure =  getEditedEntity();
        MesZengguo mesZengguo = unitProcedure.getMesZengguo();
        Map<String,Object> mesZengguoOperationsDlParams = new HashMap<>();
        mesZengguoOperationsDlParams.put("mesZengguo1", mesZengguo);
        mesZengguoOperationsDlParams.put("phaseStartWinccId1", unitProcedure.getPhaseStartWinccId());
        mesZengguoOperationsDlParams.put("zengSequence1",unitProcedure.getZengSequence());
        if(unitProcedure.getPhaseEndWinccId() != null){
            mesZengguoOperationsDlParams.put("phaseEndWinccId1", unitProcedure.getPhaseEndWinccId());
        }else {
            mesZengguoOperationsDlParams.put("phaseEndWinccId1", 999999999);
        }
        mesZengguoOperationsDl.setParameters(mesZengguoOperationsDlParams);
        mesZengguoOperationsDl.load();
    }
}
