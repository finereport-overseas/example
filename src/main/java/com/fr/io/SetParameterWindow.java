package com.fr.io;

import com.fr.base.background.ColorBackground;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.Background;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

public class SetParameterWindow {
    public static void main(String[] args) {
        try {
            // Define the running environment for FineReport
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
            // The path to your FineReport
            String envpath = "D:\\OtherProjects\\example\\webroot\\WEB-INF";
            SimpleWork.checkIn(envpath);
            I18nResource.getInstance();
            module.start();


            WorkBook workbook = (WorkBook) TemplateWorkBookIO
                    .readTemplateWorkBook(
                            "/doc-EN/Primary/Parameter/Template_Parameter.cpt");
            // Get the parameter attribute of workbook. 
            ReportParameterAttr paraAttr = workbook.getReportParameterAttr();

            /* The Alignment of the parameter pane.
             * 0 : left
             * 1 ：center
             * 2 ：right
             */
            paraAttr.setAlign(1);

            // Set the background of parameter pane. Besides ColorBackground, there are GradientBackground, ImageBackground, PatternBackground and TextureBackground.
            Background background = ColorBackground.getInstance(new Color(0, 255, 255));
            paraAttr.setBackground(background);

            // Set the parameter attribute and export the new workbook.
            workbook.setReportParameterAttr(paraAttr);
            FileOutputStream outputStream = new FileOutputStream(new File(
                    "D:\\OtherProjects\\example\\webroot\\WEB-INF\\reportlets\\Template_Parameter_New.cpt"));
            EmbeddedTableDataExporter templateExporter = new EmbeddedTableDataExporter();
            templateExporter.export(outputStream, workbook);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }
}