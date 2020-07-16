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
import com.fr.main.TemplateWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.print.PrintUtils;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.util.HashMap;

public class JavaPrint {
    public static void main(String[] args) {
        // 首先需要定义执行所在的环境，这样才能正确读取数据库信息
        // 定义报表运行环境,用于执行报表
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
        String envpath = "//Applications//FineReport10_325//webapps//webroot//WEB-INF";//工程路径
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();

        try {
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook("GettingStarted.cpt");
            // 参数传值
            Parameter[] parameters = workbook.getParameters();
            HashMap<String, String> paraMap = new HashMap<String, String>();
            paraMap.put(parameters[0].getName(), "华北");
            // java中调用报表打印方法
            boolean a = PrintUtils.printWorkBook("GettingStarted.cpt", paraMap, true);
            if (!a) {
                System.out.println("失败啦！返回" + a);
            } else {
                System.out.println("成功！返回" + a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            module.stop();
        }
    }
}