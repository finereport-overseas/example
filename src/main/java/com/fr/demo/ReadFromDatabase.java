package com.fr.demo;

import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import com.fr.log.FineLoggerFactory;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;



public class ReadFromDatabase extends Reportlet {
    public TemplateWorkBook createReport(ReportletRequest reportletRequest) {

        WorkBook workbook = new WorkBook();
        String name = reportletRequest.getParameter("cptname").toString();
        try {
            // define data connection (modify according to actual database info)
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://review.finedevelop.com:3306/susie";
            String user = "root";
            String pass = "ilovejava";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, pass);
            // read templates from database
            String sql = "select cpt from report where cptname = '" + name
                    + "'";
            Statement smt = conn.createStatement();
            ResultSet rs = smt.executeQuery(sql);
            while (rs.next()) {
                Blob blob = rs.getBlob(1); // read the value of the first column,which is cpt column
                FineLoggerFactory.getLogger().info(blob.toString());
                InputStream ins = blob.getBinaryStream();
                workbook.readStream(ins);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
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
