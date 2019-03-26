package com.fr.plugin.design;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractSubmitProvider;

public class SubmitProviderImpl extends AbstractSubmitProvider {
    @Override
    public BasicBeanPane appearanceForSubmit() {
        return null;
    }

    @Override
    public String dataForSubmit() {
        return "";
    }

    @Override
    public String keyForSubmit() {
        return "";
    }
}