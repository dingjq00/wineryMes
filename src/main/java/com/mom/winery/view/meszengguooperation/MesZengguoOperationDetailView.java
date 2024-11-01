package com.mom.winery.view.meszengguooperation;

import com.mom.winery.entity.MesZengguo;
import com.mom.winery.entity.MesZengguoOperation;
import com.mom.winery.entity.MesZengguoRecord;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/30 16:16
 */

@Route(value = "mesZengguoOperations/:id", layout = MainView.class)
@ViewController("MesZengguoOperation.detail")
@ViewDescriptor("mes-zengguo-operation-detail-view.xml")
@EditedEntityContainer("mesZengguoOperationDc")
public class MesZengguoOperationDetailView extends StandardDetailView<MesZengguoOperation> {
    @ViewComponent
    private CollectionLoader<MesZengguoRecord> mesZengguoRecordsDl;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        MesZengguoOperation operation =  getEditedEntity();
        MesZengguo mesZengguo = operation.getMesZengguo();
        Map<String,Object> mesZengguoRecordsDlParams = new HashMap<>();
        mesZengguoRecordsDlParams.put("mesZengguo1", mesZengguo);
        mesZengguoRecordsDlParams.put("phaseStartWinccId1", operation.getPhaseStartWinccId());
//        mesZengguoRecordsDlParams.put("phaseEndWinccId2",operation.getPhaseEndWinccId());
        mesZengguoRecordsDlParams.put("zengSequence1", operation.getZengSequence());
        if(operation.getPhaseEndWinccId() != null){
            mesZengguoRecordsDlParams.put("phaseEndWinccId2", operation.getPhaseEndWinccId());
        }else {
            mesZengguoRecordsDlParams.put("phaseEndWinccId2", 999999999);
        }
        mesZengguoRecordsDl.setParameters(mesZengguoRecordsDlParams);
        mesZengguoRecordsDl.load();
    }

}
