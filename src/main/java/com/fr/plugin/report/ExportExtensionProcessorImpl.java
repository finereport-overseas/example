package com.fr.plugin.report;

import com.fr.io.collection.ExportCollection;
import com.fr.report.fun.impl.AbstractExportExtension;
import com.fr.web.core.ReportSessionIDInfor;
import com.fr.web.core.TemplateSessionIDInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportExtensionProcessorImpl extends AbstractExportExtension {
    @Override
    public String fileName(HttpServletRequest httpServletRequest, TemplateSessionIDInfo templateSessionIDInfo) throws Exception {
        return "";
    }

    @Override
    public ExportCollection createCollection(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ReportSessionIDInfor reportSessionIDInfor, String s, String s1, boolean b) throws Exception {
        return null;
    }
}