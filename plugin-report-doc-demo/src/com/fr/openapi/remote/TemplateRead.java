package com.fr.openapi.remote;

import com.fr.config.activator.ConfigurationActivator;
import com.fr.data.impl.config.activator.RestrictionActivator;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.env.RemoteDesignerWorkspaceInfo;
import com.fr.io.TemplateWorkBookIO;
import com.fr.log.FineLoggerFactory;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.module.Activator;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.module.ReportBaseActivator;
import com.fr.serialization.SerializationActivator;
import com.fr.stable.PageActor;
import com.fr.startup.WorkspaceRegister;
import com.fr.store.StateServerActivator;
import com.fr.workspace.WorkContext;
import com.fr.workspace.connect.WorkspaceConnectionInfo;
import com.fr.workspace.engine.WorkspaceActivator;
import com.fr.workspace.server.ServerWorkspaceRegister;

import java.util.HashMap;

/**
 * 远程环境读取模板
 */
public class TemplateRead {

    public static void main(String[] args) {
        try {
            Module module = ActivatorToolBox.simpleLink(
                    new WorkspaceActivator(),
                    new SerializationActivator(),
                    new Activator() {
                        @Override
                        public void start() {
                            WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo("http://远程服务器地址:8080/webroot/decision", "admin", "ilovejava", "", "");
                            try {
                                WorkContext.switchTo(DesignerWorkspaceGenerator.generate(RemoteDesignerWorkspaceInfo.create(connectionInfo)));
                            } catch (Exception e) {
                               e.printStackTrace();
                            }
                        }

                        @Override
                        public void stop() {

                        }
                    },
                    new ConfigurationActivator(),
                    new StateServerActivator(),
                    new ReportBaseActivator(),
                    new RestrictionActivator(),
                    new ReportActivator(),
                    new WorkspaceRegister(),
                    new ServerWorkspaceRegister()
            );
            module.start();
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook("12345678.cpt");
            ResultWorkBook result = workbook.execute(new HashMap<String, Object>(), new PageActor());

            module.stop();//停止module
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);

        }

    }

}