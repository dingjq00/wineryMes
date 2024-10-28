package com.mom.winery.view.meswinccitemconfig;

import com.mom.winery.entity.MesWinccItemConfig;
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
 * @Date 2024/10/26 12:16
 */

@Route(value = "mesWinccItemConfigs", layout = MainView.class)
@ViewController("MesWinccItemConfig.list")
@ViewDescriptor("mes-wincc-item-config-list-view.xml")
@LookupComponent("mesWinccItemConfigsDataGrid")
@DialogMode(width = "64em")
public class MesWinccItemConfigListView extends StandardListView<MesWinccItemConfig> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<MesWinccItemConfig> mesWinccItemConfigsDc;

    @ViewComponent
    private InstanceContainer<MesWinccItemConfig> mesWinccItemConfigDc;

    @ViewComponent
    private InstanceLoader<MesWinccItemConfig> mesWinccItemConfigDl;

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

    @Subscribe("mesWinccItemConfigsDataGrid.create")
    public void onMesWinccItemConfigsDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        MesWinccItemConfig entity = dataContext.create(MesWinccItemConfig.class);
        mesWinccItemConfigDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("mesWinccItemConfigsDataGrid.edit")
    public void onMesWinccItemConfigsDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveBtn")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        MesWinccItemConfig item = mesWinccItemConfigDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        mesWinccItemConfigsDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelBtn")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        mesWinccItemConfigDl.load();
        updateControls(false);
    }

    @Subscribe(id = "mesWinccItemConfigsDc", target = Target.DATA_CONTAINER)
    public void onMesWinccItemConfigsDcItemChange(final InstanceContainer.ItemChangeEvent<MesWinccItemConfig> event) {
        MesWinccItemConfig entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            mesWinccItemConfigDl.setEntityId(entity.getId());
            mesWinccItemConfigDl.load();
        } else {
            mesWinccItemConfigDl.setEntityId(null);
            mesWinccItemConfigDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(MesWinccItemConfig entity) {
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
