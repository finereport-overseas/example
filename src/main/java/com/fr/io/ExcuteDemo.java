package com.fr.io;

import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.io.exporter.ExcelExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.WriteActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileOutputStream;


public class ExcuteDemo {
    public static void main(String[] args) {
        try {
            // the execution environment needs to be defined beforehand in order to read the database correctly
            // define the environment to execute the report
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
            String envpath = "//Applications//FineReport10_325//webapps//webroot//WEB-INF";//env path
            SimpleWork.checkIn(envpath);
            I18nResource.getInstance();
            module.start();


            // read the template
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook("//doc//Primary//Parameter//Parameter.cpt");
            /*
             * generate parameter map and pass parameter and its corresponding value to execute the report.There is only one region parameter. Assign the value to be North China
             * if the parameter passed during sending the request,it can be obtained by req.getParameter(name)
             * put the parameter in the map，paraMap.put(paraname,paravalue)
             */
            java.util.Map paraMap = new java.util.HashMap();
            paraMap.put("Region", "North China");
            // use paraMap to execute and generate results
            ResultWorkBook result = workbook.execute(paraMap, new WriteActor());
            // use the result such as exporting to excel
            FileOutputStream outputStream = new FileOutputStream(new File(
                    "//Users//susie//Downloads//Parameter.xls"));
            ExcelExporter excelExporter = new ExcelExporter();
            excelExporter.export(outputStream,result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }
}
