package com.fr.plugin.design;

import com.fr.data.impl.Connection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractConnectionProvider;

public class ConnectionProviderImpl extends AbstractConnectionProvider {
    @Override
    public String nameForConnection() {
        return "";
    }

    @Override
    public String iconPathForConnection() {
        return "";
    }

    @Override
    public Class<? extends Connection> classForConnection() {
        return null;

    }

    @Override
    public Class<? extends BasicBeanPane<? extends Connection>> appearanceForConnection() {
        return null;
    }
}