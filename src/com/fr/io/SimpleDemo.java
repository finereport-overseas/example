//��ȡ�޸ı���
package com.fr.io;

import com.fr.base.Style;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.data.impl.config.activator.RestrictionActivator;
import com.fr.general.FRFont;
import com.fr.main.impl.WorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.cell.CellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.module.ReportBaseActivator;
import com.fr.serialization.SerializationActivator;
import com.fr.startup.WorkspaceRegister;
import com.fr.store.StateServerActivator;
import com.fr.workspace.engine.WorkspaceActivator;
import com.fr.workspace.server.ServerWorkspaceRegister;
import com.fr.workspace.simple.SimpleWork;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

public class SimpleDemo {
    public static void main(String[] args) {
        // 定义报表运行环境,用于执行报表
        Module module = ActivatorToolBox.simpleLink(
                new WorkspaceActivator(),
                new BaseDBActivator(),
                new ConfigurationActivator(),
                new StateServerActivator(),
                new ReportBaseActivator(),
                new RestrictionActivator(),
                new ReportActivator(),
                new WorkspaceRegister(),
                new ServerWorkspaceRegister(),
                new SerializationActivator());
        String envpath = "D:\\FineReport_10\\webapps\\webroot\\WEB-INF";//工程路径
        SimpleWork.checkIn(envpath);
        module.start();
        try {
            // ��ȡģ��  
            WorkBook workbook = (WorkBook) TemplateWorkBookIO
                    .readTemplateWorkBook(
                            "\\doc\\Primary\\Parameter\\Parameter.cpt");

            // ���WorkBook�е�WorkSheet�������޸�A1��Ԫ���ǰ��ɫΪ��ɫ  
            TemplateElementCase report = (TemplateElementCase) workbook
                    .getReport(0);
            // getCellElement(int column, int  
            // row),column��row����0��ʼ�����A1��Ԫ����ǵ�0�е�0��  
            CellElement cellA1 = report.getCellElement(0, 0);
            FRFont frFont = FRFont.getInstance();
            frFont = frFont.applyForeground(Color.red);
            Style style = Style.getInstance();
            style = style.deriveFRFont(frFont);
            cellA1.setStyle(style);
            // ����ģ��  
            FileOutputStream outputStream = new FileOutputStream(new File(
                    "D:\\newParameter1.cpt"));
            ((WorkBook) workbook).export(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }
}