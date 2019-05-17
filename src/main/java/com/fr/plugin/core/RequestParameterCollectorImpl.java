package com.fr.plugin.core;

import com.fr.stable.fun.impl.AbstractRequestParameterCollector;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RequestParameterCollectorImpl extends AbstractRequestParameterCollector {
    @Override
    public Map<String, Object> getParametersFromSession(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public Map<String, Object> getParametersFromAttribute(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public Map<String, Object> getParametersFromReqInputStream(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public Map<String, Object> getParametersFromParameter(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public Map<String, Object> getParametersFromJSON(HttpServletRequest httpServletRequest, Map<String, Object> map) {
        return null;
    }
}