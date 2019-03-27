package com.fr.plugin.core;

import com.fr.stable.fun.impl.AbstractAttachmentDownloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AttachmentDownloaderImpl extends AbstractAttachmentDownloader {
    @Override
    public void download(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s, String[] strings) throws Exception {

    }

    @Override
    public String createDownloadScript(String s) {
        return "";
    }
}