package com.fr.demo;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.general.FRFont;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.Constants;
import com.fr.stable.unit.OLDPIX;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;


public class SetCellElementStyle extends Reportlet {
    public TemplateWorkBook createReport(ReportletRequest arg0) {
        // Create a new workbook.
        WorkBook workbook = new WorkBook();
        WorkSheet worksheet = new WorkSheet();

        // Create a cell element.
        TemplateCellElement cellElement = new DefaultTemplateCellElement(1, 1,
                2, 2, "FineReport");

        // Set the column width and the row height of the worksheet.
        worksheet.setColumnWidth(1, new OLDPIX(300));
        worksheet.setRowHeight(1, new OLDPIX(30));

        // Get the style from the cell.
        Style style = cellElement.getStyle();
        if (style == null) {
            style = Style.getInstance();
        }

        // Font setting
        FRFont frFont = FRFont.getInstance("Dialog", Font.BOLD, 16);
        frFont = frFont.applyForeground(new Color(21, 76, 160));
        style = style.deriveFRFont(frFont);

        // Background setting
        ColorBackground background = ColorBackground.getInstance(new Color(255,
                255, 177));
        style = style.deriveBackground(background);

        // Alignment setting
        style = style.deriveHorizontalAlignment(Constants.CENTER);

        // Border setting
        style = style.deriveBorder(Constants.LINE_DASH, Color.red,
                Constants.LINE_DOT, Color.gray, Constants.LINE_DASH_DOT,
                Color.BLUE, Constants.LINE_DOUBLE, Color.CYAN);

        // Set the style for the cell.
        cellElement.setStyle(style);

        // Add the cell to the workbook.
        worksheet.addCellElement(cellElement);
        workbook.addReport(worksheet);
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