package com.fr.io;

import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.general.log.Log4jConfig;
import com.fr.general.log.parser.ExtraPatternParserManager;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.*;
import com.fr.io.exporter.excel.stream.StreamExcel2007Exporter;
import com.fr.main.impl.WorkBook;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.WriteActor;
import com.fr.stable.resource.ResourceLoader;
import com.fr.store.StateServiceActivator;
import com.fr.third.apache.log4j.Level;
import com.fr.third.apache.log4j.PropertyConfigurator;
import com.fr.workspace.simple.SimpleWork;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ExportApi {

    public static void main(String[] args) {

        initLog4j();

        /**定义报表运行环境,用于执行报表*/
        com.fr.module.Module module = ActivatorToolBox.simpleLink(new BaseDBActivator(),
                new ConfigurationActivator(),
                new StandaloneModeActivator(),
                new ModuleHealActivator(),
                //2020.4.26jar包之前的版本，替换成StateServerActivator()
                new StateServiceActivator(),
                new SchedulerActivator(),
                new ReportBaseActivator(),
                new RestrictionActivator(),
                new ReportActivator(),
                new WriteActivator(),
                new ChartBaseActivator());
        SimpleWork.supply(CommonOperator.class, new CommonOperatorImpl());
        //定义工程路径
        String envpath = "D:\\javatools\\FineReport_10.0\\webapps\\webroot\\WEB-INF\\";
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();

        /**输出模板*/
        try {
            // 定义输出的模板路径，以reportlets为根目录
            WorkBook workbook = (WorkBook) TemplateWorkBookIO.readTemplateWorkBook("GettingStarted.cpt");
            // 设置传入的参数
            Map parameterMap = new HashMap();
            parameterMap.put("地区","华东");

            // 定义输出流
            FileOutputStream outputStream;
            String outputUrl="F:\\io\\WorkBook\\";

            /**将模板工作薄导出为内置数据集模板文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"EmbExport.cpt"));
            EmbeddedTableDataExporter templateExporter = new EmbeddedTableDataExporter();
            templateExporter.export(outputStream, workbook);

            /**将模板工作薄导出为模板文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"TmpExport.cpt"));
            ((WorkBook) workbook).export(outputStream);

            /**将结果工作薄导出为2003Excel文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"ExcelExport2003.xls"));
            ExcelExporter ExcelExport = new ExcelExporter();
            ExcelExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为2007Excel文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"ExcelExport2007.xlsx"));
            StreamExcel2007Exporter ExcelExport1 = new StreamExcel2007Exporter();
            ExcelExport1.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为Word文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"WordExport.doc"));
            WordExporter WordExport = new WordExporter();
            WordExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为Pdf文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"PdfExport.pdf"));
            PDFExporter PdfExport = new PDFExporter();
            PdfExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为Txt文件（txt文件本身不支持表格、图表等，被导出模板一般为明细表）*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"TxtExport.txt"));
            TextExporter TxtExport = new TextExporter();
            TxtExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为Csv文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"CsvExport.csv"));
            CSVExporter CsvExport = new CSVExporter();
            CsvExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为SVG文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"SvgExport.svg"));
            SVGExporter SvgExport = new SVGExporter();
            SvgExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            /**将结果工作薄导出为image文件*/
            outputStream = new FileOutputStream(new java.io.File(outputUrl+"PngExport.png"));
            ImageExporter ImageExport = new ImageExporter();
            ImageExport.export(outputStream, workbook.execute(parameterMap, new WriteActor()));

            outputStream.close();
            module.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }

    private static void initLog4j() {

        PropertyConfigurator.configure(loadLog4jPropertiesFromJar(Level.toLevel("INFO")));
    }

    private static Properties loadLog4jPropertiesFromJar(Level level) {

        Properties properties = new Properties();

        System.setProperty("LOG_HOME", System.getProperty("user.dir"));
        System.setProperty("LOG_ROOT_LEVEL", level.toString());
        ExtraPatternParserManager.setSystemProperty();
        try {
            properties.load(ResourceLoader.getResourceAsStream("/com/fr/general/log/log4j.properties", Log4jConfig.class));
        } catch (IOException ignore) {
            //do nothing
        }

        return properties;
    }
}