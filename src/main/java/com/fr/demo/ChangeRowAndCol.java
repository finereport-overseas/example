//traverse the cell
package com.fr.demo;

import com.fr.io.TemplateWorkBookIO;
import com.fr.main.TemplateWorkBook;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;

import java.util.Map;


public class ChangeRowAndCol extends Reportlet {
    public TemplateWorkBook createReport(ReportletRequest reportletrequest) {
        // define the workbook
        TemplateWorkBook workbook = null;
        WorkSheet newworksheet = new WorkSheet();
        String change = "0";
        try {
            // read the template and save as workbook
            workbook = TemplateWorkBookIO.readTemplateWorkBook("//doc//Primary//GroupReport//Group.cpt");
            // read the parameters to decide whether row and column need to switch, 0 means no 1 means yes
            if (reportletrequest.getParameter("change") != null) {
                change = reportletrequest.getParameter("change").toString();
            }
            if (change.equals("1")) {
                // get the report before getting the cell
                TemplateElementCase report = (TemplateElementCase) workbook
                        .getTemplateReport(0);
                // traverse the cell
                int col = 0, row = 0;
                byte direction = 0;
                java.util.Iterator it = report.cellIterator();
                while (it.hasNext()) {
                    TemplateCellElement cell = (TemplateCellElement) it.next();
                    // get the row and column and then switch
                    col = cell.getColumn();
                    row = cell.getRow();
                    cell.setColumn(row);
                    cell.setRow(col);
                    // get the expanding direction, 0 means vertical , 1 means horizontal 
                    direction = cell.getCellExpandAttr().getDirection();
                    if (direction == 0) {
                        cell.getCellExpandAttr().setDirection((byte) 1);
                    } else if (direction == 1) {
                        cell.getCellExpandAttr().setDirection((byte) 0);
                    }
                    // add the changed cell to the new worksheet
                    newworksheet.addCellElement(cell);
                }
                // replace the original sheet 
                workbook.setReport(0, newworksheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    @Override
    public void setParameterMap(Map arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTplPath(String arg0) {
        // TODO Auto-generated method stub

    }
}
