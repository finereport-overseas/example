package com.fr.demo;

import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.FRLogger;
import com.fr.general.I18nResource;
import com.fr.io.TemplateWorkBookIO;
import com.fr.log.FineLoggerFactory;
import com.fr.log.LogHandler;
import com.fr.main.impl.WorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;
import junit.framework.TestCase;
import org.junit.Assert;

public class ModuleStartTest extends TestCase {
    final TestLogAppender logAppender = new TestLogAppender();

    @Override
    protected void setUp() throws Exception {
        LogHandler logHandler = new LogHandler<TestLogAppender>() {
            @Override
            public TestLogAppender getHandler() {
                return logAppender;
            }
        };
        FineLoggerFactory.registerLogger(FRLogger.getLogger());
        FineLoggerFactory.getLogger().addLogAppender(logHandler);
    }

    public void testModuleStartAndShutdown() {
        for (int i = 0; i < 2; i++) {
            Module module = initModule();
            SimpleWork.supply(CommonOperator.class, new CommonOperatorImpl());
            SimpleWork.checkIn(ModuleStartTest.class.getResource("../../../WEB-INF").getPath());
            I18nResource.getInstance();

            module.start();
            WorkBook workbook = null;
            try {
                workbook = (WorkBook) TemplateWorkBookIO
                        .readTemplateWorkBook("//demo.cpt");
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            Assert.assertNotNull(workbook);
            module.stop();
        }
        Assert.assertEquals(logAppender.getErrorList().size(), 0);

    }

    private Module initModule() {
        Module module = ActivatorToolBox.simpleLink(new BaseDBActivator(),
                new ConfigurationActivator(),
                new StandaloneModeActivator(),
                new StateServiceActivator(),
                new ChartBaseActivator(),
                new SchedulerActivator(),
                new ReportBaseActivator(),
                new RestrictionActivator(),
                new ReportActivator(),
                new WriteActivator()
                );
        return module;
    }

    @Override
    protected void tearDown() throws Exception {
        logAppender.close();
    }
}