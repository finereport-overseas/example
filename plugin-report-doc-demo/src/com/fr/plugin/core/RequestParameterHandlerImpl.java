package com.fr.plugin.core;

import com.fr.stable.fun.impl.AbstractRequestParameterHandler;

import javax.servlet.http.HttpServletRequest;

public class RequestParameterHandlerImpl extends AbstractRequestParameterHandler {
    @Override
    public Object getParameterFromRequest(HttpServletRequest httpServletRequest, String s) {
        return null;
    }

    @Override
    public Object getParameterFromRequestInputStream(HttpServletRequest httpServletRequest, String s) {
        return null;
    }

    @Override
    public Object getParameterFromAttribute(HttpServletRequest httpServletRequest, String s) {
        return null;
    }

    @Override
    public Object getParameterFromJSONParameters(HttpServletRequest httpServletRequest, String s) {
        return null;
    }

    @Override
    public Object getParameterFromSession(HttpServletRequest httpServletRequest, String s) {
        return null;
    }
}