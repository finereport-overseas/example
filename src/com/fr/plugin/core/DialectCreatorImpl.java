package com.fr.plugin.core;

import com.fr.stable.UrlDriver;
import com.fr.stable.fun.impl.AbstractDialectCreator;

import java.sql.Connection;

public class DialectCreatorImpl extends AbstractDialectCreator {
    @Override
    public Class<?> generate(UrlDriver urlDriver) {
        return null;
    }

    @Override
    public Class<?> generate(Connection connection) {
        return null;
    }
}