package com.fr.io;

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
            // 连接数据库
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://review.finedevelop.com:3306/susie";
            String user = "root";
            String pass = "ilovejava";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement presmt = conn
                    .prepareStatement("insert into report values(?,?)");
            // 读进需要保存入库的模板文件
            // 首先需要定义执行所在的环境，这样才能正确读取数据库信息
            // 定义报表运行环境,用于执行报表
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
            String envpath = "//Applications//FineReport10_325//webapps//webroot//WEB-INF";//工程路径
            SimpleWork.checkIn(envpath);
            I18nResource.getInstance();
            module.start();

            File cptfile = new File("//doc//Primary//Parameter//Parameter.cpt");
            int lens = (int) cptfile.length();
            InputStream ins = new FileInputStream(cptfile);
            // 将模板保存入库
            presmt.setString(1, "Parameter.cpt"); // 第一个字段存放模板相对路径
            presmt.setBinaryStream(2, ins, lens); // 第二个字段存放模板文件的二进制流
            presmt.execute();
            conn.commit();
            presmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}