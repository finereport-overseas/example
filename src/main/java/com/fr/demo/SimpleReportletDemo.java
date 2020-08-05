//程序网络报表
package com.fr.demo;

import com.fr.io.TemplateWorkBookIO;
import com.fr.main.TemplateWorkBook;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import java.util.Map;


public class SimpleReportletDemo extends Reportlet {
    public TemplateWorkBook createReport(ReportletRequest reportletrequest) {
        // Create a new WorkBook.
        TemplateWorkBook WorkBook = null;
        try {
            // Read the template from the path and store it as a Workbook.
            WorkBook = TemplateWorkBookIO.readTemplateWorkBook("//doc-EN//Primary//Parameter//Parameter_Reference.cpt");
        } catch (Exception e) {
            e.getStackTrace();
        }
        return WorkBook;
    }

    @Override
    public void setParameterMap(Map arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTplPath(String arg0) {
        // TODO Auto-generated method stub

    }
}