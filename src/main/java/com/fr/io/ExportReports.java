package com.fr.io;

import com.fr.base.Parameter;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.io.exporter.PageExcelExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.PageWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.core.ReportUtils;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.report.PageReport;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.PageActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ExportReports {
    public static void main(String[] args) {
        // first define the environment to read the database info correctly
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


        // some essential initialization
        try {
            //workbook to be executed
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(
                    "Gettingstarted.cpt");
            // get the result where parameter value is East China，save the result to rworkbook
            Parameter[] parameters = workbook.getParameters();
            java.util.Map parameterMap = new java.util.HashMap();
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), "East China");
            }
            PageWorkBook rworkbook = (PageWorkBook)workbook.execute(parameterMap,new PageActor());
            rworkbook.setReportName(0, "East China");
            // clear parametermap，change the parameter value to North China and get ResultReport
            parameterMap.clear();
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), "North China");
            }
            PageWorkBook rworkbook2 = (PageWorkBook)workbook.execute(parameterMap,new PageActor());
            PageReport rreport2 = rworkbook2.getPageReport(0);
            rworkbook.addReport("North China", rreport2);
            //Export Resultbook as Excel
            OutputStream outputStream = new FileOutputStream(new File("//Users//susie//Downloads//ExcelExport1.xls"));
            PageExcelExporter excelExport = new PageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook));
            excelExport.export(outputStream, rworkbook);
            outputStream.close();
            module.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
