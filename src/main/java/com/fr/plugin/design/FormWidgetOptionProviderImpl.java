package com.fr.plugin.design;

import com.fr.design.fun.impl.AbstractFormWidgetOptionProvider;
import com.fr.form.ui.Widget;

public class FormWidgetOptionProviderImpl extends AbstractFormWidgetOptionProvider {
    @Override
    public Class<? extends Widget> classForWidget() {
        return null;
    }

    @Override
    public Class<?> appearanceForWidget() {
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