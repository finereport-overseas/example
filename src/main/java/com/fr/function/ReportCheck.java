package com.fr.function;

import com.fr.base.ResultFormula;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.io.TemplateWorkBookIO;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.main.impl.WorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.cell.CellElement;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.report.ResultReport;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.script.AbstractFunction;
import com.fr.stable.WriteActor;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;
import com.fr.write.cal.WB;

import java.util.HashMap;

public class ReportCheck extends AbstractFunction {
    private static HashMap wMap = new HashMap();

    public Object run(Object[] args) {
        String cptname = args[0].toString(); // get CPT name
        int colnumber = Integer.parseInt(args[2].toString()); // get the column of the cell
        int rownumber = Integer.parseInt(args[3].toString()); // get the row of the cell
        // define the return value
        Object returnValue = null;
        // define the running env to execute report
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
        String envpath = "D:\\Tools\\apache-tomcat-8.5.57\\webapps\\webroot\\WEB-INF";
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();


        try {
            ResultWorkBook rworkbook = null;
            // read the template
            WorkBook workbook = (WorkBook)TemplateWorkBookIO
                    .readTemplateWorkBook(cptname);
            // get the parameters should be passed to the report, e.g.[{"name":para1name,"value":para1value},{"name":para2name,"value":para2value},......]
            JSONArray parasArray = new JSONArray(args[1].toString());
            // retrieve the cached result workbook
            Object tempWObj = wMap.get(cptname + parasArray.toString());
            if (tempWObj != null) {
                TpObj curTpObj = (TpObj) tempWObj;

                if ((System.currentTimeMillis() - curTpObj.getExeTime()) < 8000) {
                    rworkbook = curTpObj.getRworkbook();
                } else {
                    wMap.remove(cptname + parasArray.toString());
                }
            }
            // The result workbook needs to be recreated if no longer in the cache.
            if (rworkbook == null) {
                JSONObject jo = new JSONObject();
                java.util.Map parameterMap = new java.util.HashMap();
                if (parasArray.length() > 0) {
                    for (int i = 0; i < parasArray.length(); i++) {
                        jo = parasArray.getJSONObject(i);
                        parameterMap.put(jo.get("name"), jo.get("value"));
                    }
                }
                // execute the template
                rworkbook = workbook.execute(parameterMap, new WriteActor());
                // save it to cache
                wMap.put(cptname + parasArray.toString(), new TpObj(rworkbook,
                        System.currentTimeMillis()));
            }
            // get the value of the cell from the result workbook
            ResultReport report = rworkbook.getResultReport(0);
            CellElement cellElement = ((WB) report).getCellElement(colnumber, rownumber);
            returnValue = cellElement.getValue().toString();
            if(cellElement.getValue() instanceof ResultFormula) {
                returnValue = ((ResultFormula)cellElement.getValue()).getResult().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    class TpObj {
        private ResultWorkBook rworkbook = null;
        private long exeTime = System.currentTimeMillis();

        public TpObj(ResultWorkBook rworkbook, long exeTime) {
            this.setRworkbook(rworkbook);
            this.setExeTime(exeTime);
        }

        public ResultWorkBook getRworkbook() {
            return rworkbook;
        }

        public void setRworkbook(ResultWorkBook rworkbook) {
            this.rworkbook = rworkbook;
        }

        public long getExeTime() {
            return exeTime;
        }

        public void setExeTime(long exeTime) {
            this.exeTime = exeTime;
        }
    }

}