package com.mom.winery.view.winccgeiliaoji;

import com.mom.winery.entity.WinccGeiliaoji;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 10:35
 */

@Route(value = "winccGeiliaojis/:id", layout = MainView.class)
@ViewController("WinccGeiliaoji.detail")
@ViewDescriptor("wincc-geiliaoji-detail-view.xml")
@EditedEntityContainer("winccGeiliaojiDc")
public class WinccGeiliaojiDetailView extends StandardDetailView<WinccGeiliaoji> {
}
