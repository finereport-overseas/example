package com.fr.demo;

import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.excel.stream.StreamExcel2007Exporter;
import com.fr.log.FineLoggerFactory;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.worksheet.WorkSheet;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.stable.WriteActor;
import com.fr.store.StateServerActivator;
import com.fr.workspace.simple.SimpleWork;
import com.fr.write.web.excel.ExcelImportPlusProcessor;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author zack
 * @version 10.0
 * @date 2019/8/27
 */
public class ExportApiTest extends TestCase {
    public void testExport() {
        Module module = ActivatorToolBox.simpleLink(new BaseDBActivator(),
                new ConfigurationActivator(),
                new StandaloneModeActivator(),
                new StateServerActivator(),
                new ChartBaseActivator(),
                new SchedulerActivator(),
                new ReportBaseActivator(),
                new RestrictionActivator(),
                new ReportActivator(),
                new WriteActivator());
        SimpleWork.supply(CommonOperator.class, new CommonOperatorImpl());
        SimpleWork.checkIn(ExportApiTest.class.getResource("../../../WEB-INF").getPath());
        I18nResource.getInstance();
        module.start();


        ResultWorkBook rworkbook = null;
        try {
            // 未执行模板工作薄
            WorkBook workbook = (WorkBook) TemplateWorkBookIO
                    .readTemplateWorkBook("//sumtest.cpt");
            // 获取报表参数并设置值，导出内置数据集时数据集会根据参数值查询出结果从而转为内置数据集
            // 定义parametermap用于执行报表，将执行后的结果工作薄保存为rworkBook
            java.util.Map parameterMap = new java.util.HashMap();
            // 定义输出流
            ByteArrayOutputStream outputStream;
            // 将结果工作薄导出为Excel文件
            outputStream = new ByteArrayOutputStream();
            StreamExcel2007Exporter ExcelExport1 = new StreamExcel2007Exporter();
            ExcelExport1.export(outputStream, workbook.execute(parameterMap, new WriteActor()));
            outputStream.close();
            InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
            TemplateWorkBook workBook = new ExcelImportPlusProcessor().generateWorkBookByStream(in, "111.xlsx", new HashMap());
            Assert.assertEquals(6, ((WorkSheet) workBook.getReport(0)).getCellElement(0, 0).getValue());
            Assert.assertEquals(3, ((WorkSheet) workBook.getReport(0)).getCellElement(1, 0).getValue());
            module.stop();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            SimpleWork.checkOut();
        }
    }
}