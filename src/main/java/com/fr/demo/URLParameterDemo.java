package com.fr.demo;
import java.util.Map;
import com.fr.main.TemplateWorkBook;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import com.fr.io.TemplateWorkBookIO;
import com.fr.base.Parameter;


@SuppressWarnings("unused")
public class URLParameterDemo extends Reportlet {
    public TemplateWorkBook createReport(ReportletRequest reportletRequest) {

        // get parameters from url
        TemplateWorkBook wbTpl = null;
        String countryValue = reportletRequest.getParameter("Region").toString();
        try {
            wbTpl = TemplateWorkBookIO.readTemplateWorkBook(
                    "//doc-EN//Primary//Parameter//Template_Parameter.cpt");
            // Get the parameter array. Since the template only has one parameter, we can get it at index 0. Then we put the new value to it.
            Parameter[] ps = wbTpl.getParameters();
            ps[0].setValue(countryValue);
            // We can remove the parameter pane.
            wbTpl.getReportParameterAttr().setParameterUI(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return wbTpl;
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