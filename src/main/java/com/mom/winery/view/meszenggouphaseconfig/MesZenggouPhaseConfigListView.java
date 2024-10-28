package com.mom.winery.view.meszenggouphaseconfig;

import com.mom.winery.entity.MesZenggouPhaseConfig;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/27 13:04
 */

@Route(value = "mesZenggouPhaseConfigs", layout = MainView.class)
@ViewController("MesZenggouPhaseConfig.list")
@ViewDescriptor("mes-zenggou-phase-config-list-view.xml")
@LookupComponent("mesZenggouPhaseConfigsDataGrid")
@DialogMode(width = "64em")
public class MesZenggouPhaseConfigListView extends StandardListView<MesZenggouPhaseConfig> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<MesZenggouPhaseConfig> mesZenggouPhaseConfigsDc;

    @ViewComponent
    private InstanceContainer<MesZenggouPhaseConfig> mesZenggouPhaseConfigDc;

    @ViewComponent
    private InstanceLoader<MesZenggouPhaseConfig> mesZenggouPhaseConfigDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("mesZenggouPhaseConfigsDataGrid.create")
    public void onMesZenggouPhaseConfigsDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        MesZenggouPhaseConfig entity = dataContext.create(MesZenggouPhaseConfig.class);
        mesZenggouPhaseConfigDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("mesZenggouPhaseConfigsDataGrid.edit")
    public void onMesZenggouPhaseConfigsDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveBtn")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        MesZenggouPhaseConfig item = mesZenggouPhaseConfigDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        mesZenggouPhaseConfigsDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelBtn")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        mesZenggouPhaseConfigDl.load();
        updateControls(false);
    }

    @Subscribe(id = "mesZenggouPhaseConfigsDc", target = Target.DATA_CONTAINER)
    public void onMesZenggouPhaseConfigsDcItemChange(final InstanceContainer.ItemChangeEvent<MesZenggouPhaseConfig> event) {
        MesZenggouPhaseConfig entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            mesZenggouPhaseConfigDl.setEntityId(entity.getId());
            mesZenggouPhaseConfigDl.load();
        } else {
            mesZenggouPhaseConfigDl.setEntityId(null);
            mesZenggouPhaseConfigDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(MesZenggouPhaseConfig entity) {
        ViewValidation viewValidation = getViewValidation();
        ValidationErrors validationErrors = viewValidation.validateUiComponents(form);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        validationErrors.addAll(viewValidation.validateBeanGroup(UiCrossFieldChecks.class, entity));
        return validationErrors;
    }

    private void updateControls(boolean editing) {
        UiComponentUtils.getComponents(form).forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }
}
