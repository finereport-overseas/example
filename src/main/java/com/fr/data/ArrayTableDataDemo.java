package com.fr.data;

public class ArrayTableDataDemo extends AbstractTableData {
    /**
     * Define a String array for column names and a two-dimensional array to store data
     */
    private String[] columnNames;
    private Object[][] rowData;

    /**
     * Prepare data in the constructor.
     */
    public ArrayTableDataDemo() {
        String[] columnNames = {"Name", "Score"};
        Object[][] datas = {{"Alex", 15},
                {"Helly", 22}, {"Bobby", 99}};
        this.columnNames = columnNames;
        this.rowData = datas;
    }

    // Since hasRow() has been implemented in AbstractTableData, here we only need to implement the other four functions.

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return rowData.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData[rowIndex][columnIndex];
    }
}