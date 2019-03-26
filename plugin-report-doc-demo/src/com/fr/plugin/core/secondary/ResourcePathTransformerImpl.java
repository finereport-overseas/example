package com.fr.plugin.core.secondary;

import com.fr.stable.fun.impl.AbstractResourcePathTransformer;

/**
 * 资源路径接口
 */
public class ResourcePathTransformerImpl  extends AbstractResourcePathTransformer{
    @Override
    public boolean accept(String s) {
        return true;
    }

    @Override
    public String transform(String s) {
        return s;
    }
}