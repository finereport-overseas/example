package com.fr.plugin.design;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractCellWidgetOptionProvider;
import com.fr.form.ui.Widget;

public class CellWidgetOptionProviderImpl extends AbstractCellWidgetOptionProvider {
    @Override
    public Class<? extends Widget> classForWidget() {
        return null;
    }

    @Override
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget() {
        return null;
    }

    @Override
    public String iconPathForWidget() {
        return "";
    }

    @Override
    public String nameForWidget() {
        return "";
    }
}