package com.mom.winery.view.winccgeiliaoji;

import com.mom.winery.entity.WinccGeiliaoji;
import com.mom.winery.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


/**
 * @Descriptrion: TODO(描述该类作用)
 * @Author dingjq
 * @Date 2024/10/25 10:35
 */

@Route(value = "winccGeiliaojis", layout = MainView.class)
@ViewController("WinccGeiliaoji.list")
@ViewDescriptor("wincc-geiliaoji-list-view.xml")
@LookupComponent("winccGeiliaojisDataGrid")
@DialogMode(width = "64em")
public class WinccGeiliaojiListView extends StandardListView<WinccGeiliaoji> {
}
