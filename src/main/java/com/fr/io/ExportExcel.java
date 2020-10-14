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
import com.fr.io.exporter.ExcelExporter;
import com.fr.io.exporter.LargeDataPageExcelExporter;
import com.fr.io.exporter.PageExcel2007Exporter;
import com.fr.io.exporter.PageExcelExporter;
import com.fr.io.exporter.PageToSheetExcel2007Exporter;
import com.fr.io.exporter.PageToSheetExcelExporter;
import com.fr.io.exporter.excel.stream.StreamExcel2007Exporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.core.ReportUtils;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.WriteActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileOutputStream;


public class ExportExcel {
    public static void main(String[] args) {
        // Define the running env to read the database and execute reports.
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
        String envpath = "//Applications//FineReport10_325//webapps//webroot//WEB-INF";
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();


        ResultWorkBook rworkbook = null;
        try {
            // read the workbook
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook("//doc//Primary//Parameter//Parameter.cpt");
            // get the parameters and set value
            Parameter[] parameters = workbook.getParameters();
            parameters[0].setValue("华东");
            // define a parameter map to execute the workbook
            java.util.Map parameterMap = new java.util.HashMap();
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), parameters[i]
                        .getValue());
            }

            FileOutputStream outputStream;

            // unaltered export to xls
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//ExcelExport.xls"));
            ExcelExporter excel = new ExcelExporter();
            excel.setVersion(true);
            excel.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            // unaltered export to xlsx
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//ExcelExport.xlsx"));
            StreamExcel2007Exporter excel1 = new StreamExcel2007Exporter();
            excel.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            // full page export to xls
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//PageExcelExport.xls"));
            PageExcelExporter page = new PageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook.execute(parameterMap,new WriteActor())));
            page.setVersion(true);
            page.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            // full page export to xlsx
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//PageExcelExport.xlsx"));
            PageExcel2007Exporter page1 = new PageExcel2007Exporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook));
            page1.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            // page to sheet export to xls
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//PageSheetExcelExport.xls"));
            PageToSheetExcelExporter sheet = new PageToSheetExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook.execute(parameterMap,new WriteActor())));
            sheet.setVersion(true);
            sheet.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            // page to sheet export to xlsx
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//PageSheetExcelExport.xlsx"));
            PageToSheetExcel2007Exporter sheet1 = new PageToSheetExcel2007Exporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook));
            sheet1.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            // Large data volume export to xls
            outputStream = new FileOutputStream(new File("//Users//susie//Downloads//LargeExcelExport.zip"));
            LargeDataPageExcelExporter large = new LargeDataPageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(workbook.execute(parameterMap,new WriteActor())), true);
            // Large data volume export to xlsx
            // outputStream = new FileOutputStream(new File("//Users//susie//Downloads//LargeExcelExport.xlsx"));
            // LargeDataPageExcel2007Exporter large = new LargeDataPageExcel2007Exporter(ReportUtils.getPaperSettingListFromWorkBook(rworkbook), true);
            large.export(outputStream, workbook.execute(parameterMap,new WriteActor()));

            outputStream.close();
            module.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}