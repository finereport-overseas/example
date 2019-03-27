package com.fr.plugin.core;

import com.fr.intelli.record.Focus;
import com.fr.intelli.record.Original;
import com.fr.record.analyzer.EnableMetrics;
import com.fr.stable.fun.impl.AbstractLocaleFinder;

@EnableMetrics
public class MyLocaleFinder extends AbstractLocaleFinder  {
    @Override
    @Focus(id = "com.fr.plugin.function", text = "插件全家桶", source = Original.PLUGIN)
    public String find() {
        return "com/fr/plugin/demo";
    }
}
