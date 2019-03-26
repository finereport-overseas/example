package com.fr.plugin.core.secondary;

import com.fr.stable.fun.impl.AbstractSiteTransformer;

/**
 * cloudcenter地址接口
 */
public class SiteTransformerImpl extends AbstractSiteTransformer{
    @Override
    public boolean match(String s) {
        return true;
    }

    @Override
    public String transform() {
        return "";
    }

    @Override
    public String transform(String s) {
        return s;
    }
}