package com.fr.plugin.core;

import com.fr.web.core.ActionCMD;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionCMDImpl implements ActionCMD {
    @Override
    public String getCMD() {
        return "";
    }

    @Override
    public void actionCMD(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) throws Exception {

    }

    @Override
    public void actionCMD(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

    }
}