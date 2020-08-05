package com.fr.io;

import com.fr.base.TableData;
import com.fr.base.operator.common.CommonOperator;
import com.fr.chart.activator.ChartBaseActivator;
import com.fr.cluster.engine.activator.standalone.StandaloneModeActivator;
import com.fr.config.activator.BaseDBActivator;
import com.fr.config.activator.ConfigurationActivator;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.env.operator.CommonOperatorImpl;
import com.fr.general.I18nResource;
import com.fr.general.data.TableDataColumn;
import com.fr.health.activator.ModuleHealActivator;
import com.fr.main.impl.WorkBook;
import com.fr.module.Module;
import com.fr.module.tool.ActivatorToolBox;
import com.fr.report.ReportActivator;
import com.fr.report.RestrictionActivator;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.module.ReportBaseActivator;
import com.fr.report.worksheet.WorkSheet;
import com.fr.report.write.WriteActivator;
import com.fr.scheduler.SchedulerActivator;
import com.fr.store.StateServiceActivator;
import com.fr.workspace.simple.SimpleWork;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class CreateGenericTemplate {
    public static void main(String[] args) throws Exception {
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
        String envpath = "//Applications//FineReport10_325//webapps//webroot//WEB-INF";//project path
        SimpleWork.checkIn(envpath);
        I18nResource.getInstance();
        module.start();


        WorkBook wb = new WorkBook();
        //add new datasets
        TableData td = genericTableData("FRDemo", "SELECT * FROM Equipment");
        wb.putTableData("company information", td);
        //create the first report,which is worksheet
        WorkSheet report = new WorkSheet();
        wb.addReport(report);
        //database reading is suspended for now and  "fake" some temporary data
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "Company");
        map.put(1, "Tel");
        //add colum head
        DefaultTemplateCellElement title = genericCell(0, 0, null, "this is title");
        title.setRowSpan(2);
        title.setColumnSpan(2);
        report.addCellElement(title);
        //add data column
        for (Integer key : map.keySet()) {
            TemplateCellElement cellHeaer = genericCell(2, key, null, map.get(key));
            report.addCellElement(cellHeaer);
            TemplateCellElement cell = genericCell(3, key, "公司信息", map.get(key));
            report.addCellElement(cell);
        }
        // export the template
        FileOutputStream outputStream = new FileOutputStream(new File("//Users//susie//Downloads//company.cpt"));
        wb.export(outputStream);
        outputStream.close();
        module.stop();
        System.out.println("finished");
    }
    /**
     * generates Table Data
     */
    private static TableData genericTableData(String conString, String sqlQuery) {
        NameDatabaseConnection database = new NameDatabaseConnection(conString);
        TableData td = new DBTableData(database, sqlQuery);
        return td;
    }
    /**
     *  add cells
     * row (row number)
     * column (column number)
     * dsName (dataset name such as ds1)
     * data column name such as id column in ds1, simply type in id
     */
    private static DefaultTemplateCellElement genericCell(int row, int column, String dsName, String columnName) {
        DefaultTemplateCellElement dtCell = new DefaultTemplateCellElement(row, column);
        if (dsName != null) {
            DSColumn dsColumn = new DSColumn();
            dsColumn.setDSName(dsName);
            dsColumn.setColumn(TableDataColumn.createColumn(columnName));
            dtCell.setValue(dsColumn);
        } else {
            dtCell.setValue(columnName);
        }
        return dtCell;
    }
}