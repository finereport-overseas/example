package com.fr.demo;

import com.fr.form.main.Form;
import com.fr.form.main.FormIO;
import com.fr.web.weblet.Formlet;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SimpleFormletDemo extends Formlet {
    @Override
    protected Form createForm(HttpServletRequest httpServletRequest) throws Exception {
        Form form = null;
        try {
            form = FormIO.readForm("//doc-EN//Dashboard//First_Dashboard.frm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return form;
    }

    @Override
    public void setParameterMap(Map<String, Object> map) {

    }
}
