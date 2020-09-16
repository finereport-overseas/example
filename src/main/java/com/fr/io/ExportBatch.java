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
import com.fr.stable.StableUtils;
import com.fr.stable.WriteActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;


public class ExportBatch {
    public static void main(String[] args) {
        try {
            // define environment to execute the report
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


            // read template file
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(
                    "//doc//Primary//Parameter//Parameter.cpt");
            File parafile = new File(envpath + "//para.txt");
            FileInputStream fileinputstream;
            fileinputstream = new FileInputStream(parafile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileinputstream));
            // define map that saves parameters to execute the report
            java.util.Map paramap = new java.util.HashMap();

            //read the first line and save the parameter name
            String lineText = bufferedReader.readLine();
            lineText = lineText.trim();
            String[] paraname = StableUtils.splitString(lineText, ",");
            System.out.println(Arrays.toString(paraname));
            // traverse every parameter combinations, execute the template and export results
            int number = 0;
            while ((lineText = bufferedReader.readLine()) != null) {
                lineText = lineText.trim();
                String[] paravalue = StableUtils.splitString(lineText, ",");
                for (int j = 0; j < paravalue.length; j++) {
                    paramap.put(paraname[j], paravalue[j]);
                }
                ResultWorkBook result = workbook.execute(paramap,new WriteActor());
                OutputStream outputstream = new FileOutputStream(new File("//Users//susie//Downloads//ExportEg" + number + ".xls"));
                ExcelExporter excelexporter = new ExcelExporter();
                excelexporter.export(outputstream, result);
                //  clear the parameter map for future use
                paramap.clear();
                number++;
                outputstream.close();
            }
            module.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
