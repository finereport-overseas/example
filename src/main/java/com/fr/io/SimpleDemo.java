package com.fr.io;

import com.fr.base.Style;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.FRFont;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.main.impl.WorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

public class SimpleDemo {
    public static void main(String[] args) {
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


        try {
            WorkBook workbook = (WorkBook) TemplateWorkBookIO
                    .readTemplateWorkBook(
                            "//doc//Primary//Parameter//Parameter.cpt");

            // get the worksheet in the workbook, modify the front background to be red
            TemplateElementCase report = (TemplateElementCase) workbook
                    .getReport(0);
            // getCellElement(int column, int
            // row),column和row both start from 0，thus A1 ia Column 0, Row 0
            CellElement cellA1 = report.getCellElement(0, 0);
            FRFont frFont = FRFont.getInstance();
            frFont = frFont.applyForeground(Color.red);
            Style style = Style.getInstance();
            style = style.deriveFRFont(frFont);
            cellA1.setStyle(style);
            // save the template
            FileOutputStream outputStream = new FileOutputStream(new File(
                    "/Users//susie//Downloads//newParameter1.cpt"));
            ((WorkBook) workbook).export(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }
}
