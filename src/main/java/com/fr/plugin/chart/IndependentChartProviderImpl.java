package com.fr.plugin.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.fun.impl.AbstractIndependentChartsProvider;
import com.fr.stable.StringUtils;

public class IndependentChartProviderImpl extends AbstractIndependentChartsProvider {
    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Chart[] getChartTypes() {
        return null;
    }

    @Override
    public String[] getRequiredJS() {
        return new String[0];
    }

    @Override
    public String getWrapperName() {
        return StringUtils.EMPTY;
    }

    @Override
    public int currentAPILevel() {
        return 0;
    }
}