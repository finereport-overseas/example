package com.fr.plugin.chart;

import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.extended.chart.AbstractExtendedChartTableDataPane;
import com.fr.extended.chart.AbstractExtendedChartUIProvider;

public class IndependentChartUIProviderImpl extends AbstractExtendedChartUIProvider {
    @Override
    protected AbstractExtendedChartTableDataPane getTableDataSourcePane() {
        return null;
    }

    @Override
    protected AbstractReportDataContentPane getReportDataSourcePane() {
        return null;
    }

    @Override
    public String getIconPath() {
        return "";
    }
}