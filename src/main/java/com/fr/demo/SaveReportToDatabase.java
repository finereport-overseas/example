package com.fr.demo;

import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SaveReportToDatabase {
    public static void main(String[] args) {
        SaveReport();
    }

    private static void SaveReport() {
        try {
            Module module = ActivatorToolBox.simpleLink(new BaseDBActivator(),
                    new ConfigurationActivator(),
                    new StandaloneModeActivator(),
                    new ModuleHealActivator(),
                    new StateServiceActivator(),
                    new ChartBaseActivator(),
                    new SchedulerActivator(),
                    new ReportBaseActivator(),
                    new RestrictionActivator(),
                    new ReportActivator(),
                    new WriteActivator());
            SimpleWork.supply(CommonOperator.class, new CommonOperatorImpl());
            String envpath = "D:\\FineReport_10.0\\webapps\\webroot\\WEB-INF";
            SimpleWork.checkIn(envpath);
            I18nResource.getInstance();
            module.start();


            // connect to the database
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/joe";
            String user = "joe";
            String pass = "123456";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);
            PreparedStatement presmt = conn
                    .prepareStatement("INSERT INTO report VALUES(?,?)");

            // read the template file to be saved to database
            File cptfile = new File(envpath
                    + "\\reportlets\\GettingStarted.cpt");
            int lens = (int) cptfile.length();
            InputStream ins = new FileInputStream(cptfile);
            // save the template to the database
            presmt.setString(1, "GettingStarted.cpt"); // place relative path of the template in Field 1
            presmt.setBinaryStream(2, ins, lens); // place byte stream of the file in Field 2
            presmt.execute();
            conn.commit();
            presmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }
}