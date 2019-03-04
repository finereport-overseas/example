// �����������API 
package com.fr.io;

import com.fr.base.background.ColorBackground;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.data.impl.config.activator.RestrictionActivator;
import com.fr.general.Background;
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
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

public class SetParameterWindow {
    public static void main(String[] args) {
        try {
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
            WorkBook workbook = (WorkBook) TemplateWorkBookIO
                    .readTemplateWorkBook(
                            "\\doc\\Primary\\Parameter\\Parameter.cpt");
            // ��ȡWorkBook�������Ĳ�������ReportParameterAttr
            ReportParameterAttr paraAttr = workbook.getReportParameterAttr();
            /* ��������Ĳ���
             * 0 : ����
             * 1 ������
             * 2 �� ����
             */
            paraAttr.setAlign(1);
            /*
             * ���ò������汳��
             * ColorBackground ����ɫ����
             * GradientBackground ������ɫ����
             * ImageBackground ��ͼƬ����
             * PatternBackground ��ͼ������
             * TextureBackground ��������
             */
            Background background = ColorBackground.getInstance(new Color(0, 255, 255));
            paraAttr.setBackground(background);
            // �������ò�������,�������ս��
            workbook.setReportParameterAttr(paraAttr);
            FileOutputStream outputStream = new FileOutputStream(new File(
                    "D:\\newParameter.cpt"));
            EmbeddedTableDataExporter templateExporter = new EmbeddedTableDataExporter();
            templateExporter.export(outputStream, workbook);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SimpleWork.checkOut();
        }
    }
}