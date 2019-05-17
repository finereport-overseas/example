package com.fr.plugin.design;

import com.fr.base.TableData;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.fun.impl.AbstractTableDataDefineProvider;

public class TableDataDefineProviderImpl extends AbstractTableDataDefineProvider {
    @Override
    public Class<? extends TableData> classForTableData() {
        return null;
    }

    @Override
    public Class<? extends TableData> classForInitTableData() {
        return null;
    }

    @Override
    public Class<? extends AbstractTableDataPane> appearanceForTableData() {
        return null;
    }

    @Override
    public String nameForTableData() {
        return "";
    }

    @Override
    public String prefixForTableData() {
        return "";
    }

    @Override
    public String iconPathForTableData() {
        return "";
    }
}