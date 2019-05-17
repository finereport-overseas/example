package com.fr.plugin.report;

import com.fr.main.TemplateWorkBook;
import com.fr.report.fun.impl.AbstractExcelImportProcessor;

import java.io.InputStream;
import java.util.Map;

public class ExcelImportProcessor extends AbstractExcelImportProcessor {
    @Override
    public TemplateWorkBook generateWorkBookByStream(InputStream inputStream, String s, Map<String, Object> map) throws Exception {
        return null;
    }
}